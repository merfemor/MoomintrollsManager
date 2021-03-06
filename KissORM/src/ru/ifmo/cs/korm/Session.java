package ru.ifmo.cs.korm;

import ru.ifmo.cs.korm.mapping.MappingAttribute;
import ru.ifmo.cs.korm.mapping.MappingTable;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Session {
    private final String url;
    private SQLSyntax sqlSyntax;
    private Connection connection;
    private Map<Class, MappingTable> mapping = new HashMap<>();

    public Session(Connection connection, SQLSyntax sqlSyntax) throws SQLException {
        this.connection = connection;
        this.sqlSyntax = sqlSyntax;
        this.url = connection.getMetaData().getURL();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public Session addClass(Class annotatedClass) throws SQLException {
        MappingTable mappingTable = new MappingTable(annotatedClass);
        assertTableExists(mappingTable);
        this.mapping.put(annotatedClass, mappingTable);
        return this;
    }

    private void assertTableExists(MappingTable table) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(
                null, null, table.getName(), null);
        if (!tables.next()) {
            // TODO: create table
            throw new RuntimeException("Table " + table.getName() + " isn't exists");
        }
    }

    public void add(Object[] objects) {
        if (objects == null || objects.length == 0)
            return;
        MappingTable mappingTable = mapping.get(objects[0].getClass());
        if (mappingTable == null) {
            throw new IllegalArgumentException("Table for class " + objects.getClass() + " isn't mapped");
        }
        String sql = "INSERT INTO " + mappingTable.getName() + " (" + mappingTable.getNotAutoGeneratedAttributes()
                .stream()
                .map(ma -> ma.tName)
                .collect(Collectors.joining(", ")) +
                ") VALUES (" +
                new String(new char[mappingTable.getNotAutoGeneratedAttributes().size()])
                        .replace("\0", "?").replace("??", "?, ?").replace("??", "?, ?") +
                ");";

        PreparedStatement statement;
        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            for (Object object : objects) {
                fillWithAttributes(object, statement, mappingTable.getNotAutoGeneratedAttributes());
                statement.executeUpdate();
                if (!mappingTable.getAutoGeneratedAttributes().isEmpty()) {
                    ResultSet autoGenIds = statement.getGeneratedKeys();
                    for (MappingAttribute ma : mappingTable.getAutoGeneratedAttributes()) {
                        autoGenIds.next();
                        ma.setter.invoke(
                                object,
                                sqlSyntax.fromJdbcObject(
                                        autoGenIds.getObject(
                                                ma.tName,
                                                sqlSyntax.toSQLtype(ma.oClass)
                                        ), ma.oClass
                                )
                        );
                    }
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T[] getData(Class<T> c) {
        MappingTable table = mapping.get(c);
        if (table == null)
            throw new IllegalArgumentException("No mapping for class " + c);
        List<T> collection = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM " + table.name);
            while (resultSet.next()) {
                T o = c.newInstance();
                Stream.concat(
                        table.getAutoGeneratedAttributes().stream(),
                        table.getNotAutoGeneratedAttributes().stream())
                        .forEach(ma -> {

                        });
                for (MappingAttribute ma : table.getAutoGeneratedAttributes())
                    ma.setter.invoke(
                            o,
                            sqlSyntax.fromJdbcObject(
                                    resultSet.getObject(ma.tName, sqlSyntax.toSQLtype(ma.oClass)),
                                    ma.oClass));
                for (MappingAttribute ma : table.getNotAutoGeneratedAttributes())
                    ma.setter.invoke(
                            o,
                            sqlSyntax.fromJdbcObject(
                                    resultSet.getObject(ma.tName, sqlSyntax.toSQLtype(ma.oClass)),
                                    ma.oClass));
                collection.add(o);
            }
        } catch (SQLException | IllegalAccessException | InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        T[] ts = (T[]) Array.newInstance(c, 0);
        return collection.toArray(ts);
    }

    public void update(Object[] objects) {
        if (objects == null || objects.length == 0)
            return;
        MappingTable table = mapping.get(objects[0].getClass());
        if (table == null)
            throw new IllegalArgumentException("No mapping for class " + objects[0].getClass());

        String sql = "UPDATE " + table.getName() + " SET " +
                table.getNotAutoGeneratedAttributes()
                        .stream()
                        .map(ma -> ma.tName)
                        .collect(Collectors.joining(" = ?, ")) + " = ?" +
                " WHERE " +
                table.getIdAttributes()
                        .stream()
                        .map(ma -> ma.tName)
                        .collect(Collectors.joining(" = ? AND ")) + " = ?;";
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (Object o : objects) {
                fillWithAttributes(o, preparedStatement, table.getNotAutoGeneratedAttributes());
                fillWithAttributes(o, preparedStatement, table.getIdAttributes(),
                        table.getNotAutoGeneratedAttributes().size() + 1);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void fillWithAttributes(Object object, PreparedStatement statement, Collection<MappingAttribute> mappingAttributes) throws InvocationTargetException, IllegalAccessException, SQLException {
        fillWithAttributes(object, statement, mappingAttributes, 1);
    }

    private void fillWithAttributes(Object object, PreparedStatement statement, Collection<MappingAttribute> mappingAttributes, int start) throws InvocationTargetException, IllegalAccessException, SQLException {
        for (MappingAttribute ma : mappingAttributes) {
            statement.setObject(start, sqlSyntax.toJdbcObject(ma.getter.invoke(object)));
            start++;
        }
    }

    public void remove(Object[] objects) {
        if (objects == null || objects.length == 0)
            return;
        MappingTable table = mapping.get(objects[0].getClass());
        if (table == null)
            throw new IllegalArgumentException("No mapping for class " + objects[0].getClass());

        String sql = "DELETE FROM " + table.getName() +
                " WHERE " +
                table.getIdAttributes()
                        .stream()
                        .map(ma -> ma.tName)
                        .collect(Collectors.joining(" = ? AND ")) + " = ?;";
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (Object o : objects) {
                fillWithAttributes(o, preparedStatement, table.getIdAttributes());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUrl() {
        return url;
    }
}

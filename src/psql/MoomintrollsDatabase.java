package psql;

import com.sun.rowset.CachedRowSetImpl;
import trolls.Kindness;
import trolls.Moomintroll;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MoomintrollsDatabase extends PSQLClient {
    private final String[] fieldsNames = {
            "moomintroll_id",
            "name",
            "is_male",
            "color",
            "kindness",
            "position"
    };

    private final String INSERT_FIELDS
            = Arrays.stream(fieldsNames).skip(1).collect(Collectors.joining(", "));
    private final String UPDATE_MOOMINTROLL_FUNCTION = "update_moomintroll";


    public MoomintrollsDatabase(String hostname, int port, String database, String username, String password, String tableName) throws SQLException {
        super(hostname, port, database, username, password);
        setTableName(tableName);
    }

    public MoomintrollsDatabase(String hostname, int port, String database, String username, String password) throws SQLException {
        this(hostname, port, database, username, password, "moomintroll");
    }

    protected String getFields(Moomintroll moomintroll) {
        return "\'" + moomintroll.getName() + "\', " +
                (moomintroll.isMale() ? "true" : "false") + ", " +
                moomintroll.getRgbBodyColor().getRGB() + ", " +
                moomintroll.getKindness().value() + ", " +
                moomintroll.getPosition();
    }

    public long[] insert(Moomintroll[] moomintrolls) throws SQLException {

        PreparedStatement preparedInsertStatement = connection.prepareStatement(
                "INSERT INTO " + tableName + "(" + INSERT_FIELDS + ") VALUES (?, ?, ?, ?, ?);",
                Statement.RETURN_GENERATED_KEYS);

        long[] generatedIds = new long[moomintrolls.length];
        connection.setAutoCommit(false);

        for (int i = 0; i < moomintrolls.length; i++) {
            Moomintroll moomintroll = moomintrolls[i];
            preparedInsertStatement.setString(1, moomintroll.getName());
            preparedInsertStatement.setBoolean(2, moomintroll.isMale());
            preparedInsertStatement.setInt(3, moomintroll.getRgbBodyColor().getRGB());
            preparedInsertStatement.setInt(4, moomintroll.getKindness().value());
            preparedInsertStatement.setInt(5, moomintroll.getPosition());
            preparedInsertStatement.executeUpdate();

            ResultSet rs = preparedInsertStatement.getGeneratedKeys();
            rs.next();
            generatedIds[i] = (rs.getLong(1));
        }
        connection.commit();
        connection.setAutoCommit(true);
        return generatedIds;
    }

    public long insert(Moomintroll moomintroll) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(
                "INSERT INTO " + tableName + "(" + INSERT_FIELDS + ") VALUES ("
                        + getFields(moomintroll) + ");",
                Statement.RETURN_GENERATED_KEYS
        );
        ResultSet generatedKeys = statement.getGeneratedKeys();
        generatedKeys.next();
        return generatedKeys.getLong(1);
    }

    public void delete(long moomintrollId) throws SQLException {
        connection.createStatement().executeUpdate(
                "DELETE FROM " + tableName + " WHERE " + fieldsNames[0] + " = " + moomintrollId
        );
    }

    public void delete(long[] ids) throws SQLException {
        connection.prepareStatement(
                "DELETE FROM " + tableName + " WHERE " + fieldsNames[0] + " IN (" +
                        Arrays.toString(ids).replaceAll("[\\[\\]]", "") + ");"
        ).execute();
    }

    public Map<Long, Moomintroll> toMap() throws SQLException {
        CachedRowSet fullDataRowSet = new CachedRowSetImpl();
        fullDataRowSet.setCommand("SELECT * FROM " + tableName);
        fullDataRowSet.setReadOnly(true);
        fullDataRowSet.execute(connection);

        Map<Long, Moomintroll> moomintrollMap = new HashMap<>(fullDataRowSet.size());
        while (fullDataRowSet.next()) {
            moomintrollMap.put(fullDataRowSet.getLong(fieldsNames[0]),
                    new Moomintroll(
                            fullDataRowSet.getString(fieldsNames[1]),
                            fullDataRowSet.getBoolean(fieldsNames[2]),
                            fullDataRowSet.getInt(fieldsNames[5]),
                            new Color(fullDataRowSet.getInt(fieldsNames[3])),
                            new Kindness(fullDataRowSet.getInt(fieldsNames[4]))
                    ));
        }
        return moomintrollMap;
    }

    public void update(long id, Moomintroll moomintroll) throws SQLException {
        CallableStatement statement = connection.prepareCall(
                "{call " + UPDATE_MOOMINTROLL_FUNCTION + "(?, ?, ?, ?, ?, ?)}");
        statement.setLong(1, id);
        statement.setString(2, moomintroll.getName());
        statement.setBoolean(3, moomintroll.isMale());
        statement.setInt(4, moomintroll.getRgbBodyColor().getRGB());
        statement.setInt(5, moomintroll.getKindness().value());
        statement.setInt(6, moomintroll.getPosition());
        statement.execute();
    }
}

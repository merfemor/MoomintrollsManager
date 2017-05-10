package psql;

import com.sun.rowset.CachedRowSetImpl;
import trolls.Kindness;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MoomintrollsCollectionPSQLClient extends PSQLClient {
    private String tableName = "moomintroll";
    private String[] fieldsNames = {
            "moomintroll_id",
            "name",
            "is_male",
            "color",
            "kindness",
            "position"
    };

    private String NOT_SERIAL_FIELDS;

    public MoomintrollsCollectionPSQLClient(String hostname, int port, String database, String username, String password) throws SQLException {
        super(hostname, port, database, username, password);
        connection.setAutoCommit(false);
        generateNotSerialFields();
    }

    public void setFieldsNames(String[] fieldsNames) {
        this.fieldsNames = fieldsNames;
    }

    private void generateNotSerialFields() {
        NOT_SERIAL_FIELDS = Arrays.stream(fieldsNames).skip(1).collect(Collectors.joining(", "));
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void add(Moomintroll moomintroll) throws SQLException {
        CachedRowSet cachedRowSet = selectAllNotSerial();
        cachedRowSet.moveToInsertRow();
        cachedRowSet.updateString(fieldsNames[1], moomintroll.getName());
        cachedRowSet.updateBoolean(fieldsNames[2], moomintroll.isMale());
        cachedRowSet.updateInt(fieldsNames[3], moomintroll.getRgbBodyColor().getRGB());
        cachedRowSet.updateInt(fieldsNames[4], moomintroll.getKindness().value());
        cachedRowSet.updateInt(fieldsNames[5], moomintroll.getPosition());
        cachedRowSet.insertRow();
        cachedRowSet.moveToCurrentRow();
        cachedRowSet.acceptChanges();
    }

    public void remove(int moomintrollId) throws SQLException {
        CachedRowSet cachedRowSet = new CachedRowSetImpl();
        cachedRowSet.setCommand(
                "SELECT " + NOT_SERIAL_FIELDS + " FROM " + tableName +
                " WHERE " + fieldsNames[0] + "=" + moomintrollId
        );
        cachedRowSet.execute(connection);
        cachedRowSet.next();
        cachedRowSet.deleteRow();
        cachedRowSet.acceptChanges();
    }

    public MoomintrollsCollection getFullTable() throws SQLException {
        CachedRowSet cachedRowSet = selectAllNotSerial();

        MoomintrollsCollection moomintrollsCollection = new MoomintrollsCollection();
        while (cachedRowSet.next()) {
            moomintrollsCollection.add(getMoomintroll(cachedRowSet));
        }
        return moomintrollsCollection;
    }

    private Moomintroll getMoomintroll(CachedRowSet cachedRowSet) throws SQLException {
        return new Moomintroll(
                cachedRowSet.getString(fieldsNames[1]),
                cachedRowSet.getBoolean(fieldsNames[2]),
                cachedRowSet.getInt(fieldsNames[3]),
                new Color(cachedRowSet.getInt(fieldsNames[4])),
                new Kindness(cachedRowSet.getInt(fieldsNames[5]))
        );
    }

    private CachedRowSet selectAllNotSerial() throws SQLException {
        CachedRowSet cachedRowSet = new CachedRowSetImpl();
        cachedRowSet.setCommand("SELECT " + NOT_SERIAL_FIELDS + " FROM " + tableName);
        cachedRowSet.execute(connection);
        return cachedRowSet;
    }
}

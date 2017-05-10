package psql;

import trolls.Kindness;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MoomintrollsDatabase extends PSQLClient {
    private String[] fieldsNames = {
            "moomintroll_id",
            "name",
            "is_male",
            "color",
            "kindness",
            "position"
    };

    private String NOT_SERIAL_FIELDS;

    public MoomintrollsDatabase(String hostname, int port, String database, String username, String password, String tableName) throws SQLException {
        super(hostname, port, database, username, password);
        setTableName(tableName);
        reloadData();
        generateNotSerialFields();
    }

    public MoomintrollsDatabase(String hostname, int port, String database, String username, String password) throws SQLException {
        this(hostname, port, database, username, password, "moomintroll");
    }

    public void setFieldsNames(String[] fieldsNames) {
        this.fieldsNames = fieldsNames;
    }

    private void generateNotSerialFields() {
        NOT_SERIAL_FIELDS = Arrays.stream(fieldsNames).skip(1).collect(Collectors.joining(", "));
    }

    public void add(Moomintroll moomintroll) throws SQLException {
        CachedRowSet crs = select(NOT_SERIAL_FIELDS);
        crs.moveToInsertRow();
        crs.updateString(fieldsNames[1], moomintroll.getName());
        crs.updateBoolean(fieldsNames[2], moomintroll.isMale());
        crs.updateInt(fieldsNames[3], moomintroll.getRgbBodyColor().getRGB());
        crs.updateInt(fieldsNames[4], moomintroll.getKindness().value());
        crs.updateInt(fieldsNames[5], moomintroll.getPosition());
        crs.insertRow();
        crs.moveToCurrentRow();
        crs.acceptChanges();

    }

    public boolean remove(int moomintrollId) throws SQLException {
        int[] ids = {moomintrollId};
        return remove(ids);
    }

    public boolean remove(int[] moomintrollIds) throws SQLException {
        boolean changed = false;
        fullDataRowSet.first();
        while (fullDataRowSet.next()) {
            for(int id: moomintrollIds) {
                if(fullDataRowSet.getInt(fieldsNames[0]) == id) {
                    fullDataRowSet.deleteRow();
                    changed = true;
                }
            }
        }
        fullDataRowSet.acceptChanges();
        return changed;
    }

    public MoomintrollsCollection getFullTable() throws SQLException {
        CachedRowSet crs = select(NOT_SERIAL_FIELDS);
        MoomintrollsCollection moomintrollsCollection = new MoomintrollsCollection();
        while (crs.next()) {
            moomintrollsCollection.add(getMoomintroll(crs));
        }
        return moomintrollsCollection;
    }

    private Moomintroll getMoomintroll(CachedRowSet crs) throws SQLException {
        return new Moomintroll(
                crs.getString(fieldsNames[1]),
                crs.getBoolean(fieldsNames[2]),
                crs.getInt(fieldsNames[3]),
                new Color(crs.getInt(fieldsNames[4])),
                new Kindness(crs.getInt(fieldsNames[5]))
        );
    }
}

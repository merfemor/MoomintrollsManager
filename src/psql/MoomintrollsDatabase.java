package psql;

import trolls.Kindness;
import trolls.Moomintroll;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        connection.setAutoCommit(false);
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

    public int insert(Moomintroll moomintroll) throws SQLException {
        CachedRowSet crs = select(NOT_SERIAL_FIELDS);
        crs.moveToInsertRow();
        update(crs, moomintroll);
        crs.insertRow();
        crs.moveToCurrentRow();
        crs.acceptChanges(connection);
        reloadData();
        fullDataRowSet.last();
        return fullDataRowSet.getInt(fieldsNames[0]);
    }

    public boolean delete(long moomintrollId) throws SQLException {
        return delete(new long[]{moomintrollId});
    }

    public boolean delete(long[] ids) throws SQLException {
        boolean changed = false;
        Set<Long> idsSet = Arrays.stream(ids).boxed().collect(Collectors.toSet());
        fullDataRowSet.beforeFirst();
        while (fullDataRowSet.next()) {
            if (idsSet.remove(fullDataRowSet.getLong(fieldsNames[0]))) {
                fullDataRowSet.deleteRow();
                changed = true;
            }
        }
        fullDataRowSet.acceptChanges(connection);
        return changed;
    }

    public Map<Long, Moomintroll> toMoomintrollsMap() throws SQLException {
        Map<Long, Moomintroll> moomintrollMap = new HashMap<>(fullDataRowSet.size());
        fullDataRowSet.beforeFirst();
        while (fullDataRowSet.next()) {
            moomintrollMap.put(fullDataRowSet.getLong(fieldsNames[0]),
                    getMoomintroll(fullDataRowSet));
        }
        return moomintrollMap;
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

    private void update(CachedRowSet crs, Moomintroll moomintroll) throws SQLException {
        crs.updateString(fieldsNames[1], moomintroll.getName());
        crs.updateBoolean(fieldsNames[2], moomintroll.isMale());
        crs.updateInt(fieldsNames[3], moomintroll.getRgbBodyColor().getRGB());
        crs.updateInt(fieldsNames[4], moomintroll.getKindness().value());
        crs.updateInt(fieldsNames[5], moomintroll.getPosition());
    }
}

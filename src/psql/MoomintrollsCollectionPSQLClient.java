package psql;

import com.sun.rowset.CachedRowSetImpl;
import trolls.Kindness;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collector;
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

    private CachedRowSet cachedRowSet;

    public MoomintrollsCollectionPSQLClient(String hostname, int port, String database, String username, String password) throws SQLException {
        super(hostname, port, database, username, password);
        connection.setAutoCommit(false);
        cachedRowSet = new CachedRowSetImpl();
        cachedRowSet.setCommand("SELECT " +
                Arrays.stream(fieldsNames).skip(1).collect(Collectors.joining(", "))
                + " FROM " + tableName);
        cachedRowSet.execute(connection);

    }

    public void setFieldsNames(String[] fieldsNames) {
        this.fieldsNames = fieldsNames;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void add(Moomintroll moomintroll) throws SQLException {
        cachedRowSet.moveToInsertRow();
        //cachedRowSet.updateNull(fieldsNames[0]);
        cachedRowSet.updateString(fieldsNames[1], moomintroll.getName());
        cachedRowSet.updateBoolean(fieldsNames[2], moomintroll.isMale());
        cachedRowSet.updateInt(fieldsNames[3], moomintroll.getRgbBodyColor().getRGB());
        cachedRowSet.updateInt(fieldsNames[4], moomintroll.getKindness().value());
        cachedRowSet.updateInt(fieldsNames[5], moomintroll.getPosition());
        cachedRowSet.insertRow();
        cachedRowSet.moveToCurrentRow();
        cachedRowSet.acceptChanges();
    }

    public MoomintrollsCollection getFullTable() throws SQLException {
        MoomintrollsCollection moomintrollsCollection = new MoomintrollsCollection();
        while (cachedRowSet.next()) {
            moomintrollsCollection.add(new Moomintroll(
                    cachedRowSet.getString(fieldsNames[1]),
                    cachedRowSet.getBoolean(fieldsNames[2]),
                    cachedRowSet.getInt(fieldsNames[3]),
                    new Color(cachedRowSet.getInt(fieldsNames[4])),
                    new Kindness(cachedRowSet.getInt(fieldsNames[5]))
            ));
        }
        return moomintrollsCollection;
    }
}

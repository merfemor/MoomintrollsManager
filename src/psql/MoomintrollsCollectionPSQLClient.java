package psql;

import com.sun.rowset.CachedRowSetImpl;
import trolls.Kindness;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import javax.sql.rowset.CachedRowSet;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Statement;

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

    public MoomintrollsCollectionPSQLClient(String hostname, int port, String database, String username, String password) throws SQLException {
        super(hostname, port, database, username, password);
    }

    public void setFieldsNames(String[] fieldsNames) {
        this.fieldsNames = fieldsNames;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public MoomintrollsCollection getFullTable() throws SQLException {
        CachedRowSet crs = new CachedRowSetImpl();
        crs.setReadOnly(true);
        crs.setCommand("SELECT * FROM " + tableName);
        crs.execute(connection);
        MoomintrollsCollection moomintrollsCollection = new MoomintrollsCollection();
        while (crs.next()) {
            moomintrollsCollection.add(new Moomintroll(
                    crs.getString(fieldsNames[1]),
                    crs.getBoolean(fieldsNames[2]),
                    crs.getInt(fieldsNames[3]),
                    new Color(crs.getInt(fieldsNames[4])),
                    new Kindness(crs.getInt(fieldsNames[5]))
            ));
        }
        return moomintrollsCollection;
    }
}

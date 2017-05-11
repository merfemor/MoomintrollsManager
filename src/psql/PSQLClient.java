package psql;

import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PSQLClient {
    protected String tableName;
    protected String hostname, username, password, url, database;
    protected int port;
    protected Connection connection;
    protected CachedRowSet fullDataRowSet;

    public PSQLClient(String hostname, int port, String database, String username, String password) throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
        String url = "jdbc:postgresql://" + hostname + ":" + port + "/" + database +
                "?user=" + username + "&password=" + password;
        this.connection = DriverManager.getConnection(url);
        this.url = url;
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    protected CachedRowSet select(String fields) throws SQLException {
        CachedRowSet crs = new CachedRowSetImpl();
        crs.setCommand("SELECT " + fields + " FROM " + tableName);
        crs.execute(connection);
        return crs;
    }

    protected void reloadData() throws SQLException {
        fullDataRowSet = select("*");
        fullDataRowSet.execute(connection);
    }
}

package psql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PSQLClient {
    protected String tableName;
    protected String hostname, username, password, url, database;
    protected int port;
    protected Connection connection;

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

    public String getUrl() {
        return url;
    }

    public void close() throws SQLException {
        connection.close();
    }
}

import ru.ifmo.cs.korm.SQLSyntax;
import ru.ifmo.cs.korm.Session;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Utils {
    private final static String URL = "jdbc:postgresql://localhost:5432/mooman?user=usr&password=123456";

    public static Session getNewSession() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            return null;
        }
        try {
            return new Session(DriverManager.getConnection(URL), SQLSyntax.get());
        } catch (SQLException e) {
            return null;
        }
    }
}

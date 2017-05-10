import psql.MoomintrollsCollectionPSQLClient;

import java.sql.SQLException;

public class TestRemove {
    public static void main(String[] args) {
        MoomintrollsCollectionPSQLClient psqlClient;
        try {
            psqlClient = new MoomintrollsCollectionPSQLClient(
                    "localhost",
                    5432,
                    "mooman",
                    "usr",
                    "123456"
            );
        } catch (SQLException e) {
            return;
        }

        try {
            PrintDatabase.getAndPrintDatabase(psqlClient);
            psqlClient.remove(4);

            PrintDatabase.getAndPrintDatabase(psqlClient);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

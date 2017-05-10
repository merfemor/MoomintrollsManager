import psql.MoomintrollsCollectionPSQLClient;
import trolls.MoomintrollsCollection;
import trolls.utils.MoomintrollUtils;

import java.sql.SQLException;

public class PrintDatabase {
    public static void getAndPrintDatabase(MoomintrollsCollectionPSQLClient psqlClient) throws SQLException {
        MoomintrollsCollection moomintrolls;
        moomintrolls = psqlClient.getFullTable();
        moomintrolls.forEach(MoomintrollUtils::beautifulPrint);
        System.out.println("--------\n" + moomintrolls.size() + " trolls");
    }

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
            getAndPrintDatabase(psqlClient);
        } catch (SQLException e) {
            return;
        }
    }
}
import psql.MoomintrollsCollectionPSQLClient;
import trolls.Moomintroll;
import trolls.utils.MoomintrollUtils;

import java.sql.SQLException;

public class TestAddToDatabase {
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
            System.out.println();
            Moomintroll moomintroll = trolls.utils.Random.randomTroll();
            MoomintrollUtils.beautifulPrint(moomintroll);
            psqlClient.add(moomintroll);
            System.out.println();
            PrintDatabase.getAndPrintDatabase(psqlClient);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

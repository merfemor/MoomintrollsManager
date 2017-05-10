import psql.MoomintrollsDatabase;
import trolls.Moomintroll;
import trolls.utils.MoomintrollUtils;

import java.sql.SQLException;

public class TestAddToDatabase {
    public static void main(String[] args) {
        try {
            MoomintrollsDatabase moomintrollsDatabase = Databases.getTestDatabaseConnection();
            Databases.getAndPrintDatabase(moomintrollsDatabase);
            System.out.println();

            for(int i = 0; i < 10; i++) {
                Moomintroll moomintroll = trolls.utils.Random.randomTroll();
                MoomintrollUtils.beautifulPrint(moomintroll);
                int id = moomintrollsDatabase.add(moomintroll);
                System.out.println("\nAdded with id = " + id);
            }

            System.out.println();
            Databases.getAndPrintDatabase(moomintrollsDatabase);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

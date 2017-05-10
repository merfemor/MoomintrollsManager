import psql.MoomintrollsDatabase;

import java.sql.SQLException;
import java.util.stream.IntStream;

public class TestRemove {
    public static void main(String[] args) {
        try {
            MoomintrollsDatabase moomintrollsDatabase = Databases.getTestDatabaseConnection();
            Databases.getAndPrintDatabase(moomintrollsDatabase);

            moomintrollsDatabase.delete(IntStream.rangeClosed(79, 79).toArray());

            Databases.getAndPrintDatabase(moomintrollsDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

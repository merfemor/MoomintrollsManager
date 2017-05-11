import psql.MoomintrollsDatabase;

import java.sql.SQLException;
import java.util.stream.LongStream;

public class TestRemove {
    public static void main(String[] args) {
        try {
            MoomintrollsDatabase moomintrollsDatabase = Databases.getTestDatabaseConnection();
            Databases.getAndPrintDatabase(moomintrollsDatabase);

            moomintrollsDatabase.delete(LongStream.rangeClosed(134, 141).toArray());

            Databases.getAndPrintDatabase(moomintrollsDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

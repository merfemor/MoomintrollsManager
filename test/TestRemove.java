import psql.MoomintrollsDatabase;

import java.sql.SQLException;
import java.util.stream.IntStream;

public class TestRemove {
    public static void main(String[] args) {
        MoomintrollsDatabase psqlClient;
        try {
            psqlClient = new MoomintrollsDatabase(
                    "localhost",
                    5432,
                    "mooman",
                    "usr",
                    "123456"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            PrintDatabase.getAndPrintDatabase(psqlClient);
            psqlClient.remove(IntStream.rangeClosed(16, 21).toArray());

            PrintDatabase.getAndPrintDatabase(psqlClient);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

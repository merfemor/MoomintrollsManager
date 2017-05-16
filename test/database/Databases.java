package database;

import psql.MoomintrollsDatabase;
import trolls.Moomintroll;

import java.sql.SQLException;
import java.util.Map;

public class Databases {
    public static void getAndPrintDatabase(MoomintrollsDatabase psqlClient) throws SQLException {
        Map<Long, Moomintroll> moomintrolls;
        moomintrolls = psqlClient.toMap();
        moomintrolls.forEach((key, value) -> {
            System.out.print(key + ": " + value);
        });
        System.out.println("--------\n" + moomintrolls.size() + " trolls");
    }

    public static MoomintrollsDatabase getTestDatabaseConnection() throws SQLException {
        return new MoomintrollsDatabase(
                "localhost",
                5432,
                "mooman",
                "usr",
                "123456"
        );
    }
}
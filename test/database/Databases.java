package database;

import psql.MoomintrollsDatabase;
import trolls.Moomintroll;

import java.sql.SQLException;

public class Databases {
    public static void getAndPrintDatabase(MoomintrollsDatabase psqlClient) throws SQLException {
        Moomintroll[] moomintrolls;
        moomintrolls = psqlClient.toArray();
        for (Moomintroll m : moomintrolls) {
            System.out.println(m.getId() + " : " + m);
        }
        System.out.println("--------\n" + moomintrolls.length + " trolls");
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
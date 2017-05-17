package database;

import net.IdentifiedMoomintroll;
import psql.MoomintrollsDatabase;

import java.sql.SQLException;

public class Databases {
    public static void getAndPrintDatabase(MoomintrollsDatabase psqlClient) throws SQLException {
        IdentifiedMoomintroll[] moomintrolls;
        moomintrolls = psqlClient.toArray();
        for (IdentifiedMoomintroll m : moomintrolls) {
            System.out.println(m.id() + " : " + m.moomintroll());
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
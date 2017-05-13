package database;

import psql.MoomintrollsDatabase;

import java.sql.SQLException;
import java.util.stream.LongStream;

public class TestRemove {
    public static void main(String[] args) {
        try {
            MoomintrollsDatabase moomintrollsDatabase = Databases.getTestDatabaseConnection();
            Databases.getAndPrintDatabase(moomintrollsDatabase);

            moomintrollsDatabase.delete(LongStream.rangeClosed(140, 500).toArray());
            moomintrollsDatabase.delete(210);

            Databases.getAndPrintDatabase(moomintrollsDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

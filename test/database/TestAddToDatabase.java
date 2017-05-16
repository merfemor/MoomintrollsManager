package database;

import psql.MoomintrollsDatabase;
import trolls.Moomintroll;
import trolls.utils.Random;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

public class TestAddToDatabase {
    public static void main(String[] args) {
        try {
            MoomintrollsDatabase moomintrollsDatabase = Databases.getTestDatabaseConnection();
            Databases.getAndPrintDatabase(moomintrollsDatabase);
            System.out.println();


            System.out.println("Group insert");
            Moomintroll[] moomintrolls = Stream.generate(Random::randomTroll).limit(5).toArray(Moomintroll[]::new);
            Arrays.asList(moomintrolls).forEach(System.out::println);
            long[] ids = moomintrollsDatabase.insert(moomintrolls);

            System.out.println(Arrays.toString(ids) + "\n\n");
            System.out.println("One item insert");
            Moomintroll moomintroll = Random.randomTroll();
            System.out.println(moomintroll + "Id: " + moomintrollsDatabase.insert(moomintroll) + "\n\n");

            Databases.getAndPrintDatabase(moomintrollsDatabase);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

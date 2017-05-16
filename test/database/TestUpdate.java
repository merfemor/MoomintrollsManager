package database;

import psql.MoomintrollsDatabase;
import trolls.Moomintroll;
import trolls.utils.Random;

import java.sql.SQLException;

public class TestUpdate {

    public static void main(String[] args) {
        try {
            MoomintrollsDatabase database = Databases.getTestDatabaseConnection();
            Moomintroll m = Random.randomTroll();
            System.out.println(m);
            database.update(141, m);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

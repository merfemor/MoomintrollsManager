import psql.MoomintrollsCollectionPSQLClient;
import trolls.Moomintroll;
import trolls.MoomintrollsCollection;

import java.awt.*;
import java.sql.SQLException;

public class PrintDatabase {
    public static void main(String[] args) {
        MoomintrollsCollectionPSQLClient psqlClient;
        try {
            psqlClient = new MoomintrollsCollectionPSQLClient(
                    "localhost",
                    5432,
                    "mooman",
                    "usr",
                    "123456"
            );
        } catch (SQLException e) {
            return;
        }
        MoomintrollsCollection moomintrolls;
        try {
            moomintrolls = psqlClient.getFullTable();
        } catch (SQLException e) {
            return;
        }

        for(Moomintroll moomintroll : moomintrolls) {
            System.out.println(
                    "Moomintroll [" +
                    moomintroll.getName() + ", " +
                            moomintroll.isMale() + ", " +
                            moomintroll.getRgbBodyColor().getRGB() + ", " +
                            moomintroll.getKindness().value() + ", " +
                            moomintroll.getPosition() +
                            "]"
            );
        }
    }
}
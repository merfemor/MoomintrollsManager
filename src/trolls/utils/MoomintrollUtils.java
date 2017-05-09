package trolls.utils;

import trolls.Moomintroll;

public class MoomintrollUtils {

    public static void beautifulPrint(Moomintroll moomintroll) {
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

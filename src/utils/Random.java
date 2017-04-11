package utils;

import trolls.Kindness;
import trolls.Moomintroll;
import trolls.Wight;

import java.awt.*;

public class Random {
    public static int randomInt(int from, int to) {
        return ((int)(Math.random() * Integer.MAX_VALUE) % (to - from + 1)) + from;
    }

    /**
     * Generates random moomintroll
     * @return randomized moomintroll
     */
    public static Moomintroll randomTroll() {
        Moomintroll moomintroll = new Moomintroll(
                "Random",
                Math.random() > 0.5,
                randomInt(-500, 500),
                Wight.BodyColor.white
        );
        moomintroll.setRgbBodyColor(
                new Color(randomInt(0, 255), randomInt(0, 255), randomInt(0, 255))
        );
        moomintroll.setKindness(new Kindness(randomInt(Kindness.DEVIL.value(), Kindness.ANGEL.value() - 1)));
        return moomintroll;
    }
}

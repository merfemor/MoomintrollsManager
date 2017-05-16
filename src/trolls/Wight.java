package trolls;

import java.awt.*;
import java.io.Serializable;

public abstract class Wight implements Serializable {
    public enum BodyColor {
        redAsLobster, white, lightGreen
    }
    protected String name;
    protected boolean isMale;
    protected transient BodyColor bodyColor;
    protected Color rgbBodyColor;
    protected int position;

    // занять позицию так, чтобы находиться на расстоянии не большем, чем указанное
    public abstract void moveTo(int position, int maxDistance);

    public Wight(String name, boolean isMale, int position, BodyColor bodyColor) {
        this.name = name;
        this.isMale = isMale;
        this.bodyColor = bodyColor;
        this.position = position;
        this.rgbBodyColor = Color.WHITE;
    }

    public Wight(String name) {
        this.name = name;
    }

    public void setBodyColor(BodyColor bodyColor) {
        this.bodyColor = bodyColor;
        if(this.bodyColor == BodyColor.redAsLobster)
            System.out.println(this.name + " красный(-ая), как рак");
        else if(this.bodyColor == BodyColor.white)
            System.out.println(this.name + " побелел(-а)");
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public BodyColor getBodyColor() {
        return bodyColor;
    }

    public Color getRgbBodyColor() {
        return rgbBodyColor;
    }

    public void setRgbBodyColor(Color rgbBodyColor) {
        this.rgbBodyColor = rgbBodyColor;
    }

    public boolean isMale() {
        return isMale;
    }
}
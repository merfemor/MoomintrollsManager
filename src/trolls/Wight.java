package trolls;

import ru.ifmo.cs.korm.annotations.Attribute;

import java.awt.*;
import java.io.Serializable;

public abstract class Wight implements Serializable {
    @Attribute(name = "name")
    protected String name;
    @Attribute(name = "is_male")
    protected boolean isMale;
    protected transient BodyColor bodyColor;

    @Attribute(name = "color")
    protected Color rgbBodyColor;
    @Attribute(name = "position")
    protected long position;

    public Wight(String name, boolean isMale, int position, BodyColor bodyColor) {
        this.name = name;
        this.isMale = isMale;
        this.bodyColor = bodyColor;
        this.position = position;
        this.rgbBodyColor = Color.WHITE;
    }

    public Wight() {

    }

    public Wight(String name) {
        this.name = name;
    }

    public boolean getIsMale() {
        return isMale;
    }

    public void setIsMale(boolean male) {
        isMale = male;
    }

    // занять позицию так, чтобы находиться на расстоянии не большем, чем указанное
    public abstract void moveTo(long position, int maxDistance);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public BodyColor getBodyColor() {
        return bodyColor;
    }

    public void setBodyColor(BodyColor bodyColor) {
        this.bodyColor = bodyColor;
        if (this.bodyColor == BodyColor.redAsLobster)
            System.out.println(this.name + " красный(-ая), как рак");
        else if (this.bodyColor == BodyColor.white)
            System.out.println(this.name + " побелел(-а)");
    }

    public Color getRgbBodyColor() {
        return rgbBodyColor;
    }

    public void setRgbBodyColor(Color rgbBodyColor) {
        this.rgbBodyColor = rgbBodyColor;
    }

    public enum BodyColor {
        redAsLobster, white, lightGreen
    }
}
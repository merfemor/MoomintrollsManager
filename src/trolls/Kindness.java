package trolls;

import java.io.Serializable;

public class Kindness implements Comparable<Kindness>, Serializable {
    protected int kindness;
    public transient static final Kindness DEVIL = new Kindness(-666);
    public transient static final Kindness VERY_BAD = new Kindness(-500);
    public transient static final Kindness BULLY = new Kindness(-320);
    public transient static final Kindness BAD = new Kindness(-150);
    public transient static final Kindness NORMAL = new Kindness(0);
    public transient static final Kindness GOOD = new Kindness(140);
    public transient static final Kindness GREAT = new Kindness(280);
    public transient static final Kindness BEAUTIFUL = new Kindness(420);
    public transient static final Kindness BRILLIANT = new Kindness(560);
    public transient static final Kindness ANGEL = new Kindness(700);

    public Kindness(final int kindness) {
        this.kindness = kindness;
    }

    public int value() {
        return kindness;
    }

    @Override
    public String toString() {
        if(kindness <= DEVIL.kindness) return "devil";
        else if(kindness <= VERY_BAD.kindness) return "very bad";
        else if(kindness <= BULLY.kindness) return "bully";
        else if(kindness <= BAD.kindness) return "bad";
        else if(kindness <= NORMAL.kindness) return "normal";
        else if(kindness <= GOOD.kindness) return "good";
        else if(kindness <= GREAT.kindness) return "great";
        else if(kindness <= BEAUTIFUL.kindness) return "beautiful";
        else if(kindness <= BRILLIANT.kindness) return "brilliant";
        else if(kindness <= ANGEL.kindness) return "angel";
        else return "MOST KIND!!";
    }

    @Override
    public int compareTo(Kindness kindness) {
        return this.kindness - kindness.kindness;
    }

    @Override
    public boolean equals(Object kindness) {
        return !(kindness == null || (!(kindness instanceof Kindness)))
                && this.kindness == ((Kindness) kindness).kindness;
    }
}

package trolls;

public class Kindness implements Comparable<Kindness> {
    protected int kindness;
    public static final Kindness DEVIL = new Kindness(-666);
    public static final Kindness VERY_BAD = new Kindness(-500);
    public static final Kindness BULLY = new Kindness(-320);
    public static final Kindness BAD = new Kindness(-150);
    public static final Kindness NORMAL = new Kindness(0);
    public static final Kindness GOOD = new Kindness(140);
    public static final Kindness GREAT = new Kindness(280);
    public static final Kindness BEAUTIFUL = new Kindness(420);
    public static final Kindness BRILLIANT = new Kindness(560);
    public static final Kindness ANGEL = new Kindness(700);

    public Kindness(final int kindness) {
        this.kindness = kindness;
    }

    public int value() {
        return kindness;
    }

    @Override
    public String toString() {
        if(kindness <= DEVIL.kindness) return "DEVIL IN THE FLESH!!";
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

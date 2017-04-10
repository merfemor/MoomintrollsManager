package trolls;

public class Kindness implements Comparable<Kindness> {
    protected int kindness;
    public static final Kindness DEVIL = new Kindness(-666);
    public static final Kindness VERY_BAD = new Kindness(-60);
    public static final Kindness BULLY = new Kindness(-30);
    public static final Kindness BAD = new Kindness(-10);
    public static final Kindness NORMAL = new Kindness(0);
    public static final Kindness GOOD = new Kindness(10);
    public static final Kindness GREAT = new Kindness(40);
    public static final Kindness BEAUTIFUL = new Kindness(60);
    public static final Kindness BRILLIANT = new Kindness(100);
    public static final Kindness ANGEL = new Kindness(1000);

    public Kindness(final int kindness) {
        this.kindness = kindness;
    }

    @Override
    public String toString() {
        if(kindness == DEVIL.kindness) return "DEVIL IN THE FLESH!!";
        else if(kindness == VERY_BAD.kindness) return "very bad";
        else if(kindness == BULLY.kindness) return "bully";
        else if(kindness == BAD.kindness) return "bad";
        else if(kindness == NORMAL.kindness) return "good";
        else if(kindness == GOOD.kindness) return "beautiful";
        else if(kindness == GREAT.kindness) return "great";
        else if(kindness == BEAUTIFUL.kindness) return "beautiful";
        else if(kindness == BRILLIANT.kindness) return "brilliant";
        else if(kindness == ANGEL.kindness) return "angel";
        else return (kindness > 0) ? "good" : "bad";
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

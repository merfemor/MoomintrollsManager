package trolls;

public class SelfBowException extends Exception{
    private Wight wight;
    SelfBowException(Wight wight) {
        super(wight.getName() + " не настолько одинок(-а), чтобы кланяться самому(-ой) себе");
        this.wight = wight;
    }
    public Wight getWight() {
        return wight;
    }
}
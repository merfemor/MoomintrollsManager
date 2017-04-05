package trolls;

import java.util.Collection;
import java.util.HashSet;

public class Story {
    private Collection<Moomintroll> moomintrolls = new HashSet<>();

    public void looksLike() {
        int bows = 0, movements = 0;
        for(Moomintroll moomintroll: moomintrolls){
            bows += moomintroll.actionLog.countAction(Moomintroll.Action.bow);
            movements += moomintroll.actionLog.countAction(Moomintroll.Action.movement);
        }
        if(bows > 1) {
            System.out.println("Все вместе выглядит так, будто они пришли на званый вечер");
        } else if(movements > 3) {
            System.out.println("Все вместе выглядит так, будто это соревнования по бегу");
        } else {
            System.out.println("Все вместе выглядит так, будто ничего не происходит");
        }
    }

    public void addMoomintroll(Moomintroll moomintroll) {
        moomintrolls.add(moomintroll);
    }
}
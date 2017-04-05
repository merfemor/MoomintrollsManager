package trolls;

import java.util.Set;

public interface BowTo {
    void bowTo(Wight wight) throws SelfBowException;
    void bowTo(Set<Wight> wights) throws SelfBowException;
}

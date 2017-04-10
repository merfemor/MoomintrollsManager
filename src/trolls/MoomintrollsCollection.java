package trolls;

import java.awt.*;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import static utils.Random.randomInt;

public class MoomintrollsCollection extends PriorityQueue<Moomintroll> {
    /**
     * Removes first element in moomintrolls collection
     */
    public void remove_first() {
        if (this.isEmpty()) {
            System.out.println("Nothing to remove: collection is already empty");
        } else {
            poll();
            System.out.println("First element was successfully deleted");
        }
    }

    /**
     * Adds moomintroll object into collection of moomintrolls if it is bigger than the maximum element in collection
     * @param moomintroll element to add
     */
    public void add_if_max(Moomintroll moomintroll) {
        if (moomintroll.compareTo(peek()) < 0) {
            add(moomintroll);
            System.out.println("Object successfully added");
        } else {
            System.out.println("Object was not added");
        }
    }

    /**
     * Removes all elements in moomintrolls collection that are smaller than this element
     * @param moomintroll element to check
     */
    public void remove_greater(Moomintroll moomintroll) {
        int deletedElementsNum = 0;
        while (!isEmpty() && moomintroll.compareTo(peek()) > 0) {
            poll();
            deletedElementsNum++;
        }
        System.out.println(deletedElementsNum + " elements were deleted");
    }

    /**
     * Generate random troll and add to collection
     */
    public void add_random_troll() {

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
        add(moomintroll);
    }
}
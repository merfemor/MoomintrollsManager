package trolls;

import java.util.PriorityQueue;
import java.util.Random;

public class MoomintrollsCollection {

    protected PriorityQueue <Moomintroll> moomintrolls = new PriorityQueue<>();

    /**
     * Removes first element in moomintrolls collection
     */
    public void remove_first() {
        if (moomintrolls.isEmpty()) {
            System.out.println("Nothing to remove: collection is already empty");
        } else {
            moomintrolls.poll();
            System.out.println("First element was successfully deleted");
        }
    }

    /**
     * Adds moomintroll object into collection of moomintrolls if it is bigger than the maximum element in collection
     * @param moomintroll element to add
     */
    public void add_if_max(Moomintroll moomintroll) {
        if (moomintroll.compareTo(moomintrolls.peek()) < 0) {
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
        while (!moomintrolls.isEmpty() && moomintroll.compareTo(moomintrolls.peek()) > 0) {
            moomintrolls.poll();
            deletedElementsNum++;
        }
        System.out.println(deletedElementsNum + " elements were deleted");
    }

    /**
     * Just adds moomintroll into collection
     * @param moomintroll Moomintroll to add
     */
    public void add(Moomintroll moomintroll) {
        moomintrolls.add(moomintroll);
    }

    /**
     * Generate random troll and add to collection
     */
    public void add_random_troll() {
        Random r = new Random();
        Moomintroll moomintroll = new Moomintroll(
                "Random",
                r.nextBoolean(),
                (r.nextInt(200) - 100),
                Wight.BodyColor.white
        );
        add(moomintroll);
    }

    @Override
    public String toString() {
        return moomintrolls.toString();

    }
}
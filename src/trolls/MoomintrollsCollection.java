package trolls;

import java.util.*;

@Deprecated
public class MoomintrollsCollection extends PriorityQueue<Moomintroll> {

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
}
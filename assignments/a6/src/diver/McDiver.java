package diver;

import java.util.HashSet;

import game.*;


/** This is the place for your implementation of the {@code SewerDiver}.
 */
public class McDiver implements SewerDiver {

    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        // TODO : Look for the ring and return.
        // DO NOT WRITE ALL THE CODE HERE. DO NOT MAKE THIS METHOD RECURSIVE.
        // Instead, write your method (it may be recursive) elsewhere, with a
        // good specification, and call it from this one.
        //
        // Working this way provides you with flexibility. For example, write
        // one basic method, which always works. Then, make a method that is a
        // copy of the first one and try to optimize in that second one.
        // If you don't succeed, you can always use the first one.
        //
        // Use this same process on the second method, scram.

        /****=== Note from PCHEN == **
        Seems we need to implement some logic HERE ??? Depth First Search ???
        to find the route from SeekSewer.entrance -> SeekSewer.ring
        *****/
        HashSet<Long> visited = new HashSet<>();
        traversePreOrder(state, visited);
    }

    private void traversePreOrder(SeekState state, HashSet<Long> visited){
        if (state.distanceToRing()==0) {
            return; // ring found, exit routin
        }
        visited.add(state.currentLocation());
        for ( NodeStatus neighor : state.neighbors()) {
            if (!visited.contains(neighor.getId())){
                // make sure to skip already visited Node
                state.moveTo(neighor.getId());
                traversePreOrder(state, visited);
            }
        }
    }

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
    }

}

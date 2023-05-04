package diver;

import java.util.Collection;
import java.util.HashSet;

import datastructures.MyStack;
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
        MyStack<Long> myStack = new MyStack<>();
        traverseMaze_Working(state, visited, myStack); // IT WORKS

        //traversePreOrder(state, visited);        
        //traversePreOrder(state);
    }
    /**
     * a helper method to check if All Neighors have been visited 
     * @param neighbors : Collection of 'NodeStatus'
     * @param visited : HashSet<Long> locationId
     * @return true/false
     */
    private boolean isAllNeighborVisited(Collection<NodeStatus> neighbors, HashSet<Long> visited ) {
        for(var x : neighbors) {
            if (!visited.contains(x.getId())){
                return false;
            }
        }
        return true;
    }
    
    /**
     * A recurrsive function to traverse the Maze ( game board)
     * @param state
     * @param visited
     * @param trace
     * @return
     */
    private boolean traverseMaze_Working(SeekState state, HashSet<Long> visited, MyStack<Long> trace){
        if (state.distanceToRing()==0) {
            return true;// ring found, exit routin
        }
        
        if (!isAllNeighborVisited(state.neighbors(), visited)){
            trace.push(state.currentLocation()); // push currentlocation to "trace stack"
            for ( NodeStatus neighor : state.neighbors()) {
                Long neighborId = neighor.getId();
                if (!visited.contains(neighborId)){
                    state.moveTo(neighborId);  // move to New location (neighborid)
                    visited.add(neighborId);   // log the new location(neighborId) as "Visied"
                    if (traverseMaze_Working(state, visited, trace)){
                        // exit the recursive if ring is found by moving to neighbor, otherwise keep looking
                        return true;
                    }; 
                }
            }
        }
        else { // if All its neighbors were already visited, move back
            state.moveTo(trace.pop());
            if (traverseMaze_Working(state, visited, trace)){
                return true;
            }; 
        }  
        return false;   
    }
    
    private void traversePreOrder(SeekState state){
        if (state.distanceToRing()==0) {
            return; // ring found, exit routin
        }
        long currLocationId = state.currentLocation();
        boolean bMovedToNeighbor = false;
        for ( NodeStatus neighor : state.neighbors()) {
            if (neighor.getId() != currLocationId){
                bMovedToNeighbor = true;
                state.moveTo(neighor.getId());
                traversePreOrder(state);
            }
        }
        if (bMovedToNeighbor) {
            //move back to root in no found in all neighbors
            state.moveTo(currLocationId);
        }            
    }

    private void traversePreOrder(SeekState state, HashSet<Long> visited){
        if (state.distanceToRing()==0) {
            return; // ring found, exit routin
        }
        visited.add(state.currentLocation());
        boolean bMovedToNeighbor = false;
        long currLocationId = state.currentLocation();
        for ( NodeStatus neighor : state.neighbors()) {
            if (!visited.contains(neighor.getId())){
                // make sure to skip already visited Node
                bMovedToNeighbor = true;
                state.moveTo(neighor.getId());
                traversePreOrder(state, visited);
            }
        }
        // if can't find in neighbors, step back to root
        if (bMovedToNeighbor){
           //move back to root
            state.moveTo(currLocationId);
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

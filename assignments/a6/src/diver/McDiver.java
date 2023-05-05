package diver;

import java.util.Collection;
import java.util.HashSet;
import java.util.prefs.BackingStoreException;

import datastructures.MyPQueue;
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
        HashSet<Long> visitedLocation = new HashSet<>();        
        MyStack<Long> myStack = new MyStack<>();
        MyPQueue pQueue = new MyPQueue<>();
        
        //traverseMaze_v3(state, myStack);
        traverseMaze_V2(state, visitedLocation, myStack);  // Version 2:  add a PriorityQueue to the logic, better than traverseMaze_V1() but sill not the best
        //traverseMaze_V1(state, visitedLocation, myStack); // Version 1 : IT WORKS

        //traversePreOrder(state, visited);        
        //traversePreOrder(state);
    }

    private boolean traverseMaze_v3(SeekState state, MyStack<Long> visitedStack){
        // not working yet !! 
        if (state.distanceToRing()==0) {
            return true;// ring found, exit routin
        }
        //put neighbors-to-be-visited in PriorityQueue based on distance to ring
        MyPQueue<Long> pQueue = new MyPQueue<>();
        for ( NodeStatus neighbor : state.neighbors()) {      
            double d = neighbor.getDistanceToRing();
            pQueue.add(neighbor.getId(), d);
        }
        while (!pQueue.isEmpty()) {  
            // push current location to stack         
            visitedStack.push(state.currentLocation()); 
            var nextId = pQueue.extractMin();
            state.moveTo(nextId);
            if (traverseMaze_v3(state, visitedStack)) {
                return true;
            } else {
                // if neighbors-to-be-visit is a dead search, move back to its parent step
                var backId = visitedStack.pop();
                state.moveTo(backId);
            }
        }
        return false;
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
     * Version 2: Add PriorityQueue to the logic, Works better than V1, but still not the best
     * @param state
     * @param visitedSet : set of locationId that has been 'Visited'
     * @param visitedStack : FILO stack store the current "root" of the 'neighbors' that is currently being visited
     * @return
     */
    private boolean traverseMaze_V2(SeekState state, HashSet<Long> visitedSet, MyStack<Long> visitedStack){
        if (state.distanceToRing()==0) {
            return true;// ring found, exit routin
        }

        // log the location Id(neighborId) as "Visited"
        visitedSet.add(state.currentLocation()); 

        if (!isAllNeighborVisited(state.neighbors(), visitedSet)){
            // push currentlocation to "trace stack", this is to 'Pop' the location when 'MoveBack" is needed'
            visitedStack.push(state.currentLocation()); 

            // Prioritize the neighbors to be visited according to the distance to 'ring'
            // Only add 'neighors' not been visited yet
            MyPQueue<Long> pQueue = new MyPQueue<>();
            for ( NodeStatus neighor : state.neighbors()) {                
                Long neighborId = neighor.getId();
                if (!visitedSet.contains(neighborId)){
                    double d = neighor.getDistanceToRing();
                    pQueue.add(neighborId, d);
                }
            }

            while (!pQueue.isEmpty()) {
                //var neighor = pQueue.extractMin();
                Long neighborId = pQueue.extractMin();

                state.moveTo(neighborId);  // move to New location (neighborid)
                // Since a moveTo() is made, need to add some extra weight to those distance from previous step in the pQueue
                // *** no need to *** pQueue.addPriority(2);

                // Since a moveTo is made, evaluate the "Priority" of the new neighbors-to-be-visited
                // and add those NOT been Visited yet to PriorityQueue
                for ( NodeStatus newNeighor : state.neighbors()) {   
                    if (!visitedSet.contains(newNeighor.getId())){
                        // Weight of the PriorityQueue item is the distance to Ring
                        // ** Since this distance is counted after moveTo() step, no additional step to 'Priority' needs to be added.
                        // All previously added items have already additional step added to their priority
                        double d = newNeighor.getDistanceToRing(); 
                        pQueue.add(newNeighor.getId(), d);
                    }
                }

                if (traverseMaze_V2(state, visitedSet, visitedStack)){
                    // exit the recursive if ring is found by moving to neighbor, otherwise keep looking
                    return true;
                }; 
            }
        }
        else { // if All its neighbors were already visited, move back
            state.moveTo(visitedStack.pop());
            if (traverseMaze_V2(state, visitedSet, visitedStack)){
                return true;
            }; 
        }  
        return false;   
    }
    
    /**
     * A recurrsive function to traverse the Maze ( game board)
     * @param state
     * @param visited
     * @param trace
     * @return
     */
    private boolean traverseMaze_V1(SeekState state, HashSet<Long> visited, MyStack<Long> trace){
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
                    if (traverseMaze_V1(state, visited, trace)){
                        // exit the recursive if ring is found by moving to neighbor, otherwise keep looking
                        return true;
                    }; 
                }
            }
        }
        else { // if All its neighbors were already visited, move back
            state.moveTo(trace.pop());
            if (traverseMaze_V1(state, visited, trace)){
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
        
        HashSet<Node> visitedNodes = new HashSet<>();      
        exit(state, visitedNodes);
    }
    
    
    private boolean exit(ScramState state, HashSet<Node> visitedNodes){
        var currentNode = state.currentNode();
        if (currentNode == state.exit()){
            return true;
        }
        visitedNodes.add(currentNode);

        var x = state.allNodes();
        long currLocationId = state.stepsToGo();
        for(var visitingNode : state.currentNode().getNeighbors()){
            if (!visitedNodes.contains(visitingNode)) {
                state.moveTo(visitingNode);
                if (exit(state, visitedNodes)) return true;
            }
        }
        state.moveTo(currentNode);
        return false;
    }


}

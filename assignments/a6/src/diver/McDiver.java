package diver;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.prefs.BackingStoreException;

import datastructures.MyPQueue;
import datastructures.MyQueue;
import datastructures.MyStack;
import datastructures.SlowPQueue;
import game.*;
import graph.ShortestPaths;


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

        HashSet<Long> visitedLocation = new HashSet<>();   
        // log 'Entrance'  as Visited
        visitedLocation.add(state.currentLocation());
        // get the first neighbors to start with
        Long nextNodeId = state.neighbors().iterator().next().getId();
   //     doSeek_0508(state, nextNodeId, visitedLocation);

        // entering doSeek() recursive function
        doSeek(state, nextNodeId, visitedLocation);  // Seems the best approach now
        
     //  MyStack<Long> myStack = new MyStack<>();
      //  MyPQueue pQueue = new MyPQueue<>();
        
        //traverseMaze_v3(state, myStack);
  // v2 is the best working version before doSeek() is trying
  //      traverseMaze_V2(state, visitedLocation, myStack);  // Version 2:  add a PriorityQueue to the logic, better than traverseMaze_V1() but sill not the best
  //      traverseMaze_V1(state, visitedLocation, myStack); // Version 1 : IT WORKS

        //traversePreOrder(state, visited);        
        //traversePreOrder(state);
    }

    /*  *** version 5/8 , Noted attempt to use queue to do Breadth-first-search, but not working !!! *****
    start using Breadth-first-search. breadth order is based on the Distance-to-ring()
    */
    private void doSeek_0508(SeekState state, long nextNodeId, HashSet<Long> visitedSet){		
        /* Do a breadth-first-traversal(BFS) of the 'graph' based in the distance to ring
            in order to do the "BFS", a FIFO queue is needed, so use MyQueue() calss.
            borrow the pseudocode of BFS of an graph from:
            https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
        */
        MyQueue<Long> queue = new MyQueue<>();
        
        System.err.println("Node ID = " + nextNodeId +  " pushed");

        queue.push(nextNodeId);

        while (!queue.isEmpty()){
            // de-queue the next node from queue
            var nodeId = queue.pop();
            System.err.println("Node ID = " + nodeId +  " popped");
            // only process those not-yet-be-processed
           // if (!visitedSet.contains(nodeId)){
                // log the node as "Visited"
                visitedSet.add(nodeId); 
                // Make the move
                state.moveTo(nodeId);
                if (state.distanceToRing()==0) {
                    return ;// if ring found, exit routin
                }
                /*
                 * needs to move back to root to visit other neighbors,
                 *  otherwise java.lang.IllegalArgumentException: moveTo: Node must be adjacent to position
                 */
                //state.moveTo(rootId); 
                // add neighbors-to-be-processed to PriorityQueue ordered by priority (distance to ring)
                // Closer to Ring will be en-queued first.
                MyPQueue<Long> pQueue = new MyPQueue<>();
                for ( NodeStatus neighbor : state.neighbors()) {                
                    Long neighborId = neighbor.getId();
                    if (!visitedSet.contains(neighborId)){
                        double d = neighbor.getDistanceToRing();
                         System.err.println("   Node ID = " + neighborId +  " enqueue to pQueue. distance = " + d);
                        pQueue.add(neighborId, d);
                    }
                }
                // de-queue those neighbors-to-be-processd from PriorityQueue to queue
                if (!pQueue.isEmpty()){
                    while (!pQueue.isEmpty()){
                        long x = pQueue.extractMin();
                        queue.push(x);
                        System.err.println("Node ID = " + x +  " pushed to queue from pQueue ");
                        queue.push(state.currentLocation()); // 
                        System.err.println("Node ID = " + state.currentLocation() +  " pushed to queue from pQueue ");
                    }
                    queue.pop(); 
                }
           // }

        }
    }
    /**
     * Best up to 5/6/2023, 
     * @param state
     * @param nextNodeId
     * @param visitedSet
     * @return
     */
    private boolean doSeek(SeekState state, Long nextNodeId, HashSet<Long> visitedSet){	
		// move to nextNode
		state.moveTo(nextNodeId);

        if (state.distanceToRing()==0) {
            return true;// if ring found, exit routin with 'True'
        }       
        
        /* If nextNode is already 'seeked', assuming all its neighbors were already 'Visited', 
            exit routin with 'False'. No need to evaluate its neighbors
        */
        if (visitedSet.contains(nextNodeId)) {
            return false;
        }   
        // log the "Visited" node
        visitedSet.add(nextNodeId); 

		/* create a PriorityQueue to store Node-to-be-visited from neighbors. 
        *  only those not visited "Neighbors" will be queued
        *  Priority is based on "DistanceToRing", shorter distance has higher priority
		*/
        MyPQueue<Long> pQueue = new MyPQueue<>();
		for ( NodeStatus neighor : state.neighbors()) {                
			Long neighborId = neighor.getId();
			if (!visitedSet.contains(neighborId)){
				double d = neighor.getDistanceToRing();
				pQueue.add(neighborId, d);
			}
		}
        while (!pQueue.isEmpty()) {
            // Get the shortest DistanctToRing 'neighbor' to be visited
			Long neighborId = pQueue.extractMin();
			if (doSeek(state, neighborId, visitedSet)) {
				return true; // true == Ring found, exit routin is Ring is found
            }
            state.moveTo(nextNodeId); // moved back to root to visit next child neighbor, otherwise illegal move will be thrown
		}
		return false; // not found, return false	
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
        
        /* since allNodes are known now, let's built the Maze graph from allNodes(); */
        Maze mGraph = new Maze((Set<Node>) state.allNodes());
        /* use the ShortestPaths to find the shortest path to exit first */
        ShortestPaths<Node, Edge> ssp = new ShortestPaths<>(mGraph);   
        // ssp.singleSourceDistances(state.currentNode()); 

        // set of Node to that has been visied
        HashSet<Node> visitedNodes = new HashSet<>(); 
        
        /* Ramdomly choose a neighors to start with */
        Node nextNode = state.currentNode().getNeighbors().iterator().next();
        /*works but not optimum  .. doScram2(state, nextNode, ssp, visitedNodes); */

        /*** 
        StringBuilder sb = new StringBuilder();
        sb.append("best path: \n");
        for (var edge : ssp.bestPath(state.exit())) {
            sb.append(edge.source().getId() + "-> " + edge.destination().getId() + " length = " + edge.length() + "\n");
        }

        String s = sb.toString(); ***/
        //doSSP(state, ssp.bestPath(state.exit()));

        // Step 1 : we do a depth first graph traversal untill the stepToGo() is about runout
        // /*RAW BUT WORKING*/ 
        //doScram3(state, nextNode, ssp, visitedNodes);
        
        // **Best version up to 5/5/ 
         doScram_Final(state, nextNode, ssp, visitedNodes);
        //doScram_V0508(state, nextNode, ssp, visitedNodes);

        // Step 2 : now take the shortest path to exit
        ssp.singleSourceDistances(state.currentNode()); 
        for(var edge : ssp.bestPath(state.exit())) {
            state.moveTo(edge.destination());
        }

    }    
     /**
      * V0508, based on doScram_Final (5/5) version, introduce PriorityQueue
      * @param state
      * @param nextNode (next node the doScram() will move to)
      * @param SPath
      * @param visitedNodes
      * @return True means the steps is running out
      */
    private boolean doScram_V0508(ScramState state, Node nextNode, ShortestPaths<Node, Edge> SPath, HashSet<Node> visitedNodes ){        
        // if next step happen to step onto 'Exit', then exit no matter what
        if (nextNode.equals(state.exit())){            
            state.moveTo(nextNode);
            return true;
        }
        
        // get the best path from 'nextNode' to 'exit' node 
        SPath.singleSourceDistances(nextNode); 
        var bestPath = SPath.bestPath(state.exit());
        // Calculate the total steps needs to exit from 'nextNode'
        long minStepsToExit = 0;
        for (var edge : bestPath){
            // edge.length = steps to walk between node
            minStepsToExit += edge.length;
        }
        
        // Projected Steps left == current steps left - cost (steps) to move to 'nextNode' (edge.length)     
        long stepsLeft = state.stepsToGo()- state.currentNode().getEdge(nextNode).length;

        // If projected steps left is less than the minStepsToExit, we need to get out. We don't make a move
        if (stepsLeft  < minStepsToExit){ 
            return true; 
        }   
        
        // make the move to nextNode
        // System.err.println("Node ID = " + nextNode.getId()+  " Befor .moveTo state.stepsToGo() = " + state.stepsToGo());
        System.err.println("Node " + state.currentNode().getId() + "->" + nextNode.getId()+  " state.stepsToGo() = " + state.stepsToGo() + " steps to exit = " + minStepsToExit);
        state.moveTo(nextNode);
        //System.err.println("Node ID = " + nextNode.getId()+  " After .moveTo state.stepsToGo() = " + state.stepsToGo());

        // log the node been 'Visited'
        visitedNodes.add(nextNode);

        // do DFS, going to the nearest 'Node' first (this would end up stepping into the most number of 'Node' before start exiting        
        MyPQueue<Node> pQueue = new MyPQueue<>();
        for ( Node neighbor : state.currentNode().getNeighbors()) {                 
            if (!visitedNodes.contains(neighbor) && neighbor!= state.exit()) { 
                // Only add Not Visisted neighbors, skip 'Exit' for now.
                // Prioritize the neighbor by Edge.length, shorter length (steps) would visit first
                double d = state.currentNode().getEdge(neighbor).length; 
                pQueue.add(neighbor, d);
            }
        }
        
        while (!pQueue.isEmpty()) {
            // Get the nearest 'neighbor' to be visited
			Node neighborNode = pQueue.extractMin();
            // do the Scram step to 'neighorNode'
            if (doScram_V0508(state, neighborNode, SPath, visitedNodes)){
                return true; // no more child visiting when graph traverse has reached the limited
            }  

            /* Option 1: move to parent such that another child can be visit *
            if (doScram_V0508(state, nextNode, SPath, visitedNodes)){
                return true; 
            };  */

            /* Option 2:
            *Since move to 'NeighborNode' is already made, new Neighbors to 'neighbor" are exposed
            * in stead of going back to Parent ( a waste of step)
            * check to see if there are "new neighbors' that can be visited, if so, doScram(),
            */
           
            for ( Node newNeighbor : state.currentNode().getNeighbors()) {                 
                if (!visitedNodes.contains(newNeighbor) && newNeighbor!= state.exit()) { 
                    // Only add Not Visisted neighbors, skip 'Exit' for now.
                    // Prioritize the neighbor by Edge.length, shorter length (steps) would visit first
                    if (doScram_V0508(state, newNeighbor, SPath, visitedNodes)){
                        return true; 
                    };
                }
            }
            // Since neighbor's neighbor could have been visited, do the shortest path backout to "Root" such that the next neighbor can be visit.
            SPath.singleSourceDistances(state.currentNode()); 
            var pathToRoot = SPath.bestPath(nextNode); 
            for(var edge : pathToRoot) {
                if (pathToRoot.size() > 1){
                    var x = 1;
                }
                if (doScram_V0508(state, edge.destination(), SPath, visitedNodes)){
                     return true; 
                };
            }
            /* end of option 2 */
		}
        return false;
    }

    /**
     * Currently the best version by 5/5
     * @param state
     * @param gotoNode
     * @param SPath
     * @param visitedNodes
     * @return
     */
    private boolean doScram_Final(ScramState state, Node nextNode, ShortestPaths<Node, Edge> SPath, HashSet<Node> visitedNodes ){  
        
        System.err.println("Entering doScram() :  Node ID = " + nextNode.getId());      

        if (nextNode.equals(state.exit())){
            // if happen to step onto 'Exit', then exit no matter what
            return true;
        }

        /* Logic explain : 
         doScram_Final() will do depth-first-traversal (skiping Exit node) untill steps allowed exhausted
         */        

        // get the best path from 'nextNode' to 'exit' node 
        SPath.singleSourceDistances(nextNode); 
        var bestPath = SPath.bestPath(state.exit());
        // Calculate the total steps needs to exit from 'nextNode'
        long minStepsToExit = 0;
        for (var edge : bestPath){
            // edge.length = steps to walk between node
            minStepsToExit += edge.length;
        }
        
        // Projected Steps left == current steps left - cost (steps) to move to 'nextNode' (edge.length)   
        long stepsLeft = state.stepsToGo()- state.currentNode().getEdge(nextNode).length;

        // If projected steps left is less than the minStepsToExit, we need to get out. We don't make a move
        if (stepsLeft  < minStepsToExit){ 
            return true; // get out
        }   
        
        // move to nextNode
        System.err.println("MoveTo Node ID = " + nextNode.getId());

        state.moveTo(nextNode);
        //System.err.println("Node ID = " + nextNode.getId()+  " After .moveTo state.stepsToGo() = " + state.stepsToGo());

        // if 'nextNode' already visited, no need to go over its Neighbors, just exist with 'False' 
        // just to increase performance
        if (visitedNodes.contains(nextNode)) {
            return false;
        }

        // log the node to be 'Visited'
        visitedNodes.add(nextNode);

        // do DFS, going to the nearest 'Node' first (this should end up stepping into the most 'Node' before the steps exhausted
        // Using "My Priority Queue"
        MyPQueue<Node> pQueue = new MyPQueue<>();
        // *Using provied Slow Priority Queue. It will work also.*
        // SlowPQueue<Node> pQueue = new SlowPQueue<>();
        for ( Node neighbor : state.currentNode().getNeighbors()) {                 
            if (!visitedNodes.contains(neighbor) && neighbor!= state.exit()) { 
                // Only add Not Visisted neighbors, skip 'Exit' for now.
                // Prioritize the neighbor by Edge.length, shorter length (steps) would visit first
                double d = state.currentNode().getEdge(neighbor).length; 
                pQueue.add(neighbor, d);
                System.err.println(" Node ID = " + nextNode.getId() + " en-queue neighbor " + neighbor.getId() + " distance=" + d);
            }
        }

        while (!pQueue.isEmpty()) {
            // Get the nearest 'neighbor' to be visited from PriorityQueue
			Node neighborNode = pQueue.extractMin();
            // Print info for debugging
            System.err.println(" Node ID = " + nextNode.getId() + " de-queue neighbor " + neighborNode.getId() + " and doScram()");
            // do the Scram step to 'neighorNode'
            if (doScram_Final(state, neighborNode, SPath, visitedNodes)){
                return true; // no more child visiting when graph traverse has reached the limited
            }  
            /* move back to parent such that another child can be visit, otherwise illegal move exception will be thrown * */
            System.err.println(" Node ID = " + nextNode.getId() + " move back since all neighbors are visited");
            /* force a stepback to neighborNode's root , which is 'nextNode', 
              otherwise will it will cause a illegal move exception when move to next neighborNode
            */
            if (doScram_Final(state, nextNode, SPath, visitedNodes)){
                return true; 
            }; 
        }

        // Print info for debugging
        System.err.println("Node ID = " + nextNode.getId()+  " Exit doScram() false. Steps allowed = " + state.stepsToGo());

        // when reach here, all childNodes are explored, return false,  means more explore is OK
        return false;
    }

    
    /**
     * Depth first graph traverval untill the minStepsToExist is reach 
     * @param state
     * @param gotoNode
     * @param SPath
     * @param visitedNodes
     * @return
     */
    private boolean doScram3(ScramState state, Node gotoNode, ShortestPaths<Node, Edge> SPath, HashSet<Node> visitedNodes ){
        System.err.println("Node ID = " + gotoNode.getId()+  " S Allowed = " + state.stepsToGo());
        if (gotoNode.equals(state.exit())){
            // if happen to step into Exit, then exit no matter what
            return true;
        }

        /* Logic explain : 
         * compare the stepsToGo and the ShortestPath
         * if StepsAllowed > shortestPath 
         *  do DFS
         * otherwise
         *  do ShortestPath
         */
        // get the best path to 'exit'
        SPath.singleSourceDistances(gotoNode); 
        var bestPath = SPath.bestPath(state.exit());

        String sMsg ="Node ID = " + gotoNode.getId()+  " SSP.Cont() = " + bestPath.size() + " Allowed = " + state.stepsToGo();

        System.err.println(sMsg);

        // count the total steps from the best pass
        long minStepsToExit = 0;
        for (var path : bestPath){
            minStepsToExit += path.length;
        }
        long newStepsToGo = state.stepsToGo() + state.currentNode().getEdge(gotoNode).length;

        if (newStepsToGo  <= minStepsToExit){ 
            // If the new stepsToGo will be greater than minStepsToExit, we won't get out
            // So exit the routin now. We don't make a move
            return true; 
        }   
        
        // move to gotoNode
        //System.err.println("Node ID = " + gotoNode.getId()+  " Befor .moveTo state.stepsToGo() = " + state.stepsToGo());
        state.moveTo(gotoNode);
        //System.err.println("Node ID = " + gotoNode.getId()+  " After .moveTo state.stepsToGo() = " + state.stepsToGo());

        // log the node to be 'Visited'
        visitedNodes.add(gotoNode);

        // do DFS
        // hasMovedToChild = false;
        for (var childNode : state.currentNode().getNeighbors()){
            /* If childNode has not been visited yet, skip 'Exit' since we have more 'stepsToGo()' */
            if (!visitedNodes.contains(childNode) && childNode!= state.exit()) {
                //hasMovedToChild = true;
                if (doScram3(state, childNode, SPath, visitedNodes)){
                    return true; // no more child visiting when graph traverse has reached the limited
                }
                /* move to parent such that another child can be visit */
                if (doScram3(state, gotoNode, SPath, visitedNodes)){
                    return true; 
                };
            }
        }
        // if all childNodes are explored, stepback to parentNode 
        //if (hasMovedToChild)
        //    return 
        System.err.println("Node ID = " + gotoNode.getId()+  " E Allowed = " + state.stepsToGo());
        return false;
        
        // take ShortestPath route now since bestPath to exit is less than steps allowed now
        //doSSP(state, bestPath);
        //return true;
    }

    
    /**
     * !!! Works !!! but not ideal, still have tons of wasted steps
     * Depth-first-traversal of Graph until the Graph.stepToGo() == ShortestPath to 'Exit'
     * @param state
     * @param gotoNode
     * @param SPath
     * @param visitedNodes
     * @return
     */
    private boolean doScram2(ScramState state, Node gotoNode, ShortestPaths<Node, Edge> SPath, HashSet<Node> visitedNodes ){
        // move to gotoNode
        state.moveTo(gotoNode);
        // log the node to be 'Visited'
        visitedNodes.add(gotoNode);

        if (gotoNode.equals(state.exit())){
            // if happen to step into Exit, then exit no matter what
            return true;
        }

        /* Logic explain : 
         * compare the stepsToGo and the ShortestPath
         * if StepsAllowed > shortestPath 
         *  do DFS
         * otherwise
         *  do ShortestPath
         */
        // get the best path to 'exit'
        SPath.singleSourceDistances(gotoNode); 
        var bestPath = SPath.bestPath(state.exit());

        if (bestPath.size()<state.stepsToGo()){
            // do DFS
            Boolean hasMovedToChild = false;
            for (var childNode : state.currentNode().getNeighbors()){
                /* If childNode has not been visited yet, skip 'Exit' since we have more 'stepsToGo()' */
                if (!visitedNodes.contains(childNode) && childNode!= state.exit()) {
                    hasMovedToChild = true;
                    if (doScram2(state, childNode, SPath, visitedNodes)){
                        return true;
                    }
                }
            }
            // if all childNodes are explored, stepback to parentNode 
            if (hasMovedToChild)
                doScram2(state, gotoNode, SPath, visitedNodes);
        }
        
        // take ShortestPath route now since bestPath to exit is less than steps allowed now
        doSSP(state, bestPath);
        return true;
    }

    private boolean doScram(ScramState state, ShortestPaths<Node, Edge> SPath, HashSet<Node> visitedNodes ){
        if (state.currentNode()==state.exit()){
            // if happen to step into Exit, then exit no matter what
            return true;
        }

        /* Logic explain : 
         * compare the stepsToGo and the ShortestPath
         * if StepsAllowed > shortestPath 
         *  do DFS
         * otherwise
         *  do ShortestPath
         */
        // get the best path to 'exit'
        SPath.singleSourceDistances(state.currentNode()); 
        var bestPath = SPath.bestPath(state.exit());

        if (bestPath.size()<state.stepsToGo()){
            // do DFS
            Node parentNode = state.currentNode();
            visitedNodes.add(parentNode);
            for (var nextNode : state.currentNode().getNeighbors()){
                if (!visitedNodes.contains(nextNode)) {
                    state.moveTo(nextNode);
                    //if (bestPath.size()>=state.stepsToGo()) {
                    //    break;}
                    if (doScram(state, SPath, visitedNodes)){
                        return true;
                    }
                    state.moveTo(parentNode);
                    if (bestPath.size()>=state.stepsToGo()) {
                        break;}
                    if (doScram(state, SPath, visitedNodes)){
                        return true;
                    };
                }
            }
            var i = 1;
        }
        
        // Do ShortestPath route now
        doSSP(state, bestPath);
        return true;
    }
    private void doSSP(ScramState state, Collection<Edge> SPath){
        for(var edge : SPath){
            state.moveTo(edge.destination());
        }
    }
}

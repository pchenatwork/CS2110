package diver;

import java.util.HashSet;
import java.util.Set;

import datastructures.MyPQueue;
import datastructures.MyQueue;
import datastructures.MyStack;

import game.*;
import graph.ShortestPaths;

/** This is the place for your implementation of the {@code SewerDiver}.
 */
/**
 * Version using a FIFO Queue and FILO Stack. 
 * introduced "short-cut" in Scram() !!!WORKS BETTER !! *
 */
public class McDiver implements SewerDiver {

    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        // HashSet to log the Gameboard.Node (node.id) that have been visited
        HashSet<Long> visitedNodes = new HashSet<>();
        // get next node to be visited
        Long nextNodeId = getNextSeekingNodeId(state, visitedNodes);
        doSeeking(state, nextNodeId, visitedNodes);  // Seems the best approach now
        
    }
    /**
     * Helper method that returns the next neighbor to be visited
     * @param state : Current gameboard "SeekState"
     * @param visitedSet : Node.Id that have been visited
     * @return if -1, no more 'Neighbor', otherwise the neighbor with the min(getDistanceToRing())
     */
    private long getNextSeekingNodeId(SeekState state, HashSet<Long> visitedSet){
        /* create a PriorityQueue to store Node-to-be-visited from neighbors. 
        *  Priority is based on "DistanceToRing", shorter distance has higher priority
        */
        MyPQueue<Long> pQueue = new MyPQueue<>();
        for ( NodeStatus neighor : state.neighbors()) {                
            Long neighborId = neighor.getId();
            if (!visitedSet.contains(neighborId)){
                /* only those not visited "Neighbor" will be queued */
                double d = neighor.getDistanceToRing();
                pQueue.add(neighborId, d);
            }
        }
        if (pQueue.isEmpty()){
            return -1; 
        }        
        return pQueue.extractMin();
    }
    /**
     * Given the current gameboard 'SeekState', this will make the next move to 'NextNodeId'
     * @param state : Current gameboard 'SeekState'
     * @param nextNodeId : next NodeId to move to
     * @param visitedSet : Set of Node.Id that has been visited
     * @return true : 'Ring' is found
     */
    private boolean doSeeking(SeekState state, Long nextNodeId, HashSet<Long> visitedSet){	
        
        /* A FIFO Queue stores the sequence of Node to be visited starting from 'nextNodeId' */
        MyQueue<Long> visitingQueue = new MyQueue<>();

        /* A FILO Stack to store the sequence of node that has been visited
            when the FIFO Queue (longest branch) has reached an end. diver needs to pull out 
            according to the pop sequence */
        MyStack<Long> visitedStack = new MyStack<>();

        // Log currentLocation as visited
        visitedSet.add(state.currentLocation());

        // En-queue the 'nextNodeId' to be processing
        visitingQueue.push(nextNodeId);

        // Stack used to trace the 'step-back' node sequence
        // isCleanStack means there is no un-visited node attached to the parent-node in the Stack
        while(!visitingQueue.isEmpty()){
            // stack the currentlocation;
            // used for going back when all visitingQueue are visited (branch search reach an end)
            visitedStack.push(state.currentLocation());
            // Make the move to next node from 'visitingQueue'
            Long nodeId = visitingQueue.pop();
            state.moveTo(nodeId);

            // if step on 'Ring', exit with true
            if (state.distanceToRing()==0) {
                return true;
            }         
            // log the node as "Visited"
            visitedSet.add(nodeId);

            // en-queue the next un-visited neighbor 
            var nextId = getNextSeekingNodeId(state, visitedSet);

            if (nextId > 0) {  
                //  En-queue the next node to be visited
                visitingQueue.push(nextId);
            }
        }
        // Now the visiting (branch) has come to an end, we need to back off
        while (!visitedStack.isEmpty()){
            /* during the back-off process, if an un-visited node is seen. 
            *  starts a new doSee() routin from that un-visited node
            */ 
            Long backofNodeId = visitedStack.pop();
            
            /*
            // *** Experiments a shortcut logic : *** seems working well ****
            // before make the move to backofNode, see if there are others in the Stack that is neighbor to current node
            // if so. move to that node instead for a short-cut
            // eg. current node = <0>
                backoff stack : 1->2->3->4->5->6
                if <0> is also adjacent to <6>, instead of backoff to <1> then <2> ... then <6>,
                take shortcut backing off to <6>
                This is an effort to take shortcut for more steps to explore
            *
            // ===================== Doesn't work yet ==============
            
            // if the Stack is clean, try to find a short-cut
            if (isCleanStack) {
                Boolean bShortCutFound = false;            
                System.err.println("Seek: Branch ended, step back from Node " + state.currentLocation() + " to " + backofNodeId); 
                for (var neighbor : state.neighbors()){
                    if (visitedStack.exists(neighbor.getId())){
                        bShortCutFound = true;
                        System.err.println("Another neighbor " + neighbor.getId() + " also exists in Stack.");
                        // Pop Stack until that 'n' pop out
                        // and use that 'n' as backoffNode. this is to take a short-cut
                        while(!visitedStack.isEmpty()){
                            var poppedNodeId = visitedStack.pop();
                            System.err.println("Popping Node " + poppedNodeId + " from Stack for taking short-cut");
                            if (poppedNodeId==neighbor.getId()) {
                                backofNodeId = poppedNodeId;
                                System.err.println("Step back from Node " + state.currentLocation() + " take shortcut to " + backofNodeId); 
                                break;
                            }
                        }
                    }
                    if (bShortCutFound) break;
                }                
            }
            /* **** end of finding short-cut  **** */

            state.moveTo(backofNodeId);
            var nextId = getNextSeekingNodeId(state, visitedSet);
            if (nextId > 0){
                if (doSeeking(state, nextId, visitedSet)) return true;
            }
        }
        // We shouldn't have reached here unless there is no 'Ring'
		return false; // not found, return false	
    }	

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {

        /* since allNodes are known now, let's built the Maze graph from allNodes(); */
        Maze mGraph = new Maze((Set<Node>) state.allNodes());
        /* use the "ShortestPaths" SSP Object as parameters to doScramming()
           to find the shortest path to exit */
        ShortestPaths<Node, Edge> ssp = new ShortestPaths<>(mGraph);   

        // set of Node to that has been visied
        HashSet<Node> visitedNodes = new HashSet<>(); 
        // Log currentNode() as visited
        visitedNodes.add(state.currentNode());
        
        /* Get next Node to be visited */
        Node nextNode = getNextScrammingNode(state, visitedNodes);

        // Step 1 : we do a depth first graph traversal until the state.stepToGo() is about exhausted        
        doScramming(state, nextNode, ssp, visitedNodes);
        // Step 2 : now take the shortest path to exit
        ssp.singleSourceDistances(state.currentNode()); 
        for(var edge : ssp.bestPath(state.exit())) {
            state.moveTo(edge.destination());
        }
    }  
    

    
    /**
     * Helper method that returns the next neighbor to go
     * @param state : Current gameboard "ScramState"
     * @param visitedSet : Node.Id that have been visited
     * @return if NULL, no more, otherwise the neighbor Node with the most coins
     */
    private Node getNextScrammingNode(ScramState state, HashSet<Node> visitedSet){
        /* create a PriorityQueue to store Node-to-be-visited from neighbors. 
        *  Priority is based on the coins value, higher coins value has higher priority (low priority number)
        */
        MyPQueue<Node> pQueue = new MyPQueue<>();
        // *Using provied Slow Priority Queue. It will work also.*
        // SlowPQueue<Node> pQueue = new SlowPQueue<>();
        for ( Node neighbor : state.currentNode().getNeighbors()) {    
            if (neighbor.getTile().coins()>0)    {                
                double d = neighbor.getTile().coins() * -1 ;  // Higher coins neighor has highest priority (smaller value in Priority)
                pQueue.add(neighbor, d);
            }       else  
            if (!visitedSet.contains(neighbor) && neighbor!= state.exit()) { 
                // Only add Not-Yet-Visited neighbors, excluding 'Exit' node
                // Prioritize the neighbor by Tile's coins value, more coins would visit first
                double d = state.currentNode().getEdge(neighbor).length;  // Shorter distance has higher priority to be visited
                pQueue.add(neighbor, d);
            }
        }
        if (!pQueue.isEmpty()){
            return pQueue.extractMin();
        }
        return null; // not found
    }
    /**
     * Helper to calculate if StepsToGo is exhausted after next move
     * @param state
     * @param nextNode
     * @param SPath
     * @return
     */
    private boolean isStepsExhausted(ScramState state, Node nextNode, ShortestPaths<Node, Edge> SPath){
        // get the best path from 'nextNode' to 'exit' node 
        SPath.singleSourceDistances(nextNode); 
        var bestPath = SPath.bestPath(state.exit());
        // Calculate the total steps needs to exit from 'nextNode'
        long minStepsToExit = 0;
        for (var edge : bestPath){
            // edge.length = steps to walk between nodes
            minStepsToExit += edge.length;
        }
        
        // Projected Steps left == current steps left - cost (steps) to move to 'nextNode' (edge.length)   
        long stepsLeft = state.stepsToGo()- state.currentNode().getEdge(nextNode).length;
        
        // debugging trace log
        System.err.println("  Steps to get out if moved " + minStepsToExit);    
        System.err.println("  Steps left " + state.stepsToGo() + " if moved " + stepsLeft);  

        // If projected steps left is less than the minStepsToExit, we need to get out here
        if (stepsLeft  < minStepsToExit){ 
            return true; // get out
        }  
        return false;
    }
    
    private boolean doScramming(ScramState state, Node nextNode, ShortestPaths<Node, Edge> SPath, HashSet<Node> visitedSet){	
        
        //  debugging  log 
        System.err.println("Entering doScram() : CurrentNode = " + state.currentNode().getId() + " nextNode = " + nextNode.getId());      

        if (nextNode.equals(state.exit())){
            // if happen to step onto 'Exit', then exit with true            
            return true;
        }
        
        /* A FIFO Queue stores the sequence of Nodes in order to be visited */
        MyQueue<Node> visitingQueue = new MyQueue<>();

        /* A FILO Stack to store the sequence of node that have already visited in reverse order
            when the FIFO Queue (longest branch) has reached an end. diver needs to pull out.
            The Stack will pop the sequence of Nodes divers can pull put*/
        MyStack<Node> visitedStack = new MyStack<>();

        // En-queue the 'nextNode' as first node to be processing
        visitingQueue.push(nextNode);

        while(!visitingQueue.isEmpty()){
            // Pop the next Node from visitingQueue to be processed
            nextNode = visitingQueue.pop();

            /* ** Do condition check **/
            if (isStepsExhausted(state, nextNode, SPath)){
                return true;
            }             
            /* end of condition check  */

            // debugging log
            System.err.println("Node = " + state.currentNode().getId() + " moveTo() node " + nextNode.getId());  

            /* ** We now do the actual moveTo() ** */
            // stack the currentNode;
            // used for going back when all visitingQueue are visited (branch search has reached an end) and needs to step back            
            visitedStack.push(state.currentNode());
            // log the node as "Visited"
            visitedSet.add(nextNode);
            // Make the move to next node 
            state.moveTo(nextNode);

            // en-queue the next un-visited neighbor 
            nextNode = getNextScrammingNode(state, visitedSet);
            if (nextNode != null) {
                //  En-queue the next node to be visited
                visitingQueue.push(nextNode);
            }
        }
        // Now the visiting (branch) has come to an end, we need to pull back
        while (!visitedStack.isEmpty()){
            /* during the back-off process, if an un-visited node is seen. 
            *  starts a new doScramming() routin from that un-visited node
            */ 
            Node backofNode = visitedStack.pop();

            /*
            // *** Experiments a shortcut logic : Idea explained: *** not fully tested yet ****
            // Thrown this :  java.lang.IllegalArgumentException: getEdge: Node must be a neighbor of this Node
            // before make the move to backofNode, see if there are others in the Stack that is neighbor to current node
            // if so. move to that node instead 
            // eg. current node = <0>
                backoff stack : 1->2->3->4->5->6
                if <0> is also adjence to <6>, instead of backoff to <1> then <2> ... then <6>,
                take shortcut to back off to <6>
                This is an effort to take shortcut for more steps to explore
            =================
            *** Note: works only when all Stacked nodes have no un-visited branch ***
            *** Since un-visited branch starts with a node with no coin, it is ok to skip
            =================*/
            Boolean bShortCutFound = false;            
            System.err.println("Scram : step back from Node " + state.currentNode().getId() + " to " + backofNode.getId()); 
            for (var n : state.currentNode().getNeighbors()){
                if (visitedStack.exists(n)){
                    bShortCutFound = true;
                    System.err.println("Neighbor " + n.getId() + " also exists in Stack.");
                    // Pop Stack until that 'n' pop out
                    // and use that 'n' as backoffNode. this is to take a short-cut
                    while(!visitedStack.isEmpty()){
                        var poppedNode = visitedStack.pop();
                        System.err.println("Popping Node " + poppedNode.getId() + " from Stack for skipping backoff");
                        if (poppedNode.getId()==n.getId()) {
                            backofNode = poppedNode;
                            System.err.println("Step back from Node " + state.currentNode().getId() + " will take shortcut to " + backofNode.getId()); 
                            break;
                        }
                    }
                }
                if (bShortCutFound) break;
            }
            /* **** end of experiments **** */
 
            
            // Check if backoff will exhaust the StepsToGo, If so exit the routin without making the move.
            if (isStepsExhausted(state, backofNode, SPath)){
                return true;
            }              
            // move to 'backofNode'
            state.moveTo(backofNode);        

            // after Gameboard made the backoff move, check to see if there are more un-visited Node available,
            nextNode = getNextScrammingNode(state, visitedSet);
            if (nextNode != null){
                System.err.println("Step back from Node " + state.currentNode().getId() + " found an un-visited branch " + nextNode.getId());  
                if (doScramming(state, nextNode, SPath, visitedSet)) return true;
            }
        }                
		return false; // not found, return false	
    }

    
}

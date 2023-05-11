package diver;

import java.util.HashSet;
import java.util.Set;

import datastructures.MyPQueue;
import game.*;
import graph.ShortestPaths;


/** This is the place for your implementation of the {@code SewerDiver}.
 */

 /* =======================
  * This is my final submission-ready version, with all comments
  * Version 05/11/2023
  * Changed from last version McDiver_0510,
  * better and simplier logic to skip a already 'Visited' node
  * =======================
  */

public class McDiver_0511 implements SewerDiver {

    /** See {@code SewerDriver} for specification. */
    @Override
    public void seek(SeekState state) {
        // To log the visited nodes,  1: This node been queued; 2: This node has been looked at
        HashSet<Long> visitedNodes = new HashSet<>();   
        // log 'Entrance' as  "CHECKED"
        visitedNodes.add(state.currentLocation());
        // get the first neighbor as the Next Node to start with
        Long nextNodeId = state.neighbors().iterator().next().getId();
        // entering doSeek() recursive function
        doSeek(state, nextNodeId, visitedNodes);  // Seems the best approach now        
    }

    /**
     * Execute the "seek" step
     * @param state : current gameboard "SeekState"
     * @param nextNodeId : the NodeId to be moved to
     * @param visitedSet : a Map records all the Node that have been 'Visited', and their Visited_status
     * @return True/False: Ring found
     */
    private boolean doSeek(SeekState state, Long nextNodeId, HashSet<Long> visitedSet){	
		// debugging trace log
        System.err.println("doSeek() begin : current = " + state.currentLocation() + " moveTo " + nextNodeId);
		
        // move to nextNode
        state.moveTo(nextNodeId);

        // if ring found, exit routin with 'True'
        if (state.distanceToRing()==0) {
            System.err.println("doSeek() exit : current = " + state.currentLocation() + " Ring Found");
            return true;
        }

        // log the node as "Visited" node
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
                // Queue the neighborId to PriorityQueue for processing
				pQueue.add(neighborId, d);
                // debugging trace log
                System.err.println("doSeek() : current = " + state.currentLocation() + " neighbor " + neighborId + " queued");                
			}
		}
        while (!pQueue.isEmpty()) {
            // Get the next 'neighbor' to be visited from PriorityQueue
			Long neighborId = pQueue.extractMin();
            // debugging trace log
            System.err.println("doSeek() : current = " + state.currentLocation() + " next visit " + neighborId );

            // Skip the queued 'Neighbor' if it is already 'Visited'
            if (!visitedSet.contains(neighborId)){
                
                // debugging trace log
                System.err.println("doSeek() : current = " + state.currentLocation() + " call doSeek() on " + neighborId );

                // When Neighbor has not been "CHECKED" yet
                // do the next Seek step, exit the whole recursive if True (ring found)
                if (doSeek(state, neighborId, visitedSet)) return true;

                // debugging trace log
                System.err.println("doSeek() : current = " + state.currentLocation() + " move back to " + nextNodeId );

                // Step back to "Root (nextNodeId)" for next neighbor visiting
                state.moveTo(nextNodeId);
            } else {
                // debugging trace log
                System.err.println("doSeek() : current = " + state.currentLocation() + " skiped " + neighborId + " because it is been visited" );
            }
		}
		return false; // not found, return false	
    }	

    /** See {@code SewerDriver} for specification. */
    @Override
    public void scram(ScramState state) {
        // TODO: Get out of the sewer system before the steps are used up.
        // DO NOT WRITE ALL THE CODE HERE. Instead, write your method elsewhere,
        // with a good specification, and call it from this one.
        
        /* since allNodes are known now, let's built the Maze graph from allNodes(); */
        Maze mGraph = new Maze((Set<Node>) state.allNodes());

        /* Create a 'ShortestPaths' object from the Maze graph*
            this is to be used in the method to calculate the shortest path to 'exit' */
        ShortestPaths<Node, Edge> ssp = new ShortestPaths<>(mGraph);  

        /* to LOG the nodes that have been ' Visited '
            A node's VISITED_STATUS can be "QUEUED" Or "CHECKED"
        */
        HashSet<Long> visitedNodes = new HashSet<>(); 
        // Log CurrentNode to be 'Visited'
        visitedNodes.add(state.currentNode().getId());
        
        /* Ramdomly choose a neighors to start with */
        Node nextNode = state.currentNode().getNeighbors().iterator().next();

        // Step 1 : we do a depth first graph traversal until the stepToGo() is about runout        
        doScram(state, nextNode, ssp, visitedNodes);

        // Step 2 : now take the shortest path to exit
        ssp.singleSourceDistances(state.currentNode()); 
        for(var edge : ssp.bestPath(state.exit())) {
            state.moveTo(edge.destination());
        }
    }   
    
    /**
     * Execute the "Scram" step
     * @param state : current gameboard "ScramState"
     * @param nextNode : the Node to be moved to
     * @param SPath  : ShortestPath object created from the Maze
     * @param visitedNodes : a Map records all the Node that have been 'Visited', and their Visited_status
     * @return True: When the ScramState.StepsToGo() is running out
     */
    private boolean doScram(ScramState state, Node nextNode, ShortestPaths<Node, Edge> SPath, Set<Long> visitedNodes ){  
        
        System.err.println("doScram() Start : Current = " + state.currentNode().getId() + " to Node = " + nextNode.getId());      

        if (nextNode.equals(state.exit())){
            // if happen to step onto 'Exit', then exit no matter what, 
            // This shouldn't happen, but leave it anyway
            return true;
        }

        /* Logic explain : 
         doScram() will do depth-first-traversal (skiping Exit node) until steps-allowed exhausted
         */        

        /* ** To evaluate the Steps-allowed ** */
         // get the best path from 'nextNode' to 'exit' node 
        SPath.singleSourceDistances(nextNode); 
        var bestPath = SPath.bestPath(state.exit());
        // Calculate the total steps needs to exit from 'nextNode' based on "Shortest Path"
        long minStepsToExit = 0;
        for (var edge : bestPath){
            // edge.length = steps to walk between node
            minStepsToExit += edge.length;
        }       
        
        // Projected Steps left == current steps left - cost (steps) to move to 'nextNode' (edge.length)   
        long stepsLeft = state.stepsToGo()- state.currentNode().getEdge(nextNode).length;

        // If projected steps left is less than the minStepsToExit, we need to get out. We don't make a move
        if (stepsLeft  < minStepsToExit){ 
            // debugging trace log
            System.err.println(" Steps to get out if moved " + minStepsToExit);    
            System.err.println(" Steps left " + state.stepsToGo() + " if moved " + stepsLeft);   
            System.err.println("Steps exhausted. Get Out ");   
            return true; // get out
        }   
        
        // debugging trace log
        System.err.println("MoveTo Node = " + nextNode.getId());

        // move to nextNode
        state.moveTo(nextNode);

        // log the node's status as "Visited"
        visitedNodes.add(nextNode.getId());

        // do Depth-First by visiting "neighbor" recurssively until an end
        // Using a PriorityQueue to store the 'Neighbors' to be visited, Prioritize by 'Coins'
        // Using "My Priority Queue"
        MyPQueue<Node> pQueue = new MyPQueue<>();
        // *Using provied Slow Priority Queue. It will work also.*
        // SlowPQueue<Node> pQueue = new SlowPQueue<>();
        for ( Node neighbor : state.currentNode().getNeighbors()) {  
            /* If neighbor has coins , add them according to Coins Per Step
                otherwise add them prioritized by steps
             */
            if (neighbor.getTile().coins()>0){
                double p = neighbor.getTile().coins() / state.currentNode().getEdge(neighbor).length * -1;  // Negitive higher will get pick first
                pQueue.add(neighbor, p);
            } else 
            // Only Not-Yet-Visited neighbors will be added, and skip 'Exit' node               
            if (!visitedNodes.contains(neighbor.getId()) && neighbor!= state.exit()) { 
                // Prioritize the neighbor by Coins, More Coins will get visited first, so need to convert "more" to a smaller number in priority
                //double d = neighbor.getTile().coins() * -1; // state.currentNode().getEdge(neighbor).length;                 
                
                double d = state.currentNode().getEdge(neighbor).length; // Steps to neighbor, shorter steps will have higher priority             
                pQueue.add(neighbor, d);
            }
        }

        // Now we process every 'Neighbor' log in the PriorityQueue
        while (!pQueue.isEmpty()) {
            // Get the nearest 'neighbor' to be visited from PriorityQueue
			Node neighborNode = pQueue.extractMin();
            // Print info for debugging
            System.err.println("  Neighbor ID = " + neighborNode.getId() + " is de-queued for doScram()");

            if (!visitedNodes.contains(neighborNode.getId())) {
                // do the Scram step to 'neighorNode'
                if (doScram(state, neighborNode, SPath, visitedNodes)){
                    return true; // no more child visiting when graph traverse has reached the limited
                }  
                /* force a stepback to neighbor's root , which is 'nextNode', 
                    we can not jump from one 'neighbor' right to another, otherwise a illegal move exception will be thrown
                ** Since with every move, we need to evaluate the stepsLeft and stepsToGetOut, so doScram() is called **
                */
                System.err.println(" Move back to node = " + nextNode.getId() + " since its neighbor " + neighborNode.getId() + " was just visited");
                if (doScram(state, nextNode, SPath, visitedNodes)){
                    return true; 
                }; 
            } else {
                // Print info for debugging
                System.err.println(" Neighbor ID = " + neighborNode.getId() + " is already visited, skip doScram()");
            }
        }

        // Print info for debugging
        System.err.println("doScram() End : Current = " + state.currentNode().getId() + " to Node = " + nextNode.getId());      

        // when reach here, all childNodes are explored, return false,  means more explore is OK
        return false;
    }
}

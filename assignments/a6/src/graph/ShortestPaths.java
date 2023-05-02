package graph;

import datastructures.PQueue;
import datastructures.SlowPQueue;
import game.Edge;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This object computes and remembers shortest paths through a weighted, directed graph with
 * nonnegative weights. Once shortest paths are computed from a specified source vertex, it allows
 * querying the distance to arbitrary vertices and the best paths to arbitrary destination
 * vertices.
 * <p>
 * Types Vertex and Edge are parameters, so their operations are supplied by a model object supplied
 * to the constructor.
 */
public class ShortestPaths<Vertex, Edge> {

    /**
     * The model for treating types Vertex and Edge as forming a weighted directed graph.
     */
    /*==+== *PCHEN* final keyword make 'graph' not changeable during the life-span of the object, 
     * it can only be initialized in constructor ** ==++==
     */
    private final WeightedDigraph<Vertex, Edge> graph;

    /** PCHEN Reading ** https://www.javatpoint.com/map-and-hashmap-in-java */
    /*==++== "Map" is an interface in Java used to map the key-pair values ==++== */
    /*==++== "HashMap" is a class of Java collection framework, (An implementation of Interface Map) ==++== */
    /**
     * The distance to each vertex from the source.
     */
    private Map<Vertex, Double> distances;

    /**
     * The incoming edge for the best path to each vertex from the source vertex.
     */
    private Map<Vertex, Edge> bestEdges;

    /**
     * Creates: a single-source shortest-path finder for a weighted graph.
     *
     * @param graph The model that supplies all graph operations.
     */
    public ShortestPaths(WeightedDigraph<Vertex, Edge> graph) {
        this.graph = graph;
    }

    /**
     * Effect: Computes the best paths from a given source vertex, which can then be queried using
     * bestPath().
     */
    public void singleSourceDistances(Vertex source) {
        // Implementation constraint: use Dijkstra's single-source shortest paths algorithm.
        PQueue<Vertex> frontier = new SlowPQueue<>();
        distances = new HashMap<>();
        bestEdges = new HashMap<>();
           // TODO: Complete computation of distances and best-path edges

        /* frontier[] is a priority queue that will store the node with current priority( current distance discovered from so far)
           The shortest-valued node will have the highest priority that will be poped first.
         * This is to see if further "relaxation" can be found to those "currently-shortest" node
         * If no more "relaxation" can be found to those "currently-shortest" node, it will be marked in 'found[]' queue as true.
         */
        frontier.add(source, 0); //== ini PriorityQueue with "Source" node, source->source distance == 0, so priority=0 (highest priority to be popped latter)

        /* to store the 'current distance discovered'
         * distances.get(node[6]) -> will return the current distance discoved from 'Source' to 'Node[6]'
         * at the end of the routin, 
         */
        distances.put(source, 0.0);  //== ini the distance of "source"->"source" = 0.0

        /* eg: Node [6] can have 3 'edges' that can lead to, denoted as
            edge 1: [4, 6, 20] : node[4]->node[6], weight 20
            edge 2: [5, 6, 16] : node[5]->node[6], weight 16
            edge 3: [3, 6, 37] : node[3]->node[6], weight 37
         * bestEdges[] will stored the edge (path) will go to Node[6] once node[6]'s best distance if found
         */
        bestEdges.put(source, null) ; //== Ini the Best Edge to "source" node is null

        // boolean value to indicate if the vertices (nodes)'s minimum cost is already found
        HashMap<Vertex, Boolean> isFound = new HashMap<>();
        isFound.put(source, true);  // "Node" Source's min cost is already known (==0)        
        
        while (!frontier.isEmpty()){ //== run till the PriorityQueue is empty   
            // ** Step 1: Pop the "best" vertex from PriorityQueue to work with **
            // Exam the node with MinPriority ( Priority 1 > Priority 2),
            // extractMin() operation will remove "the vertex with minPriority" from PriorityQueue 
            // to see if further "relaxation" can be found,
            Vertex fromNode = frontier.extractMin();  
            // Get all outgoing Edges that are originated from "fromNode"
            Iterable<Edge> outEdges = graph.outgoingEdges(fromNode);
            // do for each "Dest Nodes" from "fromNode"
            for (Edge edge: outEdges){
                Vertex toNode = graph.dest(edge);
                /*==  if the 'dest' node doesn't exist in those "tracking indicators" :
                (1) founded[dest]
                (2) distances[dest]
                (3) bestEdge[dest], 
                create these entries with default values
                "False (Not Found)/MaxDouble/edge"
                ==*/
                if (!isFound.containsKey(toNode))  isFound.put(toNode, false);
                if (!distances.containsKey(toNode))
                    distances.put(toNode, Double.MAX_VALUE); // assume the distance 'source'->'toNode' == Max_Value
                if (!bestEdges.containsKey(toNode))
                    bestEdges.put(toNode, edge);                    

                // Relaxation step
                if (!isFound.get(toNode) && (distances.get(fromNode) + graph.weight(edge)) < distances.get(toNode))
                {
                    distances.put(toNode, distances.get(fromNode) + graph.weight(edge));
                    bestEdges.put(toNode, edge);
                    try {
                        // try to add the node to PriorityQueue with the current distance discovered
                        frontier.add(toNode, distances.get(toNode));
                    } catch (IllegalArgumentException e){
                        // If an IllegalArgumentException is catch, 'frontier' queue already has the 'node', so update the priority
                        frontier.changePriority(toNode, distances.get(toNode));
                    }
                }
            }            
            // mark vertex `fromNode` as done so it will not get picked up again
            isFound.put(fromNode, true);
        }
    }

    /**
     * Returns: the distance from the source vertex to the given vertex. Requires: distances have
     * been computed from a source vertex, and vertex v is reachable from that vertex.
     */
    public double getDistance(Vertex v) {
        assert !distances.isEmpty() : "Must run singleSourceDistances() first";
        Double d = distances.get(v);
        assert d != null : "v not reachable from source";
        return d;
    }

    /**
     * Returns: the best path from the source vertex to a given target vertex. The path is
     * represented as a list of edges. Requires: singleSourceDistances() has already been used to
     * compute best paths, and vertex target is reachable from that source.
     */
    public List<Edge> bestPath(Vertex target) {
        assert !bestEdges.isEmpty() : "Must run singleSourceDistances() first";
        LinkedList<Edge> path = new LinkedList<>();
        Vertex v = target;
        while (true) {
            Edge e = bestEdges.get(v);
            if (e == null) {
                break; // must be the source vertex (assuming target is reachable)
            }
            path.addFirst(e);
            v = graph.source(e);
        }
        return path;
    }
}

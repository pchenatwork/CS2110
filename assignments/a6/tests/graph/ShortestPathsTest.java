package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ShortestPathsTest {
    /** The graph example from Prof. Myers's notes. There are 7 vertices labeled a-g, as
     *  described by vertices1. 
     *  Edges are specified by edges1 as triples of the form {src, dest, weight}
     *  where src and dest are the indices of the source and destination
     *  vertices in vertices1. For example, there is an edge from a to d with
     *  weight 15.
     */
    static final String[] vertices1 = { "a", "b", "c", "d", "e", "f", "g" };
    static final int[][] edges1 = {
        {0, 1, 9}, {0, 2, 14}, {0, 3, 15},
        {1, 4, 23},
        {2, 4, 17}, {2, 3, 5}, {2, 5, 30},
        {3, 5, 20}, {3, 6, 37},
        {4, 5, 3}, {4, 6, 20},
        {5, 6, 16}
    };
    static class TestGraph implements WeightedDigraph<String, int[]> {
        int[][] edges;
        String[] vertices;
        Map<String, Set<int[]>> outgoing;

        TestGraph(String[] vertices, int[][] edges) {
            this.vertices = vertices;
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (String v : vertices) {
                outgoing.put(v, new HashSet<>());
            }
            for (int[] edge : edges) {
                outgoing.get(vertices[edge[0]]).add(edge);
            }
        }
        public Iterable<int[]> outgoingEdges(String vertex) { return outgoing.get(vertex); }
        public String source(int[] edge) { return vertices[edge[0]]; }
        public String dest(int[] edge) { return vertices[edge[1]]; }
        public double weight(int[] edge) { return edge[2]; }
    }
    static TestGraph testGraph1() {
        return new TestGraph(vertices1, edges1);
    }

    @Test
    void lectureNotesTest() {
        TestGraph graph = testGraph1();
        ShortestPaths<String, int[]> ssp = new ShortestPaths<>(graph);
        /*==++== **PCHEN** study the behavior of the class "ShortestPaths()" first ==++== **
        first step is call "singleSourceDistances()", which triggers the preperation of internal Models:
        1. Map<Vertex, Double> distances
        2. Map<Vertex, Edge> bestEdges
        ssp.bestPath() and ssp.getDistance() are all based on internal models "distances" & "bestEdges"
        */
        /*==++== the method take "Vertex" as the starting node  */
        ssp.singleSourceDistances("a");  
        assertEquals(50, ssp.getDistance("g"));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (int[] e : ssp.bestPath("g")) {
            sb.append(" " + vertices1[e[0]]);
        }
        sb.append(" g");
        assertEquals("best path: a c e f g", sb.toString());
    }

    // TODO: Add 2 more tests
    /**
     * == Notes from PCHEN ==
     * myEdge is a custom POCO reprsentation of an "Edge"
     * The purpose is to test the "Generic" part of the class. aka "implements WeightedDigraph<String, myEdge>""
     */
    static class myEdge{
        private String _from, _to;
        private double _distance;
        public myEdge(String from, String to, double distance){
            _distance = distance;
            _from = from;
            _to = to;
        }
        
        public String From() { return _from ;}
        public String To() { return _to; }
        public double Distance() {return _distance;}
    }
    /**
     * myGraph is using the custom POCO myEdge to define the graph
     */
    static class myGraph implements WeightedDigraph<String, myEdge> {
        Iterable<myEdge> edges;
        String[] vertices;
        Map<String, Set<myEdge>> outgoing;

        myGraph(String[] vertices, Iterable<myEdge> edges) {
            this.vertices = vertices;
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (String v : vertices) {
                outgoing.put(v, new HashSet<>());
            }
            for (myEdge edge : edges) {
                outgoing.get(edge.From()).add(edge);
            }
        }
        public Iterable<myEdge> outgoingEdges(String vertex) { return outgoing.get(vertex); }
        public String source(myEdge edge) { return edge.From(); }
        public String dest(myEdge edge) { return edge.To(); }
        public double weight(myEdge edge) { return edge.Distance(); }
    }    
    static myGraph myGraph1() {
         myEdge[] myEdges = {
            new myEdge("a", "b", 9.1), new myEdge("a", "c", 14.1), new myEdge("a", "d", 15.1),
            new myEdge("b", "e", 23.2), 
            new myEdge("c", "e", 17.3), new myEdge("c", "d", 5.3), new myEdge("c", "f", 30.3),
            new myEdge("d", "f", 20.4), new myEdge("d", "g", 37.4),
            new myEdge("e", "f", 3.5), new myEdge("e", "g", 20.5), 
            new myEdge("f", "g", 16.6)
        };
        return new myGraph(vertices1, Arrays.asList(myEdges));
    };
    
    @Test
    /**
     * myTest1 is using a custome graph (custom defined POCO edges using the same testing values)
     */
    void myTest1() { 
        myGraph graph = myGraph1();
        ShortestPaths<String, myEdge> ssp = new ShortestPaths<>(graph);      
        ssp.singleSourceDistances("a");  
        DecimalFormat f = new DecimalFormat("##.000");        
        assertEquals(f.format(51.5), f.format(ssp.getDistance("g")));
        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (myEdge e : ssp.bestPath("g")) {
            sb.append(" " + e.From());
        }
        sb.append(" g");
        assertEquals("best path: a c e f g", sb.toString());

        sb.setLength(0); // reset the stringbuilder
        for (myEdge e : ssp.bestPath("g")) {
            sb.append(e.From() + " -> " + e.To() + " : " + e.Distance() + "\n");
        }
        System.out.printf(sb.toString()); // To print out the path+weight in Debug Console to visualize the values
    }
}

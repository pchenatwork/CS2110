package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
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
     * Custom POCO of an "Edge"
     * Created to test the "Generic" portion of "WeightedDigraph{@code <Vertice, Edge>}"
     * An 'myEdge' is defined as
     * (1) From (a Vertice)
     * (2) To (a Vertice)
     * (3) Weight
     * @param <V> Vertice object
     */
    static class myEdge<V>{
        private V _from, _to;
        private double _weight;
        public myEdge(V from, V to, double weight){
            _weight = weight;
            _from = from;
            _to = to;
        }
        
        public V From() { return _from ;}
        public V To() { return _to; }
        public double Weight() {return _weight;}
    }
    static class myNode{
        private final String _name;
        public myNode(String name){
            _name = name;
        };
        @Override
        public String toString() {
            return _name;
        }
    }
    /**
     * myGraph is using the custom POCO myEdge to define the graph
     * This works also, but the V(Vertice) is limited to "String" type
     *
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
        public double weight(myEdge edge) { return edge.weight(); }
    }   **/

    /**
     * myGraph is using the custom POCO myEdge to define the graph
     * It only needs a list of 'myEdges' to define a graph.
     * each 'myEdges' has 
     * 1. From()
     * 2. To()
     * 3. Weight()
     * @param <V> Vertice can be any object
     */
    static class myGraph<V> implements WeightedDigraph<V, myEdge<V>> {
        Iterable<myEdge<V>> edges;
        String[] vertices;
        Map<V, Set<myEdge<V>>> outgoing;

        myGraph(Iterable<myEdge<V>> edges) {
            this.edges = edges;
            this.outgoing = new HashMap<>();
            for (myEdge<V> edge : edges){
                // use outgoing[] serves two purposes
                // 1. all vertices and
                // 2. its collection of outgoing 'Edges'
                // Key is the vertice 
                // value is a Map<myEdge>
                if (!outgoing.containsKey(edge.From())) {
                    outgoing.put(edge.From(), new HashSet<>());
                }
                if (!outgoing.containsKey(edge.To())) {
                    outgoing.put(edge.To(), new HashSet<>());
                }
                outgoing.get(edge.From()).add(edge);
            }
        }
        public Iterable<myEdge<V>> outgoingEdges(V vertex) { return outgoing.get(vertex); }
        public V source(myEdge<V> edge) { return edge.From(); }
        public V dest(myEdge<V> edge) { return edge.To(); }
        public double weight(myEdge<V> edge) { return edge.Weight(); }
    } ;
    
    @Test
    /**
     * * Testing on the generic portion of the Graph.
     * "Vertice" is just a String.
     */
    void mySspTestUsingGenericObject1() { 
        //Step 1: defined all the Nodes, each "Vertice" is just a String
        var a = "a";
        var b = "b";
        var c = "c";
        var d = "d";
        var e = "e";
        var f = "f";
        var g = "g";
        //step 2: defined all the Edges 
        List<myEdge<String>> edges = new ArrayList<myEdge<String>>();
        edges.add(new myEdge<>(a, b, 9.1));
        edges.add(new myEdge<>(a, c, 14.1));
        edges.add(new myEdge<>(a, d, 15.1));
        edges.add(new myEdge<>(b, e, 23.2));
        edges.add(new myEdge<>(c, e, 17.3));
        edges.add(new myEdge<>(c, d, 5.3));
        edges.add(new myEdge<>(c, f, 30.3));
        edges.add(new myEdge<>(d, f, 20.4));
        edges.add(new myEdge<>(d, g, 37.4));
        edges.add(new myEdge<>(e, f, 3.5));
        edges.add(new myEdge<>(e, g, 20.5));
        edges.add(new myEdge<>(f, g, 16.6));

        //step 3: Create 'myGraph' object from the edges defined above
        var graph = new myGraph<>(edges);
        ShortestPaths<String, myEdge<String>> ssp = new ShortestPaths<String, myEdge<String>>(graph);   
        
        ssp.singleSourceDistances(a);  // myNode 'a' is the 

        DecimalFormat fm = new DecimalFormat("##.000");        
        assertEquals(fm.format(51.5), fm.format(ssp.getDistance(g)));

        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (var edge : ssp.bestPath(g)) {
            sb.append(" " + edge.From());
        }
        sb.append(" " + g.toString());
        assertEquals("best path: a c e f g", sb.toString());

        sb.setLength(0); // reset the stringbuilder
        for (var edge : ssp.bestPath(g)) {
            sb.append(edge.From() + " -> " + edge.To() + " : " + edge.Weight() + "\n");
        }
        System.out.printf(sb.toString()); // To print out the path+weight in Debug Console to visualize the values
    }
    
    @Test
    /**
     * myTest1 is using a custome graph (custom defined POCO edges using the same testing values)
     * Testing on the generic portion of the Graph.
     */
    void myTestUsingGenericObject1() { 
        //Step 1: defined all the Nodes, now each 'Vertice' is from custom object (myNode)
        var a = new myNode("a");
        var b = new myNode("b");
        var c = new myNode("c");
        var d = new myNode("d");
        var e = new myNode("e");
        var f = new myNode("f");
        var g = new myNode("g");
        //step 2: defined all the Edges 
        List<myEdge<myNode>> edges = new ArrayList<myEdge<myNode>>();
        edges.add(new myEdge<>(a, b, 9.1));
        edges.add(new myEdge<>(a, c, 14.1));
        edges.add(new myEdge<>(a, d, 15.1));
        edges.add(new myEdge<>(b, e, 23.2));
        edges.add(new myEdge<>(c, e, 17.3));
        edges.add(new myEdge<>(c, d, 5.3));
        edges.add(new myEdge<>(c, f, 30.3));
        edges.add(new myEdge<>(d, f, 20.4));
        edges.add(new myEdge<>(d, g, 37.4));
        edges.add(new myEdge<>(e, f, 3.5));
        edges.add(new myEdge<>(e, g, 20.5));
        edges.add(new myEdge<>(f, g, 16.6));
        
        edges.add(new myEdge<>(b, a, 9));
        edges.add(new myEdge<>(e, c, 9));
        edges.add(new myEdge<>(f, c, 9));
       // edges.add(new myEdge<>(a, e, 30));

        //step 3: Create 'myGraph' object from the edges defined above
        var graph = new myGraph<>(edges);
        ShortestPaths<myNode, myEdge<myNode>> ssp = new ShortestPaths<myNode, myEdge<myNode>>(graph);   
        
        ssp.singleSourceDistances(a);  // myNode 'a' is the 

        DecimalFormat fm = new DecimalFormat("##.000");        
        assertEquals(fm.format(51.5), fm.format(ssp.getDistance(g)));

        StringBuilder sb = new StringBuilder();
        sb.append("best path:");
        for (myEdge<myNode> edge : ssp.bestPath(g)) {
            sb.append(" " + edge.From());
        }
        sb.append(" " + g.toString());
        assertEquals("best path: a c e f g", sb.toString());

        sb.setLength(0); // reset the stringbuilder
        for (myEdge<myNode> edge : ssp.bestPath(g)) {
            sb.append(edge.From() + " -> " + edge.To() + " : " + edge.Weight() + "\n");
        }
        System.out.printf(sb.toString()); // To print out the path+weight in Debug Console to visualize the values
    }
    
}

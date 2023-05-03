import java.util.*;
// https://www.techiedelight.com/single-source-shortest-paths-dijkstras-algorithm/

// A class to store a graph edge
class Edge
{
    int source, dest, weight;
 
    public Edge(int source, int dest, int weight)
    {
        this.source = source;
        this.dest = dest;
        this.weight = weight;
    }
}
 
// A class to store a heap node
class Node
{
    int vertex, weight;
 
    public Node(int vertex, int weight)
    {
        this.vertex = vertex;
        this.weight = weight;
    }
}
 
// A class to represent a graph object
class Graph
{
    // A list of lists to represent an adjacency list
    List<List<Edge>> adjList = null;
 
    // Constructor
    Graph(List<Edge> edges, int n)
    {
        adjList = new ArrayList<>();
 
        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
 
        // add edges to the directed graph
        for (Edge edge: edges) {
            adjList.get(edge.source).add(edge);
        }
    }
}
class Graph2 {
    private int V;                              //number of nodes
 
    private LinkedList<Integer> adj[];              //adjacency list
 
    public Graph2(int v)
    {
        V = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; ++i)
        {
          adj[i] = new LinkedList();
    	}
    }
 
    void addEdge(int v, int w)
    {
        adj[v].add(w);                              //adding an edge to the adjacency list (edges are bidirectional in this example)
    }
 

    void DFSUtil(int vertex, boolean nodes[])
    {

        nodes[vertex] = true;                         //mark the node as explored
        System.out.print(vertex + " ");
        int a = 0;
 
        for (int i = 0; i < adj[vertex].size(); i++)  //iterate through the linked list and then propagate to the next few nodes
            {
                a = adj[vertex].get(i);
                if (!nodes[a])                    //only propagate to next nodes which haven't been explored
                {
                    DFSUtil(a, nodes);
                }
            }  
    }

    void DFS(int v)
    {
        boolean already[] = new boolean[V];             //initialize a new boolean array to store the details of explored nodes
        DFSUtil(v, already);
    }
}
 
class Main
{
    private static void getRoute(int[] prev, int i, List<Integer> route)
    {
        if (i >= 0)
        {
            getRoute(prev, prev[i], route);
            route.add(i);
        }
    }
 
    // Run Dijkstra’s algorithm on a given graph
    public static void findShortestPaths(Graph graph, int source, int n)
    {
        // create a min-heap and push source node having distance 0
        PriorityQueue<Node> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(node -> node.weight));
        minHeap.add(new Node(source, 0));
 
        // set initial distance from the source to `v` as infinity
        List<Integer> dist;
        //-- https://www.geeksforgeeks.org/collections-ncopies-java/
        //-- dist = [[Integer.MAX_VALUE], [Integer.MAX_VALUE]. ..n.., [Integer.MAX_VALUE]]]
        dist = new ArrayList<>(Collections.nCopies(n, Integer.MAX_VALUE));
 
        // distance from the source to itself is zero
        dist.set(source, 0); //-- element to be stored at the specified position
 
        // boolean array to track vertices for which minimum
        // cost is already found
        boolean[] done = new boolean[n];
        done[source] = true;
 
        // stores predecessor of a vertex (to a print path)
        int[] prev = new int[n];
        //-- prev = [[-1],[-1],[-1],.n..,[-1]]
        prev[source] = -1;
 
        // run till min-heap is empty
        while (!minHeap.isEmpty())
        {
            // Remove and return the best vertex
            Node node = minHeap.poll();
 
            // get the vertex number
            int fromNode = node.vertex;
 
            // do for each neighbor `v` of `fromNode`
            for (Edge edge: graph.adjList.get(fromNode))
            {
                int toNode = edge.dest;
                int weight = edge.weight;
 
                // Relaxation step
                if (!done[toNode] && (dist.get(fromNode) + weight) < dist.get(toNode))
                {
                    dist.set(toNode, dist.get(fromNode) + weight);
                    prev[toNode] = fromNode;
                    minHeap.add(new Node(toNode, dist.get(toNode)));
                }
            }
 
            // mark vertex `fromNode` as done so it will not get picked up again
            done[fromNode] = true;
        }
 
        List<Integer> route = new ArrayList<>();
 
        for (int i = 0; i < n; i++)
        {
            if (i != source && dist.get(i) != Integer.MAX_VALUE)
            {
                getRoute(prev, i, route);
                System.out.printf("Path (%d —> %d): Minimum cost = %d, Route = %s\n",
                                source, i, dist.get(i), route);
                route.clear();
            }
        }
    }
 
    public static void main(String[] args)
    { 
       // SSP();
       DFS();
    }
    public static void SSP(){

        // initialize edges as per the above diagram
        // (u, v, w) represent edge from vertex `u` to vertex `v` having weight `w`
        List<Edge> edges = Arrays.asList(
                new Edge(0, 1, 10), new Edge(0, 4, 3), new Edge(1, 2, 2),
                new Edge(1, 4, 4), new Edge(2, 3, 9), new Edge(3, 2, 7),
                new Edge(4, 1, 1), new Edge(4, 2, 8), new Edge(4, 3, 2)
        );
 
        // total number of nodes in the graph (labelled from 0 to 4)
        int n = 5;
 
        // construct graph
        Graph graph = new Graph(edges, n);
 
        // run the Dijkstra’s algorithm from every node
        for (int source = 0; source < n; source++) {
            findShortestPaths(graph, source, n);
        }
    }

    public static void DFS(){
        
        Graph2 g = new Graph2(6);

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 0);
        g.addEdge(1, 3);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(3, 5);
        g.addEdge(4, 3);
        g.addEdge(5, 3);

        System.out.println(
            "Following is Depth First Traversal: ");

        g.DFS(0);
    }
}
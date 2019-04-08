import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/*
 * This program works with Kruskal's Algorithm to find the Minimal
 * Spanning Tree of various graphs.
 */
public class MainTwo {

	public static void main(String[] args) {
		MainTwo main = new MainTwo();
		File file = null;
		if (args.length == 0) {
			file = new File("weighted-graph.txt"); //default graph file here.
		}
		else {
			file = new File(args[0]); //can also feed your own graphs in.
		}
		Scanner in = null;
		try {
			in = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("file supplied not found in project path, continuing anyways");
		}
		if (in != null) {
			String line = in.nextLine();
			int vcount = Integer.parseInt(line.split(" ")[0]);
			int ecount = Integer.parseInt(line.split(" ")[1]);
			Graph G = new Graph(vcount);

			while (true) { //the rest of the file is the edges
				line = in.nextLine();
				if (line.equals("")) {
					break;
				}
				String[] parts = line.split(" ");
				int from = Integer.parseInt(parts[0]);
				int to = Integer.parseInt(parts[1]);
				double weight = Double.parseDouble(parts[2]);
				G.addEdge(from, to, weight);
			}

			System.out.println("For provided graph, MST weight is " + main.getMstWeightKruskal(G));
			
			Graph graph = MainTwo.randomWeightedGraph(100, 300, 10, 1);
			System.out.println("Random Graph #1 MST weight is " + main.getMstWeightKruskal(graph));
			graph = MainTwo.randomWeightedGraph(50000, 300000, 100, 8);
			
			long start = System.nanoTime();
			System.out.println("Random Graph #2 MST weight is " + main.getMstWeightKruskal(graph));
			long end = System.nanoTime();
			double tot = (end - start)/(Math.pow(10, 9));
			System.out.println("Time to compute 3rd weight: " + tot);
			graph = MainTwo.randomWeightedGraph(300000, 5000000, 1000, 2);
			start = System.nanoTime();
			System.out.println("Random Graph #3 MST weight is " + main.getMstWeightKruskal(graph));
			end = System.nanoTime();
			tot = (end - start)/(Math.pow(10, 9));
			System.out.println("Time to compute last weight: " + tot);
			
		}
	}

	/**
	 * Given a graph, this method returns the total weight of the determined MST.
	 * This method employs Kruskal's Algorithm.
	 * @param G
	 * 		graph to inspect.
	 * @return
	 * 		double representing total weight
	 */
	public double getMstWeightKruskal(Graph G) {
		Graph.Edge[] edges = G.getAllEdges();
		int[] counts = new int[G.getVertexCount()];
		Graph T = new Graph(G.getEdgeCount());
		for (int i = 0; i < counts.length; i++) {
			counts[i] = 1;
			T.setParent(i, -1);
		}
		Arrays.sort(edges);
		double tw = 0; //total weight is equal to zero	
		for (Graph.Edge edge : edges) {
			if (FIND(edge.from,T) != FIND(edge.to,T)) {
				T.addEdge(edge.from, edge.to);
				UNION(edge.from,edge.to,counts,T);
				tw += edge.weight;
				if (T.getEdgeCount() >= T.getVertexCount() - 1) {
					return tw;
				}
			}
		}
		//if we reach this point, our original graph was not fully connected.
		return tw;
	}
	
	/**
	 * Given vertices v and w, this method joins 
	 * the two components containing these vertices.
	 * @param v
	 * 		vertex #1
	 * @param w
	 * 		vertex #2
	 */
	public void UNION(int v, int w, int[] counts, Graph g) {
		int root1 = FIND(v,g);
		int root2 = FIND(w,g);
		if (root1 != root2) { //if this is true, want to connect them.
			
			//enter here if root1's count is larger than root2
			if (counts[root1] > counts[root2]) {
				g.setParent(root2, root1);
				counts[root1] += counts[root2];
			}
			else { //if counts[root1] is greater or they're equal, we do this
				g.setParent(root1, root2);
				counts[root2] += counts[root1];
			}
		}
	}
	/**
	 * This method finds the root vertex of the tree
	 * containing v.
	 * @param v
	 * 		the vertex parameter
	 * @param T
	 * 		graph we're working on 
	 */
	public int FIND(int v, Graph T) {
		int parent = T.getParent(v);
		while (parent != -1) {
			v = T.getParent(v);
			parent = T.getParent(v);
		}
		return v;
	}

	public static Graph randomWeightedGraph(int v, int e, int maxWeight, long seed) {
		Random rand = new Random(seed);
		Graph g = new Graph(v);
		while (g.getEdgeCount() < e) {
			int x = rand.nextInt(v);
			int y = rand.nextInt(v);
			if (x != y && ! g.edgeExists(x, y))
				g.addEdge(x, y, 1 + rand.nextInt(maxWeight));
		}
		return g;
	}
}

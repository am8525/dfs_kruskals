

/*
 * This program works with Depth First Traversal to determine maximum
 * connected component sizes of various graphs.                                                                                                          
 * 
 * for an N-vertex graph, generate 100 graphs (randomly seeded) for that given number of edges. If we're not
 * confident with our number, increase number of edges and try again.
 * 
 * theory: for an N-vertex graph to be fully connected,
 * it takes an average of N*lgN edges.
 * 
 * For example, for a 10,000 vertex graph,
 * we would theoretically need ~130,000 edges to form 
 * a fully connected graph.
 * 
 * When I ran my program a couple of times, I noticed 
 * that the maximum connected component size was 
 * consistently 90,000 or slightly more. This value is not 
 * quite N*lgN, but given that 90,000 was achieved
 * from 5+ runs, this is a consistent answer.
 */
public class MainOne {
	public static void main(String[] args) {
		Graph graph1 = MainOne.randomGraph(100, 100, 1);
		System.out.println("Graph #1 largest component size: " + getLargestCompSize(graph1));
		Graph graph2 = MainOne.randomGraph(250000, 300000, 1);
		System.out.println("Graph #2 largest component size: " + getLargestCompSize(graph2) + "\n..\n..\n..\n\n");
		
		System.out.println("Let us investigate how the maximum connected component size\n depends on the number of edges in a graph.");
		
		/*
		 * Now, we would like to explore the following question:
		 * 
		 * For a random graph with some given number of vertices, 
		 * N, how does the size of the maximum connected component
		 * depend on the number of edges in the graph?
		 * 
		 * In particular, we will look at randomly seeded graphs 
		 * with 10,000 vertices and 1000 , 2000 , ... , 20000 edges.
		 * To investigate this question, 100 randomly seeded
		 * graphs will be generated, and we will average over their 
		 * maximum connected component sizes.
		 */
		System.out.println("For a 10,000 vertex graph, what are the average maximum connected component sizes");
		System.out.println("with 1000, 2000, 3000, ... , 20000 edges? \n\nLet's see!");
		for (int edgeCount = 1000; edgeCount <= 20000; edgeCount = edgeCount + 1000) {
			//System.out.println("BEGINNING TEST OF EDGECOUNT = "+edgeCount);
			int total = 0;
			int seed = 1;
			Graph graph3 = null;
			for (int rep = 0; rep <= 100; rep++, seed++) {
				graph3 = MainOne.randomGraph(10000, edgeCount, seed);
				total = total + MainOne.getLargestCompSize(graph3);
				//System.out.println("TOTAL: "+total);
			}
			double avrg = total / 100;
			System.out.println(edgeCount+" edges, average maximum connected component size: "+avrg);

		}
		System.out.println("\n\nThe last question we shall investigate:\n\n For a random graph with a "
				+ "given number of vertices, N,\nhow many edges does the graph need for it to be almos"
				+ "t certain\nthat the graph has only one connected component?\n\n --Let's find out!\n\n");
		
		/*
		 * we will investigate the question the following way:
		 * first for a graph of N vertices, find the largest
		 * connected component size of the graph generated with
		 * N vertices and 2N edges, continually incrementing the
		 * edge count by 1000 until we have a graph that consists
		 * of only one component. To be sure this edge count is 
		 * legitimate, we then generate 100 randomly seeded graphs 
		 * of that edge count; if the average maximum connected
		 * component size is less than 99.99% of the graph vertex count,
		 * increment the edge count and try again. Rinse and repeat until
		 * an edge count is found where the average maximum connected 
		 * component size of 100 randomly seeded graphs is equal to the
		 * vertex count of the graph. 
		 * 
		 * In other words, we consider an n-vertex graph to be "fully
		 * connected" with m edges iff, for 100 uniquely seeded graphs, 
		 * each graph's largest connected component size comprises of the
		 * entire graph. This is our test.
		 */
		int seed = 1;
		int maxcompsize = -1;
		int vCount = 10000;
		int eCount = 20000;	
		Graph graph4 = null;
		while (true) {
			
			
			graph4 = MainOne.randomGraph(vCount, eCount, seed);
			maxcompsize = MainOne.getLargestCompSize(graph4);
			//System.out.println("checking edge count of "+eCount);
			/*
			 * if we have a graph that consists of a single connected component,
			 * we must now verify that edge count against 100 different seeds:
			 * does the result hold?
			 */
			if (maxcompsize == graph4.getVertexCount()) {
				seed++;
				double tot = 0.0;
				//System.out.println("beginning loop of edgeCount "+eCount);
				for (int rep = 0; rep < 100; rep++, seed++) {
					graph4 = MainOne.randomGraph(vCount, eCount, seed);
					maxcompsize = MainOne.getLargestCompSize(graph4);
					tot = tot + maxcompsize;
				} //have total, now divide by 100.
				double avrg = tot / 100;
				if (avrg == vCount) { //avrg == vCount when all 100 are fully connected.
					System.out.println("Success! For a "+vCount+"-vertex graph,\n"+
							"it takes "+eCount+" edges to be almost certain for any\n"+
							"graph to be fully connected.");	
					break;
				}
				else {
					System.out.println("Not enough certainty for edge count of "+eCount+",\n"+
				"\n..trying again.");
					eCount = eCount + 1000;
					seed = 1;
				}
			}
			else {
				eCount = eCount + 1000;
			}
		} //end while loop
		
	}

	/**
	 * This method get the largest component size in Graph g.
	 * @param g
	 * 		Graph we're inspecting
	 * @return
	 * 		size of the largest component in the graph
	 */
	public static int getLargestCompSize(Graph g) {
		return BFS.getLargestCompSize(g);
	}
	/**
	 * Creates a random undirected graph with specified numbers of vertices
	 * and edges, using a pseudo-random number generator initialized with a
	 * given seed.
	 */
	private static Graph randomGraph( int vertexCt, int edgeCt, int seed ) {
		Random rand = new Random(seed);
		Graph grph;
		grph = new Graph(vertexCt);
		while (grph.getEdgeCount() < edgeCt) {
			int a = rand.nextInt(vertexCt);
			int b = rand.nextInt(vertexCt);
			if ( a != b && ! grph.edgeExists(a, b) ) {
				grph.addEdge(a, b);
			}
		}
		return grph;
	}
}

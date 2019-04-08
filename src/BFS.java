import java.util.concurrent.LinkedBlockingQueue;


/*
 * this is a class pertaining to depth first traversal.
 * In this context, we are using the traversal to find and return
 * the size of the largest connected component in the graph g.
 */
public class BFS {
	protected static Graph g_;
	
	protected static int[] count; //tracks the size of each tree. 
	//only care about the root's count.
	
	protected static int[] parent; //array of parent pointers
	
	/**
	 * computes and returns the largest connected component size in the Graph g
	 * @param g
	 * 		graph to inspect
	 * @return
	 * 		size of largest connected component
	 */
	public static int getLargestCompSize(Graph g) {
		count = new int[g.getVertexCount()];
		for (int i = 0; i < count.length; i++) {
			count[i] = 0;
		}
		parent = new int[g.getVertexCount()];
		
		g.markAll(Graph.UNDISCOVERED);
		g.clearParents();

		/*
		 * because we're working with random graphs, there is a good chance
		 * that our graph is not fully connected. That being said, there is
		 * a good chance that our start vertex begins in a small, outside
		 * component. 
		 * 
		 * To account for this edge case, we perform a linear traversal over
		 * the vertices; if/once we find a vertex that
		 * is undiscovered, begin BFS from there.
		 */
		
		for (int i = 0; i < g.getVertexCount(); i++) {
			//System.out.println("at vertex "+i);
			/*
			 * enter here if we find an undiscovered vertex.
			 */
			if (g.getMark(i) == Graph.UNDISCOVERED) { 
				BFS.traverse(g, i); //begin BFS traversal from this vertex.
			}
		}
		
		/*
		 * determine the largest count, and return it.
		 */
		int largest = -1;
		for (int i = 0; i < count.length; i++) {
			if (count[i] > largest) {
				largest = count[i];
			}
		}
		return largest;
	}
	
	/**
	 * This method begins a BFS traversal of g, beginning from v.
	 * @param g
	 * 		our graph
	 * @param v
	 * 		beginning vertex.
	 */
	public static void traverse(Graph g, int v) {
		LinkedBlockingQueue<Integer> q = new LinkedBlockingQueue<Integer>();
		q.add(v);
		parent[v] = -1;
		count[v]++;
		g.mark(v, Graph.DISCOVERED);
		while(!q.isEmpty()) {

			try {
				v = q.take();
			} catch (InterruptedException e) {
				System.out.println("Queue is empty! Breaking loop.");
				break;
			}
			/*
			 * parses through all outgoing edges from our current vertex, v.
			 */
			for (Graph.Edge e : g.edgeList(v)) {
				if (g.getMark(e.to) == Graph.UNDISCOVERED) { 
					//System.out.println("discovered vertex "+e.to);
					parent[e.to] = e.from;
					g.mark(e.to, Graph.DISCOVERED);
					q.add(e.to);
					incrementRoot(g, v);
					
				}
			}
		}
	}
	
	/**
	 * This method is used to increment the count of the root 
	 * vertex in a given tree.
	 * @param g
	 * 		Our graph
	 * @param v
	 * 		vertex whose root we're incrementing
	 */
	public static void incrementRoot(Graph g, int v) {
		int temp = parent[v];
		while (temp != -1) {
			v = parent[v];
			temp = parent[v];
		}
		count[v]++;
		//System.out.println("COUNT UPDATE: "+count[v]);
	}
}

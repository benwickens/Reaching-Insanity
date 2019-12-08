import java.util.Iterator;
import java.util.LinkedList;

class Graph {

	private int V; // No. of vertices
	private LinkedList<Integer> adj[]; // Adjacency Lists
	private boolean[] visited;
	private int[] parent;
	private boolean foundPlayer;
	private int nextNode;
	
	// Constructor
	public Graph(int v) {
		V = v;
		adj = new LinkedList[v];
		for (int i = 0; i < v; ++i) {
			adj[i] = new LinkedList();
		}
	}

	// Function to add an edge into the graph
	public void addEdge(int v, int w) {
		adj[v].add(w);
	}

	// prints BFS traversal from a given source s
	public void BFS(int s, int d) { 
		int eNode = s;
		visited = new boolean[V];
		parent = new int[V]; 

		// Create a queue for BFS 
		LinkedList<Integer> queue = new LinkedList<Integer>(); 

		// Mark the current node as visited and enqueue it 
		visited[s]=true; 
		queue.add(s); 
		
		
		while (queue.size() != 0) { 
			s = queue.poll(); 

			Iterator<Integer> i = adj[s].listIterator(); 
			while (i.hasNext()) {
				int n = i.next(); 
				if (!visited[n]) { 
					visited[n] = true; 
					parent[n] = s;
					
					if(n == d) {
						foundPlayer = true;		
						
						// enemy is at enode, player is at d
						// knowing that theres a path between these two we can
						// recursively get the parent of a node until we reach
						// the player and then move to that one.

						int currNode = parent[d];
						while(parent[currNode] != eNode) {
							// if the next node in the path is the player then break
							if(currNode == 0) {
								nextNode = d;
								return;
							}
							System.out.print(currNode + ", ");
							currNode = parent[currNode]; 
						}
						System.out.println("next node found: " + currNode);
						nextNode = currNode;
						break;
					}
					
					queue.add(n); 
				} 
			} 
		}
	}
	
	public int getNextNode() {
		return nextNode;
	}
	
	public boolean foundPlayer() {
		return foundPlayer;
	}
	
}
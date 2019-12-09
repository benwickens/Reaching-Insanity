import java.util.Iterator;
import java.util.LinkedList;

/**
 * Purpose:
 * Produces a graph and performs a breadth-first search.
 * <br>
 * @author Alan Tollett.
 * @version 1.0
 */ 
public class Graph {
	/**No. of vertices in the graph.*/
	private int V; 
	/**Linked list representing the adjacency list.*/
	private LinkedList<Integer> adj[];
	/**An array containing the nodes already visited.*/
	private boolean[] visited;
	/**Store of the previous node from a node.*/
	private int[] parent;
	/**Result of if player is found from the Breadth-First Search.*/
	private boolean foundPlayer;
	/**Next node in the array.*/
	private int nextNode;

	/**
	 * @param v - Number of vertices.
	 * Creates an instance of type Graph.
	 */
	public Graph(int v) {
		V = v;
		adj = new LinkedList[v];
		for (int i = 0; i < v; ++i) {
			adj[i] = new LinkedList();
		}
	}

	/**
	 * @param v - Starting edge. 
	 * @param w - Ending edge.
	 * Adds an edge between two vertices.
	 */
	public void addEdge(int v, int w) {
		adj[v].add(w);
	}

	// prints BFS traversal from a given source s

	/**
	 * @param s - The location of enemy.
	 * @param d - The location of the player. 
	 * Performs a Breadth-First Search to find shortest path to player.
	 */
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
							/* if the next node in the path is the player 
							 * then break*/
							if(currNode == 0) {
								nextNode = d;
								return;
							}
							System.out.print(currNode + ", ");
							currNode = parent[currNode]; 
						}
						System.out.println("next node found: " + currNode);
						nextNode = currNode;
					}
					queue.add(n); 
				} 
			} 
		}
	}

	/**
	 * @return The node to which the enemy should move to next.
	 * Returns the node the enemy will be moving to next move.
	 */
	public int getNextNode() {
		return nextNode;
	}

	/**
	 * @return The result if a path is found.
	 * Returns a boolean with the result of if a path is found.
	 */
	public boolean foundPlayer() {
		return foundPlayer;
	}

}
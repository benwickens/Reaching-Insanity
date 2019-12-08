import java.util.Random;

/**
 * Purpose:
 * Performs a Breadth-First Search on the map to produce shortest path to 
 * player and make the move.
 * <br>
 * @author Alan Tollett and Ben Wickens.
 * @version 1.0
 */ 


public class SmartTargettingEnemy extends Character {

	/**
	 * @param x - This is represents the x coordinate of the player.
	 * @param y - This is represents the y coordinate of the player.
	 * Constructor to create the instance of SmartTargettingEnemy.
	 */
	public SmartTargettingEnemy(int x, int y) {
		super(x, y, "STE.png");
	}

	/**
	 * Moves 
	 */
	@Override
	public void move(Cell[][] grid) {
		Player target = GameWindow.getGameState().getPlayer1();
		int tX = target.getX();
		int tY = target.getY();

		Graph graph = new Graph(grid.length * grid.length);
		loadEdges(grid, graph);
		graph.BFS(x + (y * grid.length), tX + (grid.length * tY));
		
		if(graph.foundPlayer()) {
			// move to parent of enemy in path to player (BFS)
			int nextNode = graph.getNextNode();
			int x = nextNode % grid.length; 
			int y = (nextNode - x) / grid.length;
			moveTo(x, y);
		}else {
			// random move
			randomMove(grid);			
		}
	}
	
	private void randomMove(Cell[][] grid) {
		Random r = new Random();
		boolean moved = false;
		while(!moved) {
			int num = r.nextInt(2);
			if(num == 0) { // horizontal
				num = r.nextInt(2);
				if(num == 0) { // left
					Cell c = grid[x - 1][y];
					if(c.getType().equals(CellType.EMPTY) && c.getItem()
							== null) {
						x -= 1;
						moved = true;
					}
				}else { // right
					Cell c = grid[x + 1][y];
					if(c.getType().equals(CellType.EMPTY) && c.getItem()
							== null) {
						x += 1;
						moved = true;
					}
				}
			}else { // vertical 
				num = r.nextInt(2);
				System.out.println(num);
				if(num == 0) { // up
					Cell c = grid[x][y - 1];
					if(c.getType().equals(CellType.EMPTY) && c.getItem()
							== null) {
						y -= 1;
						moved = true;
					}
				}else { // down
					System.out.println(num);
					Cell c = grid[x][y + 1];
					if(c.getType().equals(CellType.EMPTY) && c.getItem()
							== null) {
						y += 1;
						moved = true;
					}
				}
			}
		}
	}
	
	private void loadEdges(Cell[][] grid, Graph graph) {
		for(int x = 0; x < grid.length; x ++) {
			// x is every row
			for(int y = 0; y < grid.length; y ++) {
				// y is every cell
				Cell c = grid[x][y];
				
				// can only be a neighbour if empty and cell has no item
				if(c.getType().equals(CellType.EMPTY) && c.getItem() 
						== null) {
					// as empty, check adjacent cells (u/d/l/r)
					try {
						c = grid[x][y - 1]; // up
						if(c.getType().equals(CellType.EMPTY) && 
								c.getItem() == null) {
							graph.addEdge(x + (y * grid.length), x + ((y - 1)
									* grid.length));
						}
					}catch(ArrayIndexOutOfBoundsException e) {}
					
					try {
						c = grid[x][y + 1]; // down
						if(c.getType().equals(CellType.EMPTY) && c.getItem() 
								== null) {
							graph.addEdge(x + (y * grid.length), x + ((y + 1)
									* grid.length));
						}
					}catch(ArrayIndexOutOfBoundsException e) {}
					
					try {
						c = grid[x - 1][y]; // left
						if(c.getType().equals(CellType.EMPTY) && c.getItem() 
								== null) {
							graph.addEdge((x - 1) + (y * grid.length), x + 
									(y * grid.length));
						}
					}catch(ArrayIndexOutOfBoundsException e) {}
					
					try {
						c = grid[x + 1][y]; // right
						if(c.getType().equals(CellType.EMPTY) && 
								c.getItem() == null) {
							graph.addEdge((x + 1) + (y * grid.length), 
									x + (y * grid.length));
						}
					}catch(ArrayIndexOutOfBoundsException e) {}	
				}				
			}
		}
	}
	

}
import java.util.*;
public class SmartTargettingEnemy extends Character {
	private Direction direction;
	private  Queue<Cell> queue = new LinkedList<>(); 
	
	public SmartTargettingEnemy(int x, int y, Direction currentDirection) {
		super(x,y);
		this.direction = currentDirection;		
	}
	
	private ArrayList<Cell> getNeighbours(Cell input[][]){
		int[] directionRow = new int[] {-1, 1, 0, 0};
		int[] directionCol = new int[] {0, 0, 1, -1};
		ArrayList<Cell> neighbours = new ArrayList<Cell>();
		for (int i = 0; i < input.length; i++) {
			int newRow = this.x + directionRow[i];
			int newCol = this.y + directionCol[i];
			if ((newRow > 0) && (newCol > 0)) {
				if (input[newCol][newRow].getType() == CellType.EMPTY) { //Add node to queue
					//neighbours.add(e) // what to add to queue? Cell or coordinates
					
				}
			}
		}
		
		return null;
	}

}

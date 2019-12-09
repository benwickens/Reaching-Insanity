/**
 *
 * @version 1.3
 * @author Alan Tollett 
 * <br>
 * This enemy simply moves in a straight line, hits an obstacle and
 *  reverses direction. Each straight line enemy either moves vertically      
 * or horizontally over the map.
 * vertical - y incremented by 1 , horizontal - x incremented by 1
 *then decrement by 1 once a wall is hit

 */

public class StraightLineEnemy extends Character{

	/** the current direction*/
	private Direction currentDirection;

	/**
	 * Contraster for the SLE 
	 * @param x
	 * @param y
	 * @param currentDirection
	 */
	public StraightLineEnemy(int x, int y, Direction currentDirection){
		super(x, y, "SLE.png");
		this.currentDirection = currentDirection;
	}
	/**
	 * The method that move the enemy 
	 * This has gone over the limit as the way we implemented 
	 * is very hard to split it up
	 */
	public void move(Cell [][] grid){
		boolean canMoveHorizontally = true;
		CellType nextType;
		Cell nextCell;


		if (currentDirection.equals(Direction.RIGHT)){
			nextCell = grid[x+1][y];
			if (nextCell.getType().equals(CellType.EMPTY) 
					&& nextCell.getItem() == null){
				x += 1;
			} else {
				nextCell = grid[x - 1][y];
				if (nextCell.getType().equals(CellType.EMPTY) 
						&& nextCell.getItem() == null) {
					currentDirection = Direction.LEFT;
					x -= 1;
				} else {
					canMoveHorizontally = false;
					currentDirection = Direction.UP;
				}
			}
		} else if (currentDirection.equals(Direction.LEFT)){
			nextCell = grid[x - 1][y];
			if (nextCell.getType().equals(CellType.EMPTY) 
					&& nextCell.getItem() == null){
				x -= 1;
			} else {
				nextCell = grid[x + 1][y];
				if (nextCell.getType().equals(CellType.EMPTY) 
						&& nextCell.getItem() == null) {
					currentDirection = Direction.RIGHT;
					x += 1;
				} else {
					canMoveHorizontally = false;
					currentDirection = Direction.UP;
				}
			}
		} else {
			canMoveHorizontally = false;
		}

		if (!canMoveHorizontally) {
			if (currentDirection.equals(Direction.UP)){
				nextCell = grid[x][y - 1];
				if (nextCell.getType().equals(CellType.EMPTY) 
						&& nextCell.getItem() == null){
					y -= 1;
				} else {
					nextCell = grid[x][y + 1];
					if(nextCell.getType().equals(CellType.EMPTY) 
							&& nextCell.getItem() == null) {
						currentDirection = Direction.DOWN;
						y += 1;
					}
				}
			} else if (currentDirection.equals(Direction.DOWN)){
				nextCell = grid[x][y + 1];
				if (nextCell.getType().equals(CellType.EMPTY) 
						&& nextCell.getItem() == null){
					y += 1;
				} else {
					nextCell = grid[x][y - 1];
					if (nextCell.getType().equals(CellType.EMPTY) 
							&& nextCell.getItem() == null) {
						currentDirection = Direction.UP;
						y -= 1;
					}
				}
			}
		}
	}
}
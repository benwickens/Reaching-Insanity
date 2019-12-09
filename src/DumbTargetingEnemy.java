/**
 * @version 1.0
 * @author Alan Tollett 
 * Creates a dumb targeting enemy.
 *
 */
public class DumbTargetingEnemy extends Character {
	/*
	 * This enemy always moves directly towards the player 
	 * in the direction that
	 * would make it get closer to the player this is a very dumb enemy and
	 * easily gets stuck./
	 */
	/**
	 * the direction that the enemy is currently moving.
	 */
	private Direction currentDirection;

	/**the player location at p1*/
	private Player p1;

	/** the player location for p2*/
	private Player p2;

	/**
	 * Creates a DumbTargetingEnemy object
	 *
	 * @param x -the x coordinate the enemy is in.
	 * @param y - the y coordinate the enemy is in.
	 * @param startingDirection - the current direction the enemy is in.
	 * @param p1 - The instance of player 1.
	 * @param p2 - The instance of player 2.
	 */
	public DumbTargetingEnemy(int x, int y, Direction startingDirection
			, Player p1, Player p2) {
		super(x, y, "DTE.png");
		this.p1 = p1;
		this.p2 = p2;
		currentDirection = startingDirection;
	}

	/**
	 * the move method for the dumb targeting enemy
	 * @param grid - The current map.
	 */
	public void move(Cell[][] grid) {

		int xDistFromPlayer1 = x - p1.getX();
		int yDistFromPlayer1 = y - p1.getY();
		int totalDistFromP1 = Math.abs(xDistFromPlayer1) 
				+ Math.abs(yDistFromPlayer1);

		int xDistFromPlayer2 = 100000;
		int yDistFromPlayer2 = 100000;
		int totalDistFromP2 = 200000;

		if (p2 != null) {
			xDistFromPlayer2 = x - p2.getX();
			yDistFromPlayer2 = y - p2.getX();
			totalDistFromP2 = Math.abs(xDistFromPlayer2) 
					+ Math.abs(yDistFromPlayer2);
		}

		int nextX = x;
		int nextY = y;

		if (totalDistFromP1 < totalDistFromP2) {
			// then move towards player 1

			// first try move horizontally
			if (xDistFromPlayer1 < 0) {
				nextX += 1;
			} else if (xDistFromPlayer1 > 0) {
				nextX -= 1;
			}

			// if can make the move do it, otherwise vertical
			if (nextX != x && grid[nextX][y].getType().equals(CellType.EMPTY)
					&& grid[nextX][y].getItem() == null) {
				moveTo(nextX, y);
			} else {
				// then move vertically
				if (yDistFromPlayer1 < 0) {
					nextY = y + 1;
				} else if (yDistFromPlayer1 > 0) {
					nextY = y - 1;
				}
				if (grid[x][nextY].getType().equals(CellType.EMPTY) 
						&& grid[x][nextY].getItem() == null) {
					moveTo(x, nextY);
				}
				// otherwise no valid move -- stuck
			}
		} else {
			// then move towards player 2

			// first try move horizontally
			if (xDistFromPlayer1 < 0) {
				nextX += 1;
			} else if (xDistFromPlayer1 > 0) {
				nextX -= 1;
			}

			// if can make the move do it, otherwise vertical
			if (nextX != x && grid[nextX][y].getType().equals(CellType.EMPTY)
					&& grid[nextX][y].getItem() == null) {
				moveTo(nextX, y);
			} else {
				// then move vertically
				if (yDistFromPlayer1 < 0) {
					nextY = y + 1;
				} else if (yDistFromPlayer1 > 0) {
					nextY = y - 1;
				}
				if (grid[x][nextY].getType().equals(CellType.EMPTY)
						&& grid[x][nextY].getItem() == null) {
					moveTo(x, nextY);
				}
				// otherwise no valid move -- stuck
			}
		}
	}
}


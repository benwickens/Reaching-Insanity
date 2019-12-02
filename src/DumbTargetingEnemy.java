/**
 * File name: DumbTargetingEnemy.java
 *
 * @version 1.0 Creation Date: 27/11/2019 Last Modification date: 28/11/2019
 * @author Millie Quinn <br>
 *         No copyright. <br>
 *         Purpose: Cover the implementation of the move method for all possible
 *         directions that a DumbTargetingEnemy can move in. <br>
 *         Version History 1.0 - basic structure and methods laid out
 *
 */
public class DumbTargetingEnemy extends Character {
	/*
	 * This enemy always moves directly towards the player in the direction that
	 * would make it get closer to the player this is a very dumb enemy and easily
	 * gets stuck
	 */

	private int playerX;
	private int playerY;

	/**
	 * Creates a DumbTargetingEnemy object
	 * 
	 * @param x                the x coordinate the enemy is in
	 * @param y                the y coordinate the enemy is in
	 * @param currentDirection the current direction the enemy is in
	 */
	public DumbTargetingEnemy(int x, int y) {
		super(x, y, "Enemy3.png");
	}

	public void moveDumbEnemy(Cell[][] grid, int playerX, int playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
		move(grid);
	}

	public void move(Cell[][] grid) {
		if (Math.abs(playerX - x) < Math.abs(playerY - y)) {
			if (x > this.playerX) {
				CellType nextType = grid[x - 1][y].getType();
				if (nextType.equals(CellType.EMPTY)) {
					x--;
				}
			} else if (x < this.playerX) {
				CellType nextType = grid[x + 1][y].getType();
				if (nextType.equals(CellType.EMPTY)) {
					x++;
				}
			}
		} else {
			if (y > this.playerY) {
				CellType nextType = grid[x][y - 1].getType();
				if (nextType.equals(CellType.EMPTY)) {
					y--;
				}
			}

			else if (y < this.playerY) {
				CellType nextType = grid[x][y + 1].getType();
				if (nextType.equals(CellType.EMPTY)) {
					y++;
				}
			}
		}
	}

}

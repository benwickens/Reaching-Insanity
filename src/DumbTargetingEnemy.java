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
	private Direction currentDirection;
	private int nextX;
	private int nextY;
	private int playerX;
	private int playerY;
	private Player p1;
	private Player p2;

	/**
	 * Creates a DumbTargetingEnemy object
	 *
	 * @param x                the x coordinate the enemy is in
	 * @param y                the y coordinate the enemy is in
	 * @param currentDirection the current direction the enemy is in
	 */
	public DumbTargetingEnemy(int x, int y, Direction startingDirection, Player p1, Player p2) {
		super(x, y, "DTE.png");
		this.p1 = p1;
		this.p2 = p2;
		currentDirection = startingDirection;
	}

// asdfasdf
	public void move(Cell[][] grid) {
		if ((p1.getX() > x)) {
			if (currentDirection.equals(Direction.RIGHT)) {
				x += 1;
			}
		} else if (currentDirection.equals(Direction.LEFT)) {
			x -= 1;
		} else if (p1.getX() < x) {
			if (currentDirection.equals(Direction.RIGHT)) {
				x -= 1;

			} else if (currentDirection.equals(Direction.LEFT)) {
				x += 1;
			} else if (p1.getX() == x) {
				if (p1.getY() < y) {
					if (currentDirection.equals(Direction.UP) || currentDirection.equals(Direction.RIGHT)
							|| currentDirection.equals(Direction.LEFT)) {
						y -= 1;
					} else {
						y += 1;
					}
				}
			} else {
				if (currentDirection.equals(Direction.DOWN) || currentDirection.equals(Direction.RIGHT)
						|| currentDirection.equals(Direction.LEFT)) {

					y += 1;
				} else {
					y -= 1;
				}
			}
		}
	}
}


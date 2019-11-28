/**
 * File name: DumbTargetingEnemy.java
 *
 * @version 1.0
 * Creation Date: 27/11/2019
 * Last Modification date: 28/11/2019
 * @author Millie Quinn
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * Cover the implementation of the move method for all
 * possible directions that a DumbTargetingEnemy can move in.
 * <br>
 * Version History
 * 1.0 - basic structure and methods laid out
 *
 */
public class DumbTargetingEnemy extends Character{
	/*This enemy always moves directly towards the player in the
	 * direction that wpuld make it get closer to the player
	 *this is a very dumb enemy and easily gets stuck
	 */
	//Direction the enemy is currently moving in
	private Direction currentDirection;
	
	/**
	 * Creates a DumbTargetingEnemy object
	 * @param x the x coordinate the enemy is in
	 * @param y the y coordinate the enemy is in
	 * @param currentDirection the current direction the enemy is in
	 */
	public DumbTargetingEnemy(int x, int y, Direction currentDirection) {
		super(x, y, "Enemy3.png");
		this.currentDirection = currentDirection;
	}

	@Override
	public void move(Cell[][] grid) {
		// TODO Auto-generated method stub
		
	}
	
	
	//Find player
	//calculate how far away player is
	//move towards player
	//cannot move if cell isn't empty
	
}

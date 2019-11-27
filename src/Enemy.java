/**
 * File name: Enemy.java
 *
 * @version 1.1
 * Creation Date: 18/11/2019
 * Last Modification date: 26/11/2019
 * @author Gursimran Randhawa, Yassine Abdalass, Millie Quinn
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * Holds the behaviours and attributes that enemies have.
 * <br>
 * Version History
 * 1.0 - structure of the class made
 * 1.1 - changed the parameters of the abstract move method.
 *
 */

public abstract class Enemy extends Character{
	private String enemyType;

	public Enemy(int xLocation, int yLocation, String enemyType){
		super(xLocation, yLocation);
		this.enemyType = enemyType;
	}

	public String getEnemyType(){
		return enemyType;
	}

	public void setEnemyType(String enemyType) {
		this.enemyType = enemyType;
	}

	public abstract void move(Cell [][] grid);


}

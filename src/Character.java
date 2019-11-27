/**
 * File name: Character.java
 *
 * @version 1.1
 * Creation Date: 18/11/2019
 * Last Modification date: 26/11/2019
 * @author Gursimran Randhawa, Yassine Abdalass, Millie Quinn
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * Holds the behaviours and attributes that characters have.
 * <br>
 * Version History
 * 1.0 - structure of the class made
 * 1.1 - changed to implement more methods.
 */

public abstract class Character {
	
	protected int xLocation;
	protected int yLocation;

	public Character(int xLocation, int yLocation){
		this.xLocation = xLocation;
		this.yLocation = yLocation;
	}

	public int getXLocation(){
		 return xLocation;
	}

	public void setXLocation(int xLocation){
		this.xLocation = xLocation;
	}

	public int getYLocation(){
		return yLocation;
	}

	public void setYLocation(int yLocation){
		this.yLocation = yLocation;
	}

	public void moveTo(int exactX, int exactY) {
		xLocation = exactX;
		yLocation = exactY;
	}
	
	public String toString() {
		//Requires implementation
		return null;
	}
}
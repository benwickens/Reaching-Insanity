import java.util.*;


public class Player extends Character {
	String PLAYER_NAME;
	Integer highestLevel;
	HashMap<Collectable, Integer> inventory;
	
	Player() {
		this.highestLevel = highestLevel;
		this.PLAYER_NAME = PLAYER_NAME;
		this.inventory = inventory;
	}
	
	public void addToInventory(Collectable item) {
		
	}
	
	public boolean hasItem(Collectable item, Integer amount) {
		
	}
	public void useItem(Collectable item, Integer amount) {
		
	}
}

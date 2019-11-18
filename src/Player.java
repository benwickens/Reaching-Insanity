import java.util.*;


public class Player extends Character {
	private String playerName;
	private Integer highestLevel;
	private HashMap<Collectable, Integer> inventory;
	
	public Player(String playerName, HashMap<Collectable, Integer> inventory, int highestLevel) {
		this.highestLevel = highestLevel;
		this.playerName = playerName;
		this.inventory = inventory;
	}
	
	public void addToInventory(Collectable item) {
		
	}
	
	public boolean hasItem(Collectable item, Integer amount) {
		return false;
	}
	
	public void useItem(Collectable item, Integer amount) {
		
	}
}

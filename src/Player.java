import java.util.*;

public class Player extends Character {
	private String playerName;
	private Integer highestLevel;
	private HashMap<Collectable, Integer> inventory;
	
	public Player(String playerName, HashMap<Collectable, Integer> inventory, int highestLevel) {
		this.highestLevel = highestLevel;
		this.setPlayerName(playerName);
		this.inventory = inventory;
	}
	
	public void addToInventory(Collectable item) {
		inventory.replace(item, inventory.get(item) + 1);
	}
	
	public boolean hasItem(Collectable item, Integer amount) {
		if(inventory.get(item) >= amount) {
			return true;
		}
		return false;
	}
	
	public void useItem(Collectable item, Integer amount) {
		inventory.replace(item, inventory.get(item) - amount);
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getInventoryString() {
		String output = "";
		if(inventory.size() > 0) {
			for(Collectable c : Collectable.values()) {
				output += c.toString() + ":" + inventory.get(c) + ", ";
			}
			return output.substring(0, output.length() - 2);
		}
		return output;
	}
}

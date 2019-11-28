import java.util.HashMap;

/**
 * Represents the player at any given time in the current game state
 * @author Gursimran Randhawa, Millie Quinn, and Yassine Abdalass
 * @version 1.0 added attributes and methods (w/out implementation) <br>
 * 1.1 Implemented the constructor <br>.
 */

public class Player extends Character {
    private String name;
    private Integer highestLevel;
    private HashMap<Collectable, Integer> inventory;
    private int imageID;

    /**
     * Constructs a player object
     * @param name name of the current player
     * @param inventory a hashmap of the players current collectables
     * @param highestLevel is the players highest reached level
     */

    public Player(String name, HashMap<Collectable, Integer> inventory, int highestLevel, int imageID) {
        super(-1, -1, "player_down.png");
    	
    	this.highestLevel = highestLevel;
        this.name = name;
        this.imageID = imageID;
        
        if(inventory == null) {
            this.inventory = new HashMap<Collectable, Integer>();
        }else {
            this.inventory = inventory;
        }
    }

    //Adds a collectable to the players inventory
    public void addToInventory(Collectable item) {
    	if(inventory.containsKey(item)) {
            inventory.replace(item, inventory.get(item) + 1);
    	}else {
            inventory.put(item, 1);
    	}
    }

    //Checks to see if the player has the item in their inventory
    public boolean hasItem(Collectable item, Integer amount) {
        if(inventory.containsKey(item) && inventory.get(item) >= amount) {
            return true;
        }
        return false;
    }

    //Uses an item from the players inventory
    public void useItem(Collectable item, Integer amount) {
        inventory.replace(item, inventory.get(item) - amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHighestLevel() {
        return highestLevel;
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
    
    public void move(Cell[][] grid) {
    	
    }

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
}
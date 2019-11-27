import java.util.*;

/**
 * Represents the player at any given time in the current game state
 * @author Gursimran Randhawa, Millie Quinn, and Yassine Abdalass
 * @version 1.0 added attributes and methods (w/out implementation) <br>
 * 1.1 Implemented the constructor <br>.
 */

public class Player extends Character {
    private String playerName;
    private Integer highestLevel;
    private HashMap<Collectable, Integer> inventory;

    /**
     * Constructs a player object
     * @param playerName name of the current player
     * @param inventory a hashmap of the players current collectables
     * @param highestLevel is the players highest reached level
     */

    public Player(int xLocation, int yLocation, String playerName, HashMap<Collectable, Integer> inventory, int highestLevel) {
        super(xLocation, yLocation);
        this.highestLevel = highestLevel;
        this.setPlayerName(playerName);
        this.inventory = inventory;
    }

    //Adds a collectable to the players inventory
    public void addToInventory(Collectable item) {
        inventory.replace(item, inventory.get(item) + 1);
    }

    //Checks to see if the player has the item in their inventory
    public boolean hasItem(Collectable item, Integer amount) {
        if(inventory.get(item) >= amount) {
            return true;
        }
        return false;
    }

    //Uses an item from the players inventory
    public void useItem(Collectable item, Integer amount) {
        inventory.replace(item, inventory.get(item) - amount);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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
}
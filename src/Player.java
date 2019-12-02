import java.util.HashMap;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
        super(-1, -1, "player1_down.png");
    	
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
    
    public void move(Cell[][] grid) {}
    
    public Cell[][] move(Cell[][] grid, KeyEvent event) {
		int nextX = x;
		int nextY = y;
		
		switch (event.getCode()) {
			case RIGHT:
				nextX += 1;
				break;
			case LEFT:
				nextX -= 1;
				break;
			case UP:
				nextY -= 1;
				break;
			case DOWN:
				nextY += 1;
				break;
			default:
				return grid;
		}
		
		Cell nextCell = grid[nextX][nextY];
		
		switch(nextCell.getType()) {
		case WALL:
			nextX = x;
			nextY = y;
			break; 
		case RED_DOOR:
			if(hasItem(Collectable.RED_KEY, 1)) {
				useItem(Collectable.RED_KEY, 1);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
			}else {
				nextX = x;
				nextY = y;
			}
			break; 
		case GREEN_DOOR:
			if(hasItem(Collectable.GREEN_KEY, 1)) {
				useItem(Collectable.GREEN_KEY, 1);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
			}else {
				nextX = x;
				nextY = y;
			}
			break; 
		case BLUE_DOOR:
			if(hasItem(Collectable.BLUE_KEY, 1)) {
				useItem(Collectable.BLUE_KEY, 1);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
			}else {
				nextX = x;
				nextY = y;
			}
			break;
		case TOKEN_DOOR:
			if(hasItem(Collectable.BLUE_KEY, 1)) {
				useItem(Collectable.BLUE_KEY, 1);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
			}else {
				nextX = x;
				nextY = y;
			}
			break;
		case FIRE:
			if(!hasItem(Collectable.FIRE_BOOTS, 1)) {
				// resetLevel();
			}
			break;
		case WATER:
			if(!hasItem(Collectable.FLIPPERS, 1)) {
				// resetLevel();
			}
			break; 
		case ICE:
			if(!hasItem(Collectable.ICE_SKATES, 1)) {
				// resetLevel();
			}
			break;
		case TELEPORTER:
			if(event.getCode().equals(KeyCode.RIGHT)) {
				nextX = nextCell.getLinkX() + 1;
				nextY = nextCell.getLinkY();
			}else if(event.getCode().equals(KeyCode.LEFT)) {
				nextX = nextCell.getLinkX() - 1;
				nextY = nextCell.getLinkY();
			}else if(event.getCode().equals(KeyCode.UP)) {
				nextX = nextCell.getLinkX();
				nextY = nextCell.getLinkY() - 1;
			}else {
				nextX = nextCell.getLinkX();
				nextY = nextCell.getLinkY() + 1;
			}
		case EMPTY:			
			// if next cell does not hold an enemy then check if has an item
			if(nextCell.getItem() != null) {
				addToInventory(nextCell.getItem());
				nextCell.setItem(null);
			}
			break; 
		default:
			break;
		}
		moveTo(nextX, nextY);
		return grid;
    }

	public int getImageID() {
		return imageID;
	}

	public int getAmount(Collectable c) {
		if(inventory.size() > 0 && inventory.get(c) != null) {
			return inventory.get(c);
		}else {
			return 0;
		}
	}
	
	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
}
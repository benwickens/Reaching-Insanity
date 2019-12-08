import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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
    private boolean isDead;
    private boolean hasWon;
    

    /**
     * Constructs a player object
     * @param name name of the current player
     * @param inventory a hashmap of the players current collectables
     * @param highestLevel is the players highest reached level
     */

    //testing for bfs - TO REMOVE
    public Player(int x, int y) {
    	super(x,y, "player1_down.png");
    	
    }
    public Player(String name, HashMap<Collectable, Integer> inventory, int highestLevel, int imageID) {
        super(-1, -1, "player" + imageID + "_down.png");
    	
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
    
    //Adds a collectable to the players inventory
    public void addToInventory(Collectable item, int amount) {
    	if(inventory.containsKey(item)) {
            inventory.replace(item, inventory.get(item) + amount);
    	}else {
            inventory.put(item, amount);
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
		HashMap<Collectable, String> itemAbbreviations = new HashMap<Collectable, String>();
		itemAbbreviations.put(Collectable.TOKEN, "TK");
		itemAbbreviations.put(Collectable.LIFE, "L");
		itemAbbreviations.put(Collectable.RED_KEY, "RK");
		itemAbbreviations.put(Collectable.GREEN_KEY, "GK");
		itemAbbreviations.put(Collectable.BLUE_KEY, "BK");
		itemAbbreviations.put(Collectable.ICE_SKATES, "IS");
		itemAbbreviations.put(Collectable.FLIPPERS, "FL");
		itemAbbreviations.put(Collectable.FIRE_BOOTS, "FB");
    	
        String output = "";
        if(inventory.size() > 0) {
            for(Collectable c : Collectable.values()) {
            	if(inventory.get(c) == null) {
            		inventory.put(c, 0);
            	}
                output += itemAbbreviations.get(c) + ":" + inventory.get(c) + ", ";
            }
            return output.substring(0, output.length() - 2);
        }
        return output;
    }
    
    public void move(Cell[][] grid) {}
    
    private void playSound(String path) {
		Media media = new Media(new File(path).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
    }
    
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
			playSound("src/media/sound/bump.wav");
			break; 
		case RED_DOOR:
			if(hasItem(Collectable.RED_KEY, 1)) {
				useItem(Collectable.RED_KEY, 1);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
				playSound("src/media/sound/unlock.wav");
			}else {
				nextX = x;
				nextY = y;
				playSound("src/media/sound/bump.wav");
			}
			break; 
		case GREEN_DOOR:
			if(hasItem(Collectable.GREEN_KEY, 1)) {
				useItem(Collectable.GREEN_KEY, 1);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
				playSound("src/media/sound/unlock.wav");
			}else {
				nextX = x;
				nextY = y;
				playSound("src/media/sound/bump.wav");
			}
			break; 
		case BLUE_DOOR:
			if(hasItem(Collectable.BLUE_KEY, 1)) {
				useItem(Collectable.BLUE_KEY, 1);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
				playSound("src/media/sound/unlock.wav");
			}else {
				nextX = x;
				nextY = y;
				playSound("src/media/sound/bump.wav");
			}
			break;
		case TOKEN_DOOR:
			if(hasItem(Collectable.TOKEN, 5)) {
				useItem(Collectable.TOKEN, 0);
				grid[nextX][nextY] = new Cell(CellType.EMPTY, null);
				playSound("src/media/sound/unlock.wav");
			}else {
				nextX = x;
				nextY = y;
				playSound("src/media/sound/bump.wav");
			}
			break;
		case FIRE:
			if(!hasItem(Collectable.FIRE_BOOTS, 1)) {
				// set player to dead
				isDead = true;
				playSound("src/media/sound/life_lost.mp3");
				// update location
				nextX = x;
				nextY = y;
				// now check in gamestate after move if player is dead
			}else {
				playSound("src/media/sound/fire.wav");
			}
			break;
		case WATER:
			if(!hasItem(Collectable.FLIPPERS, 1)) {
				// set player to dead
				isDead = true;
				playSound("src/media/sound/life_lost.mp3");
				// update location
				nextX = x;
				nextY = y;
				// now check in gamestate after move if player is dead
			}else {
				playSound("src/media/sound/water.wav");
			}
			break; 
		case ICE:
			if(!hasItem(Collectable.ICE_SKATES, 1)) {
				// set player to dead
				isDead = true;
				playSound("src/media/sound/life_lost.mp3");
				// update location
				nextX = x;
				nextY = y;
				// now check in gamestate after move if player is dead
			}else {
				playSound("src/media/sound/ice.wav");

				int x1 = nextX;
				int y1 = nextY;
				
				if(event.getCode().equals(KeyCode.RIGHT)) {
					while(grid[x1][nextY].getType().equals(CellType.ICE)) {
						x1+=1;
					}
					nextX = x1;
				}else if(event.getCode().equals(KeyCode.LEFT)) {
					while(grid[x1][nextY].getType().equals(CellType.ICE)) {
						x1-=1;
					}
					nextX = x1;
				}else if(event.getCode().equals(KeyCode.UP)) {
					while(grid[nextX][y1].getType().equals(CellType.ICE)) {
						y1 -= 1;
					}
					nextY = y1;
				}else if(event.getCode().equals(KeyCode.DOWN)) {
					while(grid[nextX][y1].getType().equals(CellType.ICE)) {
						y1+= 1;
					}
					nextY = y1;
				}
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
			playSound("src/media/sound/teleport.wav");
			break;
		case GOAL:
			// multiplayer is level 100, so don't update highest level for players on completion.
			nextX = x;
			nextY = y;
			hasWon = true;
			break;
		case EMPTY:						
			for(Character e : GameWindow.getGameState().getEnemies()) {
				if(e.getX() == nextX && e.getY() == nextY) {
					isDead = true;
				}
			}
			
			if(nextCell.getItem() != null) {
				addToInventory(nextCell.getItem());
				nextCell.setItem(null);
				playSound("src/media/sound/pickup.wav");
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
	public boolean hasWon() {
		return hasWon;
	}
	public void setHasWon(boolean hasWon) {
		this.hasWon = hasWon;
	}
	public boolean isDead() {
		return isDead;
	}
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public void clearInventory() {
		inventory.clear();
	}

	
}
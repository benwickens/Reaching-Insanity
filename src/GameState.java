

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Represents the state of the game at any point in time (enemy/player locations, state of cells...)
 * @author Stefan, Alan
 * @version 1.0 Created the rough outline of the class (attributes 
 * and methods but with little implementation) - Stefan <br>
 * 1.1 implemented the constructor which creates a player and loads the cells into the grid - Alan
 * 1.2 created hash map to map cell type to its abbreviation for file output - Stefan
 */

public class GameState {
	
	/**Represents the state of the cells in the grid*/
	private Cell[][] grid;
	/**Reference to player class which stores player location/inventory ... */
	private Player player;
	/**The current level*/
	private int level;
	/**All of the enemies currently alive*/
	private ArrayList<Enemy> enemies; 
	
	private HashMap<CellType,String>mapTypeToString;
	
	/**
	 * Creates a gamestate object
	 * @param levelFile the level to be played
	 * @param playerName the name of the player
	 */
	public GameState(File levelFile, String playerName) {
		player = new Player(playerName, null, 0); // replace 0 w db query result
		enemies = new ArrayList<Enemy>();
		readFileToGrid(levelFile);
		convertTypeToString();
	}
	
	/**
	 * Loads the Cells specified in the input file into the grid 
	 * @param levelFile the file representing a level
	 */
	private void readFileToGrid(File levelFile) {
		try {
			Scanner s = new Scanner(levelFile);
			int y = 0;
			while(s.hasNextLine()) {
				String line = s.nextLine();
				String[] cells = line.split(",");
				
				if(y == 0) {
					int xSize = Integer.parseInt(cells[0]);
					int ySize = Integer.parseInt(cells[1]);
					grid = new Cell[xSize][ySize];
				}else {
					for(int x = 0; x < cells.length; x++) {
						String cellString = cells[x];
						Cell c = null;
						switch(cellString) {
						case "P":
							c = new Cell(CellType.EMPTY, null);
							player.moveTo(x, y);
							break;
						case "W":
							c = new Cell(CellType.WALL, null);
							break;
						case "I":
							c = new Cell(CellType.ICE, null);
							break;
						case "F":
							c = new Cell(CellType.FIRE, null);
							break;
						case "E":
							c = new Cell(CellType.EMPTY, null);
							break;
						default:
							c = new Cell(CellType.EMPTY, null);
							break;
						}
						
						grid[x][y-1] = c;
					}
				}
				y++;
			}
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error: level file not found.");
			System.exit(-1);
		}
	}
	
	public void exitGame(){
		
	}
	
	public void progressToNextLevel(){
		
	}
	
	/**
	 * Saves the current state of the game.
	 */
	public void save(){
		String outputStr = "";
		for(int y = 0; y < grid.length; y++) { // each row (line)
			for(int x = 0; x < grid.length; x++) { // each cell in a row 
				Cell c = grid[x][y];			

				if(c.getType().equals(CellType.EMPTY)) {
					if(player.getX() == x && player.getY() == y) {
						outputStr += "P";
					}else {
						boolean hasEnemy = false;
						for(Enemy e : enemies) {
							if(e.getX() == x && e.getY() == y) {
								outputStr += e.getType();
								hasEnemy = true;
							}
						}
						
						if(!hasEnemy) {
							outputStr += "E";
						}
					}
				}else { 
					// otherwise we store the type and the item the cell holds	
					outputStr += c.getType().toString();
					if(c.getItem() != null) {
						outputStr += ":" + c.getItem().toString();
					}
				}

				if((x+1) < grid.length) { // ensures the line doesn't end with a ','
					outputStr += ",";
				}
			}
			if((y+1) < grid.length) { // ensures the last line is not empty
				outputStr += "\n";
			}
		}
		
		try {
			File outputFolder = new File("src/SavedGames/" + player.getPlayerName());
			if(!outputFolder.exists()) {
				outputFolder.mkdirs();	
			}
			File outputFile = new File(outputFolder.getPath() + "/level" + level + ".txt");
			outputFile.createNewFile();
			PrintWriter w = new PrintWriter(outputFile);
			w.print(outputStr);
			w.print(player.getInventoryString());
			w.flush();
			w.close();
		} catch (IOException e) {
			System.out.println("ERROR: Cannot create file.");
			e.printStackTrace();
		}
		
	}
	/**
	 * Converts the given Cell type to it's string abbreviation
	 */
	public void convertTypeToString(){
		mapTypeToString = new HashMap<>();
		mapTypeToString.put(CellType.WALL, "W");
		mapTypeToString.put(CellType.BLUE_DOOR,"CDB");
		mapTypeToString.put(CellType.EMPTY, "E");
		mapTypeToString.put(CellType.FIRE, "F");
		mapTypeToString.put(CellType.GREEN_DOOR, "CDG");
		mapTypeToString.put(CellType.RED_DOOR, "CDR");
		mapTypeToString.put(CellType.ICE, "ICE");
		mapTypeToString.put(CellType.TELEPORTER, "TPR");
		mapTypeToString.put(CellType.WATER, "WA");
		mapTypeToString.put(CellType.GOAL, "G");
	}

	/**
	 * @return the grid
	 */
	public Cell[][] getGrid() {
		return grid;
	}

	/**
	 * @param grid the grid to set
	 */
	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the enemies
	 */
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	/**
	 * @param enemies the enemies to set
	 */
	public void setEnemies(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	/**
	 * @return the mapTypeToString
	 */
	public HashMap<CellType,String> getMapTypeToString() {
		return mapTypeToString;
	}

	/**
	 * @param mapTypeToString the mapTypeToString to set
	 */
	public void setMapTypeToString(HashMap<CellType,String> mapTypeToString) {
		this.mapTypeToString = mapTypeToString;
	}

}

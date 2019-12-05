import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private Player player1;
	/**Reference to the second player (null if single player) */
	private Player player2;
	/**The current level*/
	private int level;
	/**All of the enemies currently alive*/
	private static ArrayList<Character> enemies; 
	/**Maps a CellType to a the file abbreviation*/
	private HashMap<CellType, String> cellAbbreviations;
	/**Maps a Collectable to a the file abbreviation*/
	private HashMap<Collectable, String> itemAbbreviations;
	/**Used when reading teleporters from a file*/
	private int tempTeleportX = -1;
	/**Used when reading teleporters from a file*/
	private int tempTeleportY = -1;
	private Database db;

	/**
	 * Creates a gamestate object
	 * @param levelFile the level to be played
	 * @param player1Name the name of the player1
	 * @throws SQLException 
	 */
	public GameState(String player1Name, String player2Name, File levelFile) throws SQLException {
		this.level = Integer.parseInt(levelFile.getName().substring(6, levelFile.getName().indexOf(".txt")));
		// multiplayer is level 100, so don't update highest level for players on completion.
		

		
		db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", "root", "");
		
		ResultSet rs = db.query("SELECT highest_level, image_id FROM player WHERE name=\"" + player1Name + "\"");
		rs.next();
		player1 = new Player(player1Name, null, rs.getInt("highest_level"), rs.getInt("image_id"));
		
		if(player2Name != null) {
			rs = db.query("SELECT highest_level, image_id FROM player WHERE name=\"" + player2Name + "\"");
			rs.next();
			player2 = new Player(player2Name, null, rs.getInt("highest_level"), rs.getInt("image_id"));
		}
		
		enemies = new ArrayList<Character>();
		loadAbbreviations();
		readFile(levelFile);
	}
	

	
	/**
	 * Loads the Cells specified in the input file into the grid 
	 * @param levelFile the file representing a level
	 */
	private void readFile(File levelFile) {
		try {
			Scanner s = new Scanner(levelFile);
			int y = 0;
			while(s.hasNextLine()) {
				String line = s.nextLine();
				String[] cells = line.split(",");

				if(y == 0) { // first line is the size of the grid
					int xSize = Integer.parseInt(cells[0]);
					int ySize = Integer.parseInt(cells[1]);
					grid = new Cell[xSize][ySize];
				}else if(y == 1) { // second line is the inventory of player 1
					if(!cells[0].equals("null")) {
						for(int i = 0; i < cells.length; i++) {
							String[] parts = cells[i].split(":");
							String itemAbbr = parts[0].replace(" ", "");
							int amount = Integer.parseInt(parts[1]);
							
							for(Collectable c : Collectable.values()) {
								if(itemAbbreviations.get(c).equals(itemAbbr)) {
									player1.addToInventory(c, amount);
								}
							}
						}
					}	
				}else if(y == 2){ // third line is the inventory of player 2
					if(!cells[0].equals("null")) {
						for(int i = 0; i < cells.length; i++) {
							String[] parts = cells[i].split(":");
							String itemAbbr = parts[0].replace(" ", "");
							int amount = Integer.parseInt(parts[1]);
							
							for(Collectable c : Collectable.values()) {
								if(itemAbbreviations.get(c).equals(itemAbbr)) {
									player2.addToInventory(c, amount);
								}
							}
						}
					}	
				}else {
					for(int x = 0; x < cells.length; x++) {
						CellType type = null;
						Collectable item = null;
						if(cells[x].contains(":")) {
							// the type of cell is the first part
							type = getCellType(cells[x].split(":")[0]);
							// now deal with the extra information
							String extraInfo = cells[x].split(":")[1];
							if(extraInfo.equals("P1")) {
								player1.moveTo(x, y - 3);
							}else if(extraInfo.equals("P2")) {
								if(player2 != null) {
									player2.moveTo(x, y - 3);
								}
							}else if(extraInfo.equals("SLE")) {
								enemies.add(new StraightLineEnemy(x, y - 3, Direction.RIGHT));
							}
							else if(extraInfo.equals("WFE")) {
								enemies.add(new WallFollowingEnemy(x, y - 3, Direction.DOWN));
							}
							else if(extraInfo.equals("DTE")) {
								enemies.add(new DumbTargetingEnemy(x, y - 3));
							}else if(extraInfo.equals("STE")) {
								enemies.add(new SmartTargettingEnemy(x, y - 3));
							}
							else if(extraInfo.contains("-")) {
								// then a teleporter as cell is represented as TP:X-Y
								tempTeleportX = Integer.parseInt(extraInfo.split("-")[0]);
								tempTeleportY = Integer.parseInt(extraInfo.split("-")[1]);
							}							
							else {
								item = getItemType(cells[x].split(":")[1]);
							}
						}else {
							type = getCellType(cells[x]);
						}

						// after the above, if the cell type is null then the file is
						// incorrectly formatted.
						if(type == null) {
							System.out.println("ERROR: File not formatted properly.");
							System.exit(-1);
						}else {
							Cell c = new Cell(type, item);
							if(type.equals(CellType.TELEPORTER) && tempTeleportX != -1) {
								c.setLinkX(tempTeleportX);
								c.setLinkY(tempTeleportY);
								tempTeleportX = -1;
								tempTeleportY = -1;
							}
							grid[x][y - 3] = c;
						}
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
		
		outputStr += grid.length + "," + grid.length + "\n";
		
		outputStr += player1.getInventoryString() + "\n";
		
		if(player2 == null) {
			outputStr += "null\n";
		}else {
			outputStr += player2.getInventoryString() + "\n";
		}
		
		for(int y = 0; y < grid.length; y++) { // each row (line)
			for(int x = 0; x < grid.length; x++) { // each cell in a row 
				Cell c = grid[x][y];			
				CellType t = c.getType();
				
				if(t.equals(CellType.EMPTY)) {
					if(player1.getX() == x && player1.getY() == y) {
						outputStr += "E:P1";
					}else if(player2 != null && player2.getX() == x && player2.getY() == y) {
						outputStr += "E:P2";
					}else {
						boolean hasEnemy = false;
						for(Character e : enemies) {
							if(e.getX() == x && e.getY() == y) {
								String enemyType =  e.getClass().getName();
								String abbr = "E:";
								for(int i = 0; i < enemyType.length(); i++) {
									if(java.lang.Character.isUpperCase(enemyType.charAt(i))) {
										abbr += enemyType.charAt(i);
									}
								}
								outputStr += abbr;
								hasEnemy = true;
							}
						}
						if(!hasEnemy) {
							outputStr += cellAbbreviations.get(t);
						}
					}
					if(c.getItem() != null) {
						outputStr += ":" + itemAbbreviations.get(c.getItem());
					}
				}else if(t.equals(CellType.FIRE) || t.equals(CellType.WATER) 
						|| t.equals(CellType.ICE)) {
					if(player1.getX() == x && player1.getY() == y) {
						outputStr += cellAbbreviations.get(t) + ":P";
					}else {
						outputStr += cellAbbreviations.get(t);
					}
				}else {
					outputStr += cellAbbreviations.get(t);
				}

				if((x+1) < grid.length) { // ensures the line doesn't end with a ','
					outputStr += ",";
				}
			}
			if((y) < grid.length) { // ensures the last line is not empty
				outputStr += "\n";
			}
		}
		
		try {
			saveFile(player1, outputStr);
			if(player2 != null) {
				saveFile(player2, outputStr);
			}
		} catch (IOException e) {
			System.out.println("ERROR: Cannot create file.");
			e.printStackTrace();
		}
	}

	private void saveFile(Player p, String outputStr) throws IOException {
		File outputFolder = new File("src/SavedGames/" + p.getName());
		if(!outputFolder.exists()) {
			outputFolder.mkdirs();	
		}
		String outputFilePath = outputFolder.getPath() + "/Level " + level + ".txt";
		
		File outputFile = new File(outputFilePath);
		outputFile.createNewFile();
		PrintWriter w = new PrintWriter(outputFile);
		w.print(outputStr);
		w.flush();
		w.close();
	}
	
	/**
	 * Converts the given Cell type to it's string abbreviation
	 */
	private void loadAbbreviations(){
		cellAbbreviations = new HashMap<>();
		cellAbbreviations.put(CellType.WALL, "W");
		cellAbbreviations.put(CellType.BLUE_DOOR,"CDB");
		cellAbbreviations.put(CellType.EMPTY, "E");
		cellAbbreviations.put(CellType.FIRE, "F");
		cellAbbreviations.put(CellType.GREEN_DOOR, "CDG");
		cellAbbreviations.put(CellType.RED_DOOR, "CDR");
		cellAbbreviations.put(CellType.TOKEN_DOOR, "TDR");
		cellAbbreviations.put(CellType.ICE, "I");
		cellAbbreviations.put(CellType.TELEPORTER, "TP");
		cellAbbreviations.put(CellType.WATER, "WA");
		cellAbbreviations.put(CellType.GOAL, "G");
		cellAbbreviations.put(CellType.TOKEN_DOOR, "CDT");
		
		itemAbbreviations = new HashMap<>();
		itemAbbreviations.put(Collectable.TOKEN, "TK");
		itemAbbreviations.put(Collectable.LIFE, "L");
		itemAbbreviations.put(Collectable.RED_KEY, "RK");
		itemAbbreviations.put(Collectable.GREEN_KEY, "GK");
		itemAbbreviations.put(Collectable.BLUE_KEY, "BK");
		itemAbbreviations.put(Collectable.ICE_SKATES, "IS");
		itemAbbreviations.put(Collectable.FLIPPERS, "FL");
		itemAbbreviations.put(Collectable.FIRE_BOOTS, "FB");
	}


	public Player getPlayer1() {
		return player1;
	}
	
	public Player getPlayer2() {
		return player2;
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
	 * @return the enemies
	 */
	public ArrayList<Character> getEnemies() {
		return enemies;
	}

	
	private CellType getCellType(String abbreviation) {
		for(CellType type : cellAbbreviations.keySet()) {
			if(cellAbbreviations.get(type).equals(abbreviation)) {
				return type;
			}
		}
		return null;
	}
	
	private Collectable getItemType(String abbreviation) {
		for(Collectable collectable : itemAbbreviations.keySet()) {
			if(itemAbbreviations.get(collectable).equals(abbreviation)) {
				return collectable;
			}
		}
		return null;
	}

}

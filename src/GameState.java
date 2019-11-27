

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
	private ArrayList<Character> enemies; 
	/**Maps a CellType to a the file abbreviation*/
	private HashMap<CellType, String> cellAbbreviations;
	/**Maps a Collectable to a the file abbreviation*/
	private HashMap<Collectable, String> itemAbbreviations;

	private int a;
	private int b;
	private int c;
	private int d;



	/**
	 * Creates a gamestate object
	 * @param levelFile the level to be played
	 * @param playerName the name of the player
	 */
	public GameState(File levelFile, String playerName, int level) {
		player = new Player(playerName,null,0); // replace 0 w db query result
		enemies = new ArrayList<Character>();
		loadAbbreviations();
		readFileToGrid(levelFile);
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
						CellType type = null;
						Collectable item = null;
						if(cells[x].contains(":")) {
							// the type of cell is the first part
							type = getCellType(cells[x].split(":")[0]);
							// now deal with the extra information
							String extraInfo = cells[x].split(":")[1];
							if(extraInfo.equals("P")) {
								player.moveTo(x, y - 1);
							} else if(extraInfo.equals("SLE")) {
								enemies.add(new StraightLineEnemy(x, y - 1, Direction.RIGHT));
							}
//							else if(extraInfo.equals("WFE")) {
//								enemies.add(new WallFollowingEnemy(x, y - 1, Direction.RIGHT));
//							}else if(extraInfo.equals("DTE")) {
//								enemies.add(new DumbTargettingEnemy(x, y - 1, Direction.RIGHT));
//							}else if(extraInfo.equals("STE"))S {
//								enemies.add(new SmartTargettingEnemy(x, y - 1, Direction.RIGHT));
//							}
							else if (extraInfo.equals("I")){
										a = x;
										b = y;
							}else if (extraInfo.equals("O")){
										c = x;
										d = y;
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
							grid[x][y - 1] = c;
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

	/**
	 * returns the tp out X location
	 */
	public int locationIX(){
		return  c ;
	}
	/**
	 * returns the tp out Y location
	 */
	public int locationIY(){
		return  d ;
	}
	/**
	 * returns the tp in X location
	 */
	public int locationOX(){
		return  a ;
	}
	/**
	 * returns the tp in Y location
	 */
	public int locationOY(){
		return  b ;
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
				CellType t = c.getType();
				
				if(t.equals(CellType.EMPTY)) {
					if(player.getX() == x && player.getY() == y) {
						outputStr += "P";
					}else {
						boolean hasEnemy = false;
						for(Character e : enemies) {
							if(e.getX() == x && e.getY() == y) {
								String enemyType =  e.getClass().getName();
								String abbr = "";
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
					if(player.getX() == x && player.getY() == y) {
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
		
//		UNCOMMENT LATER, PREVENTS level0.txt being updated constantly
//		try {
//			File outputFolder = new File("src/SavedGames/" + player.getName());
//			if(!outputFolder.exists()) {
//				outputFolder.mkdirs();	
//			}
//			File outputFile = new File(outputFolder.getPath() + "/level" + level + ".txt");
//			outputFile.createNewFile();
//			PrintWriter w = new PrintWriter(outputFile);
//			w.print(outputStr);
//			w.print(player.getInventoryString());
//			w.flush();
//			w.close();
//		} catch (IOException e) {
//			System.out.println("ERROR: Cannot create file.");
//			e.printStackTrace();
//		}
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
		cellAbbreviations.put(CellType.ICE, "I");
		cellAbbreviations.put(CellType.TELEPORTER, "TP");
		cellAbbreviations.put(CellType.WATER, "WA");
		cellAbbreviations.put(CellType.GOAL, "G");
		
		itemAbbreviations = new HashMap<>();
		itemAbbreviations.put(Collectable.TOKEN, "TK");
		itemAbbreviations.put(Collectable.SPEAR, "SP");
		itemAbbreviations.put(Collectable.SHIELD, "SH");
		itemAbbreviations.put(Collectable.RED_KEY, "RK");
		itemAbbreviations.put(Collectable.GREEN_KEY, "GK");
		itemAbbreviations.put(Collectable.BLUE_KEY, "BK");
		itemAbbreviations.put(Collectable.ICE_SKATES, "IS");
		itemAbbreviations.put(Collectable.FLIPPERS, "FL");
		itemAbbreviations.put(Collectable.FIRE_BOOTS, "FB");
		itemAbbreviations.put(Collectable.DAGGER, "DG");
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
	public ArrayList<Character> getEnemies() {
		return enemies;
	}

	/**
	 * @param enemies the enemies to set
	 */
	public void setEnemies(ArrayList<Character> enemies) {
		this.enemies = enemies;
	}

}

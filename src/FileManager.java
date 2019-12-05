import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class FileManager {
	/**Maps a CellType to a the file abbreviation*/
	private HashMap<CellType, String> cellAbbreviations;
	
	/**Maps a Collectable to a the file abbreviation*/
	private HashMap<Collectable, String> itemAbbreviations;
	
	/**Used when reading teleporters from a file*/
	private int tempTeleportX = -1;
	
	/**Used when reading teleporters from a file*/
	private int tempTeleportY = -1;
	
	public FileManager() {
		loadAbbreviations();
	}
	
	/**
	 * Saves the current state of the game.
	 */
	public void save(GameState gameState){
		Player p1 = gameState.getPlayer1();
		Player p2 = gameState.getPlayer2();
		Cell[][] grid = gameState.getGrid();
		
		String outputStr = grid.length + "," + grid.length + "\n"; // size
		outputStr += p1.getInventoryString() + "\n"; // player 1 inv 
		outputStr += "null\n"; // player 2 inv, always null as no multiplayer save
		outputStr += getSeconds(gameState) + "\n"; // time
		
		for(int y = 0; y < grid.length; y++) { // each row (line)
			for(int x = 0; x < grid.length; x++) { // each cell in a row 
				Cell c = grid[x][y];			
				CellType t = c.getType();
				
				if(t.equals(CellType.EMPTY)) {
					if(p1.getX() == x && p1.getY() == y) {
						outputStr += "E:P1";
					}else if(p2 != null && p2.getX() == x && p2.getY() == y) {
						outputStr += "E:P2";
					}else {
						boolean hasEnemy = false;
						for(Character e : gameState.getEnemies()) {
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
					if(p1.getX() == x && p1.getY() == y) {
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
			saveFile(p1, outputStr, gameState.getLevel());
		} catch (IOException e) {
			System.out.println("ERROR: Cannot create file.");
			e.printStackTrace();
		}
	}
	
	private void saveFile(Player p, String outputStr, int level) throws IOException {
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
	 * Loads the Cells specified in the input file into the grid 
	 * @param levelFile the file representing a level
	 */
	public void readFileToGS(File levelFile, GameState gameState) {
		int level = Integer.parseInt(levelFile.getName().substring(6, levelFile.getName().indexOf(".txt")));
		
		Player p1 = gameState.getPlayer1();
		Player p2 = gameState.getPlayer2();
		ArrayList<Character> enemies = new ArrayList<Character>();
		Cell[][] grid = null;
		
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
									p1.addToInventory(c, amount);
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
									p2.addToInventory(c, amount);
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
								p1.moveTo(x, y - 3);
							}else if(extraInfo.equals("P2")) {
								if(p2 != null) {
									p2.moveTo(x, y - 3);
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
			
			
			// now we're done we need to send the data to the gamestate
			gameState.setGrid(grid);
			gameState.setLevel(level);
			gameState.setEnemies(enemies);
			gameState.setPlayer1(p1);
			if(p2 != null) {
				gameState.setPlayer2(p2);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: level file not found.");
			System.exit(-1);
		}
	}
	
	
	/*
	 * 
	 * HELPER FUNCTIONS
	 * 
	 */
	
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
	
	
	public static long getSeconds(GameState gameState) {
		
		return (gameState.getStartTimeMillis() - System.currentTimeMillis())/1000;
	}

}

import javafx.stage.Stage;

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
		
		// Output basic level information (size, inventories, time taken)
		String outputStr = grid.length + "," + grid.length + "\n";
		outputStr += p1.getInventoryString() + "\n";
		outputStr += "null\n";
		outputStr += GameWindow.getSeconds() + "\n";
		
		// then for every row of the grid (horizontal)
		for(int y = 0; y < grid.length; y++) {
			
			// get each individual cell for that grid and add its info to the file
			for(int x = 0; x < grid.length; x++) { 
				Cell c = grid[x][y];
				CellType t = c.getType();
				
				// if the cell is empty then it could contain the player
				// enemies or an item. so check for that and add it.
				if(t.equals(CellType.EMPTY)) {
					
					// player1 check
					if(p1.getX() == x && p1.getY() == y) {
						outputStr += "E:P1";
						
					// player2 check
					}else if(p2 != null && p2.getX() == x && p2.getY() == y) {
						outputStr += "E:P2";
					
					// enemy check
					}else {
						// iterate over all enemies and check if location matches up
						// if so then add the enemies abbreviation to the cell's output
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
						
						// if there is no enemy above then we just add the cell abbreviation
						if(!hasEnemy) {
							outputStr += cellAbbreviations.get(t);
						}
					}
					
					// an item can still be held by a cell if an enemy is on it
					// so this is an if not an else if! add the item to the output
					if(c.getItem() != null) {
						outputStr += ":" + itemAbbreviations.get(c.getItem());
					}
				
				// equally, if the call is fire/water/ice then the player could be on it
				}else if(t.equals(CellType.FIRE) || t.equals(CellType.WATER) || t.equals(CellType.ICE)) {
					// check for player 1
					if(p1.getX() == x && p1.getY() == y) {
						outputStr += cellAbbreviations.get(t) + ":P1";
					// check for player two
					}else if(p2 != null && p2.getX() == x && p2.getY() == y) {
						outputStr += cellAbbreviations.get(t) + ":P2";
					// otherwise we still need to add the cell to the output
					}else {
						outputStr += cellAbbreviations.get(t);
					}
				// in the case that its not a cell that something (enemy/item/player) can be
				// on then we still need to output that
				}else {
					// normally just a cell abbreviation as no extra info is needed
					outputStr += cellAbbreviations.get(t);
					
					// but if a teleporter then we need to add the link!
					if(t.equals(CellType.TELEPORTER)) {
						outputStr += ":" + c.getLinkX() + "-" + c.getLinkY();
					}
				}
				
				// at this point we have dealt with the cell completely and so 
				// if we aren't at the end of the row then we need to add a comma.
				if((x+1) < grid.length) {
					outputStr += ",";
				}
			}
			// at this point we have dealt with the row completely and so we need
			// to add a new line, but only if we aren't already at the end of the file.
			if((y) < grid.length) {
				outputStr += "\n";
			}
		}
		
		// now we have assembeled the output string, just output the file!
		try {
			saveFile(p1, outputStr, gameState.getLevel());
		} catch (IOException e) {
//			PopUp a = new PopUp("ERROR: Cannot create file.",true);
//			Stage stageP = new Stage();
//			stageP.setScene(a.getScene());
//			stageP.show();
//			System.out.println("ERROR: Cannot create file.");
//			e.printStackTrace();
		}
	}
	
	/**
	 * Outputs a level file to the players folder
	 * @param p the player who is saving their game
	 * @param outputStr the text description of the level
	 * @param level the current level number (output file named Level <level>.txt
	 * @throws IOException if the file cannot be created.
	 */
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
		int timeTaken = 0;
		
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
				}else if(y == 3){ // fourth line is the seconds spent on the level
					timeTaken = Integer.parseInt(cells[0]);
					
					int hours = timeTaken / 3600;
					int minutes = (timeTaken % 3600) / 60;
					int seconds = ((timeTaken % 3600) % 60) % 60;
					
					GameWindow.setHours(hours);
					GameWindow.setMinutes(minutes);
					GameWindow.setSeconds(seconds);
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
								p1.moveTo(x, y - 4);
							}else if(extraInfo.equals("P2")) {
								if(p2 != null) {
									p2.moveTo(x, y - 4);
								}
							}else if(extraInfo.equals("SLE")) {
								enemies.add(new StraightLineEnemy(x, y - 4, Direction.RIGHT));
							}
							else if(extraInfo.equals("WFE")) {
								enemies.add(new WallFollowingEnemy(x, y - 4, Direction.DOWN));
							}
							else if(extraInfo.equals("DTE")) {
								if (p2 != null) {
									enemies.add(new DumbTargetingEnemy(x, y - 4, Direction.RIGHT,p1,p2));
								}else{
									enemies.add(new DumbTargetingEnemy(x, y - 4, Direction.RIGHT,p1,null));
								}
							}else if(extraInfo.equals("STE")) {
								enemies.add(new SmartTargettingEnemy(x, y - 4));
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
//							PopUp a = new PopUp("ERROR: File not formatted properly.",true);
//							Stage stageP = new Stage();
//							stageP.setScene(a.getScene());
//							stageP.show();
//							System.out.println("ERROR: File not formatted properly.");
//							System.out.println(cells[x]);
//							//System.exit(-1);
						}else {
							Cell c = new Cell(type, item);
							if(type.equals(CellType.TELEPORTER) && tempTeleportX != -1) {
								c.setLinkX(tempTeleportX);
								c.setLinkY(tempTeleportY);
								tempTeleportX = -1;
								tempTeleportY = -1;
							}
							grid[x][y - 4] = c;
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
//			PopUp a = new PopUp("Error: level file not found.",true);
//			Stage stageP = new Stage();
//			stageP.setScene(a.getScene());
//			stageP.show();
//			System.out.println("Error: level file not found.");
//			System.exit(-1);
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
}

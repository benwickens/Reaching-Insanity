

import java.awt.event.KeyAdapter;//Not sure whether this would be a valid import
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameState extends KeyAdapter { //Does it work. 
	private Cell[][] grid;
	private Player player;
	private static LeaderBoard leaderBoard;
	private int level;
	private int startingTime;
	private ArrayList<Enemy>enemies; 
	
	public GameState(File levelFile, String playerName) {
		player = new Player(playerName, null, 0); // replace 0 w db query result
		readFileToGrid(levelFile);
	}
	
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
	
	public void keyPressed(KeyEvent event)//step Method
	{
		KeyCode key = event.getCode();
		System.out.println(key.getName());
	}
	
	public void exitGame()
	{
		
	}
	
	public void progressToNextLevel()
	{
		
	}
	
	// cant really test yet, but basic idea should work
	public void saveState(){
		String outputStr = "";
		for(int y = 0; y < grid.length; y++) { // each line of the file
			for(int x = 0; x < grid.length; x++) { // each cell of a line
				Cell c = grid[x][y];			

				if(c.getType().equals(CellType.EMPTY)) {
					boolean hasEnemy = false;
					for(Enemy e : enemies) {
						if(e.getX() == x && e.getY() == y) {
							outputStr += e.getType();
							hasEnemy = true;
						}
					}
					
					if(!hasEnemy) {
						outputStr += "EMPTY";
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
		}
		
		try {
			File outputFile = new File("Players/" + player.getPlayerName() + "/level" + level + ".txt");
			if(!outputFile.exists()) {
				outputFile.createNewFile();				
			}
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
	 * @return the leaderBoard
	 */
	public static LeaderBoard getLeaderBoard() {
		return leaderBoard;
	}

	/**
	 * @param leaderBoard the leaderBoard to set
	 */
	public static void setLeaderBoard(LeaderBoard leaderBoard) {
		GameState.leaderBoard = leaderBoard;
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
	 * @return the startingTime
	 */
	public int getStartingTime() {
		return startingTime;
	}

	/**
	 * @param startingTime the startingTime to set
	 */
	public void setStartingTime(int startingTime) {
		this.startingTime = startingTime;
	}
}

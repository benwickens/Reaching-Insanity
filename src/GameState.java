import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

	/**The epoch at which the level started*/
	private long startTimeMillis;
	
	private Database db;

	/**
	 * Creates a gamestate object
	 * @param levelFile the level to be played
	 * @param player1Name the name of the player1
	 * @throws SQLException 
	 */
	public GameState(String player1Name, String player2Name, File levelFile) throws SQLException {
		// as with game window and all other constructors, we have to "set-up" the class,
		// so set the classes attributes - thats all we really need to do in here!
		
		// instantiate the database - needed to find the image and 
		// highest level, which are parameters to the player class.
		db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", "root", "");
		
		// set the start time millis (milliseconds since jan 1st 1990)
		startTimeMillis = System.currentTimeMillis();
		
		// create the player objects from data retrieved from sql query
		ResultSet rs = db.query("SELECT highest_level, image_id FROM player WHERE name=\"" + player1Name + "\"");
		rs.next();
		player1 = new Player(player1Name, null, rs.getInt("highest_level"), rs.getInt("image_id"));
		
		if(player2Name != null) {
			rs = db.query("SELECT highest_level, image_id FROM player WHERE name=\"" + player2Name + "\"");
			rs.next();
			player2 = new Player(player2Name, null, rs.getInt("highest_level"), rs.getInt("image_id"));
		}
		
		// now players created we can read in the player inventories, grid and enemies.
		FileManager fManager = new FileManager();
		fManager.readFileToGS(levelFile, this);		
		
		// all data needed by this class is stored. this is all
		// a constructor should really do so we are done.
	}
		
	public void exitGame(){
		
	}
	
	public void progressToNextLevel(){
		
	}
	



	public void setLevel(int level) {
		this.level = level;
	}
	
	public void setPlayer1(Player p) {
		player1 = p;
	}
	
	public void setPlayer2(Player p) {
		player2 = p;
	}

	public void setEnemies(ArrayList<Character> enemies) {
		this.enemies = enemies;
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

	


	public int getLevel() {
		return level;
	}

	public long getStartTimeMillis() {
		return startTimeMillis;
	}

	public void setStartTimeMillis(long startTimeMillis) {
		this.startTimeMillis = startTimeMillis;
	}
	
}

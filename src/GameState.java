import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Represents the state of the game at any point in time (enemy/player 
 * locations, state of cells...)
 * @author Stefan, Alan, Millie 
 * @version 1.0 Created the rough outline of the class (attributes 
 * and methods but with little implementation) - Stefan <br>
 * 1.1 implemented the constructor which creates a player and loads the cells
 *  into the grid - Alan
 * 1.2 created hash map to map cell type to its abbreviation for file output
 *  - Stefan
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
	/**The database to which leader board and player profiles are stored.*/
	private Database db;
	/**The file containing the data for the map.*/
	private File levelFile;

	/**
	 * Creates a GameState object
	 * @param levelFile the level to be played
	 * @param player1Name the name of the player1
	 * @throws SQLException - Database connection not successful.
	 */
	public GameState(String player1Name, String player2Name, File levelFile) 
			throws SQLException {
		db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", 
				"root", "");
		this.levelFile = levelFile;

		// create the player objects from data retrieved from sql query
		ResultSet rs = db.query("SELECT highest_level, image_id FROM player "
				+ "WHERE name=\"" + player1Name + "\"");
		rs.next();
		player1 = new Player(player1Name, null, rs.getInt("highest_level"), 
				rs.getInt("image_id"));

		if (player2Name != null) {
			rs = db.query("SELECT highest_level, image_id FROM player WHERE "
					+ "name=\"" + player2Name + "\"");
			rs.next();
			player2 = new Player(player2Name, null, rs.getInt("highest_level"),
					rs.getInt("image_id"));
		}
		/* now players created we can read in the player inventories, grid and
		 * enemies. */
		FileManager fManager = new FileManager();
		fManager.readFileToGS(levelFile, this);		
	}

	/**
	 * Sets the level that the game state is at.
	 * @param level - The current level.
	 */
	public void setLevel (int level) {
		this.level = level;
	}

	/**
	 * Sets the local variable to an instance of Player.
	 * @param p - The instance of player being assigned to player 1.
	 */
	public void setPlayer1(Player p) {
		player1 = p;
	}

	/**
	 * Sets the local variable to an instance of Player.
	 * @param p - The instance of player being assigned to player 2.
	 */
	public void setPlayer2(Player p) {
		player2 = p;
	}

	/**
	 * Sets the Arraylist of enemies.
	 * @param enemies - The arraylist of all enemies.
	 */
	public void setEnemies(ArrayList<Character> enemies) {
		this.enemies = enemies;
	}

	/**
	 * Gets the instance of player 1.
	 * @return Player - The instance of player 1.
	 */
	public Player getPlayer1() {
		return player1;
	}

	/**
	 * Gets the instance of player 2.
	 * @return Player - The instance of player 2.
	 */
	public Player getPlayer2() {
		return player2;
	}

	/**
	 * Returns the current map.
	 * @return The instance of the current map.
	 */
	public Cell[][] getGrid() {
		return grid;
	}

	/**
	 * Sets the grid to parameter passed in.
	 * @param grid - Instance of grid to assign.
	 */
	public void setGrid(Cell[][] grid) {
		this.grid = grid;
	}

	/**
	 * Returns the list of enemies.
	 * @return the enemies
	 */
	public ArrayList<Character> getEnemies() {
		return enemies;
	}

	/**
	 * Returns the current level.
	 * @return
	 */
	public int getLevel() {
		return level;
	}
}

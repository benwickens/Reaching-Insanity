

import java.awt.event.KeyAdapter;//Not sure whether this would be a valid import
import java.io.File;
import java.util.ArrayList;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class GameState extends KeyAdapter { //Does it work. 
	private Cell[][] grid;
	private Player player;
	private static LeaderBoard leaderBoard;
	private int level;
	private int startingTime;
	private ArrayList<Enemy>enemies; 
	
	public GameState(File levelFile,String playerName) {
		setGrid(new Cell[9][9]);
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
	
	private void saveState()
	{
		
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

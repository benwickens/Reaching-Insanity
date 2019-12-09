import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Represents the main game window
 * @author Alan Tollett
 * @version 1.0
 */
public class GameWindow {

	/** the number of hours spent on the current level */
	private static int hours;
	/** the number of minutes spent on the current level */
	private static int minutes;
	/** the number of seconds spent on the current level */
	private static int seconds;
	
	/**The path to the logo*/
	private final String LOGO_PATH = "src/media/img/logo.png";
	/**The path to the image folder*/
	private final String IMG_FOLDER = "src/media/img/grid/";
	/**ImageView containing the death screen image*/
	private ImageView deathScreen;
	/**Timeline used to add and remove death screen image*/
	private Timeline timeFrame;
	/**The file that is currently being played*/
	private File file;
	/**The base layout of the window.*/
	private StackPane layout;
	/**the main contents of the scene (player info/grid...)*/
	private BorderPane borderPane;
	/** the pane containing the grid */
	private GridPane gridPane;
	/** The scene to be displayed on the window */
	private Scene scene;
	/** true if the game is paused */
	private boolean paused;
	/** true if the view is currently paused to allow 
	 * time to display a players move */
	private boolean updatingView;
	/** Indicated whether the current game is multiplayer */
	private boolean multiPlayer;
	/** In multiplayer mode it indicates the player to make the next move */
	private int currentPlayer;
	/** Indicates the enemy to next make their move */
	private int currentEnemy;
	/** holds a reference to the current game state */
	private static GameState gameState;
	/**Indicates whether the game should display the enemies moves*/
	private CheckBox showEnemyMovement;
	/**The timeline that swaps between enemies*/
	private Timeline enemyTimeline;
	/**Timeline to update view after enemy has moved*/
	private Timeline enemyStepTimeline;
	/**Timeline to update view after player has died*/
	private Timeline playerDeathTimeline;
	/**Timeline to change view back to the player*/
	private Timeline playerViewTimeline;
	/** Used to update the timer */
	private Timeline timeline;

	/**
	 * Constructs a GameWindow object which contains a
	 *  scene which can be added to the screen
	 * @param player1Name the name of the first player
	 * @param player2Name the name of the second player
	 * @param levelFile the file containing the level to be played
	 */
	public GameWindow(String player1Name, String player2Name, File levelFile) {
		scene = new Scene(new HBox(), 1200, 700);
		scene.setOnKeyPressed(e -> processKeyEvent(e));
		scene.getStylesheets().add("gameWindow.css");

		resetLevel(player1Name, player2Name, levelFile);
	}

	/**
	 * get the scene to add to the stage representing the game window
	 * @return the scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Get the time spend on this level in seconds
	 * @return seconds spent on level
	 */
	public static int getSeconds() {
		return seconds + (60 * minutes) + (60 * 60 * hours);
	}

	/**
	 * Set the time spent on the level in seconds
	 * @param x the amount of seconds
	 */
	public static void setSeconds(int x) {
		seconds = x;
	}

	/**
	 * Set the time spent on the level in minutes
	 * @param x the amount of minutes
	 */
	public static void setMinutes(int x) {
		minutes = x;
	}
	
	/**
	 * Set the time spent on the level in hours
	 * @param x the amount of hours
	 */
	public static void setHours(int x) {
		hours = x;
	}
	
	/**
	 * Get a string representation of time
	 * @return the string representation of time
	 */
	private String getTime() {
		String h = (hours >= 10 ? "" : "0") + hours;
		String m = (minutes >= 10 ? "" : "0") + minutes;
		String s = (seconds >= 10 ? "" : "0") + seconds;
		return h + ":" + m + ":" + s;
	}
	
	/**
	 * Get the gameState that the gamewindow represents
	 * @return the gamestate
	 */
	public static GameState getGameState() {
		return gameState;
	}

	/**
	 * Gets the grid containing the view around a player
	 * @return the grid which can be added to a pane
	 * @throws FileNotFoundException if any images cannot be found
	 */
	private GridPane getGrid() throws FileNotFoundException {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);

		Player player;
		Player otherPlayer;
		if (currentPlayer == 1 || !multiPlayer) {
			player = gameState.getPlayer1();
			otherPlayer = gameState.getPlayer2();
		} else {
			player = gameState.getPlayer2();
			otherPlayer = gameState.getPlayer1();
		}

		int playerX = player.getX();
		int playerY = player.getY();

		for (int gridX = -4; gridX < 4; gridX++) {
			for (int gridY = -4; gridY < 4; gridY++) {
				try {
					int currentGridX = playerX + gridX;
					int currentGridY = playerY + gridY;
					Cell cell = gameState.getGrid()
							[currentGridX][currentGridY];
					StackPane stack = new StackPane();
					if(cell == null) {
						System.out.println(currentGridX + ", " + currentGridY);
					}
					
					stack.getChildren().add(cell.getCellImage());

					if (gridX == 0 && gridY == 0) {
						stack.getChildren().add(player.getImage());
					} else {
						boolean enemyFound = false;
						for (Character enemy : gameState.getEnemies()) {
							if (enemy.getX() == currentGridX && 
									enemy.getY() == currentGridY) {
								stack.getChildren().add(enemy.getImage());
								enemyFound = true;
							}
						}

						if (!enemyFound) {
							if (cell.getItem() != null) {
								stack.getChildren().add(cell.getItemImage());
							}
						}

						if (multiPlayer && otherPlayer.getX() == currentGridX
								&& otherPlayer.getY() == currentGridY) {
							stack.getChildren().add(otherPlayer.getImage());
						}

					}
					pane.add(stack, gridX + 4, gridY + 4);
				} catch (ArrayIndexOutOfBoundsException e) {
					ImageView img = new ImageView(
							new Image(new FileInputStream(
									IMG_FOLDER + "black.png")));
					pane.add(img, gridX + 4, gridY + 4);
				}
			}
		}
		return pane;
	}

	/**
	 * Get the VBox containing player information (image/inventory)
	 * @return the vbox which can be added to the pane
	 * @throws FileNotFoundException if any images cannot be found
	 */
	private VBox getLeft() throws FileNotFoundException {
		Player player;
		if (currentPlayer == 1 || !multiPlayer) {
			player = gameState.getPlayer1();
		} else {
			player = gameState.getPlayer2();
		}

		VBox left = new VBox(60);
		left.setMinWidth(300);
		left.setAlignment(Pos.CENTER);
		Label nameLabel = new Label(player.getName());
		nameLabel.setId("playerName");

		ImageView playerIcon = new ImageView(new Image(new FileInputStream(
				"src/media/img/grid/player" + 
						player.getImageID() + "_down.png")));
		playerIcon.setScaleX(2.25);
		playerIcon.setScaleY(2.25);

		HBox inv1 = new HBox(30);
		inv1.setId("inventoryRow");
		inv1.setMinHeight(60);

		HBox inv2 = new HBox(30);
		inv2.setId("inventoryRow");
		inv2.setMinHeight(60);

		VBox invs = new VBox(10);
		invs.getChildren().addAll(inv1, inv2);

		int items = 0;
		for (Collectable c : Collectable.values()) {
			ImageView img = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + 
							c.toString().toLowerCase() + ".png")));

			int count = player.getAmount(c);

			Label countLabel = new Label(String.valueOf(count));
			countLabel.setId("countLabel");

			StackPane pane = new StackPane();
			pane.getChildren().addAll(img, countLabel);

			if (items < 4) {
				inv1.getChildren().add(pane);
			} else {
				inv2.getChildren().add(pane);
			}
			items++;
		}

		left.getChildren().addAll(nameLabel, playerIcon, invs);
		return left;
	}

	/**
	 * Get the hbox containing the time/options/exit/save buttons
	 * @return the hbox that can be added to the screen
	 */
	private VBox getRight() {
		VBox right = new VBox(40);
		right.setMinWidth(300);
		right.setAlignment(Pos.CENTER);
		Label timeLabel = new Label();
		timeLabel.setId("timeLabel");
		Label optionsLabel = new Label("Options");
		optionsLabel.setId("optionsLabel");

		Label enemyLabel = new Label("Show Enemy Movement");
		enemyLabel.setId("optionsLabel");
		enemyLabel.setStyle("-fx-font-size: 16");;
		
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			if (!paused) {
				seconds++;
				if (seconds == 60) {
					seconds = 0;
					minutes++;
					if (minutes >= 60) {
						minutes = 0;
						hours++;
					}
				}
			}
			timeLabel.setText(getTime());
		}));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

		showEnemyMovement = new CheckBox();
		showEnemyMovement.setSelected(true);

		
		Button pause = new Button("Pause");
		pause.setOnAction(e -> {
			timeline.pause();
			paused = true;
		});

		Button resume = new Button("Resume");
		resume.setOnAction(e -> {
			timeline.play();
			paused = false;
		});

		Button exitGame = new Button("Exit and Save");

		if (multiPlayer) {
			exitGame.setText("Exit Game");
		}

		exitGame.setOnAction(e -> {
			timeline.pause();
			paused = true;
			if (!multiPlayer) {
				FileManager fm = new FileManager();
				fm.save(gameState);
			}
			backToMain(e);
		});

		if(multiPlayer) {
			right.getChildren().addAll(timeLabel, optionsLabel, 
					pause, resume, exitGame);
		}else {
			right.getChildren().addAll(timeLabel, optionsLabel, enemyLabel,
					showEnemyMovement, pause, resume, exitGame);
		}

		return right;
	}

	/**
	 * Deals with the player pressing a key
	 * @param event the key event 
	 */
	private void processKeyEvent(KeyEvent event) {
		if (!paused && !updatingView) {
			Player p1;
			if (currentPlayer == 1 || !multiPlayer) {
				p1 = gameState.getPlayer1();
			} else {
				p1 = gameState.getPlayer2();
			}
			
			// move player
			p1.move(gameState.getGrid(), event);
			
			// prevent the player from pressing a key whilst views are updated
			updatingView = true;
			showPlayerView();
			
			Timeline waitAfterPlayer = new Timeline(new KeyFrame(
					Duration.millis(100), e -> {
				// now view has been updated, check if player has won or died
				if (p1.isDead()) {
					killPlayer();
				} else if (p1.hasWon()) {
					completeLevel();
				} else {
					if(showEnemyMovement.isSelected()) {
						showViews();
					} else {
						for (Character enemy : gameState.getEnemies()) {
							enemy.move(gameState.getGrid());
							if ((enemy.getX() == p1.getX() 
									&& enemy.getY() == p1.getY())) {
								if (!p1.hasItem(Collectable.LIFE, 1)) {
									killPlayer();
								    break;
								} else {
									p1.useItem(Collectable.LIFE, 1);
									
								}
							}
						}
						showPlayerView();
						updatingView = false;
					}
				}
			}));
			waitAfterPlayer.setCycleCount(1);
			waitAfterPlayer.play();
		}
		event.consume();
	}

	/**
	 * Shows the views of the enemies, followed by the (next) player
	 */
	private void showViews() {
		// deal with the enemies
		ArrayList<Character> enemies = gameState.getEnemies();
		currentEnemy = 0;
		if(enemies.size() > 0) {
			enemyTimeline = new Timeline(new KeyFrame(
					Duration.seconds(1.0), e -> {
				Character enemy = enemies.get(currentEnemy);
				gridPane = getEnemyGrid(enemy);
				borderPane.setCenter(gridPane);
				layout.getChildren().set(1, borderPane);

				enemy.move(gameState.getGrid());

				enemyStepTimeline = new Timeline(new KeyFrame(
						Duration.seconds(0.3), e2 -> {
					
					gridPane = getEnemyGrid(enemy);
					borderPane.setCenter(gridPane);
					layout.getChildren().set(1, borderPane);

					playerDeathTimeline = new Timeline(new KeyFrame(
							Duration.seconds(0.2), e3 -> {
						Player p1 = gameState.getPlayer1();
						Player p2 = gameState.getPlayer2();
						if (enemy.getX() == p1.getX() 
								&& enemy.getY() == p1.getY()) {
							killPlayer();// player 1 has died
						} else if (p2 != null 
								&& enemy.getX() == p2.getX() 
								&& enemy.getY() == p2.getY()) {
							killPlayer();
						}
					}));
					playerDeathTimeline.setCycleCount(1);
					playerDeathTimeline.play();
				}));
				enemyStepTimeline.setCycleCount(1);
				enemyStepTimeline.play();
				currentEnemy++;
			}));
			enemyTimeline.setCycleCount(enemies.size());
			enemyTimeline.play();
		}
		playerViewTimeline = new Timeline(
				new KeyFrame(
						Duration.seconds((1.5 * enemies.size()) + 0.5), e -> {
					if (multiPlayer) {
						if (currentPlayer == 1) {
							currentPlayer = 2;
						} else {
							currentPlayer = 1;
						}
						showPlayerView();
						updatingView = false;
					} else {
						showPlayerView();
						updatingView = false;
					}
				}));
		playerViewTimeline.setCycleCount(1);
		playerViewTimeline.play();
	}

	/**
	 * Gets the view of a grid around an enemy
	 * @param enemy the enemy whose view you want to show
	 * @return the grid containing the view
	 */
	private GridPane getEnemyGrid(Character enemy) {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);

		int enemyX = enemy.getX();
		int enemyY = enemy.getY();

		for (int gridX = -4; gridX < 4; gridX++) {
			for (int gridY = -4; gridY < 4; gridY++) {
				try {
					int currentGridX = enemyX + gridX;
					int currentGridY = enemyY + gridY;

					Cell cell = gameState.getGrid()[currentGridX][currentGridY];
					StackPane stack = new StackPane();
					stack.getChildren().add(cell.getCellImage());

					// if drawing the center cell, add this enemy
					if (gridX == 0 && gridY == 0) {
						stack.getChildren().add(enemy.getImage());
						// else see if we can draw player 1
					} else if (currentGridX == gameState.getPlayer1().getX()
							&& currentGridY == gameState.getPlayer1().getY()) {
						stack.getChildren().add(
								gameState.getPlayer1().getImage());
						// else try and draw player 2
					} else if (gameState.getPlayer2() != null
							&& currentGridX == gameState.getPlayer2().getX()
							&& currentGridY == gameState.getPlayer2().getY()) {
						stack.getChildren().add(
								gameState.getPlayer2().getImage());
					} else {

						boolean enemyFound = false;
						for (Character otherEnemy : gameState.getEnemies()) {
							if (otherEnemy.getX() == currentGridX
									&& otherEnemy.getY() == currentGridY) {
								stack.getChildren().add(otherEnemy.getImage());
								enemyFound = true;
							}
						}

						if (!enemyFound) {
							if (cell.getItem() != null) {
								stack.getChildren().add(cell.getItemImage());
							}
						}
					}
					pane.add(stack, gridX + 4, gridY + 4);
				} catch (ArrayIndexOutOfBoundsException e1) {
					ImageView img;
					try {
						img = new ImageView(
								new Image(new FileInputStream(
										IMG_FOLDER + "black.png")));
						pane.add(img, gridX + 4, gridY + 4);
					} catch (FileNotFoundException e2) {
						System.out.println("ERROR: cannot find black image");
						e2.printStackTrace();
					}
				}
			}
		}
		return pane;
	}

	/**
	 * Shows the player view
	 */
	private void showPlayerView() {
		try {
			gridPane = getGrid();
			borderPane.setCenter(gridPane);
			borderPane.setLeft(getLeft());
			layout.getChildren().set(1, borderPane);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resets the level whenever the player is killed
	 */
	private void killPlayer() {
		try {
			if(enemyStepTimeline != null && enemyTimeline != null 
					&& playerDeathTimeline != null 
					&& playerViewTimeline != null) {
				
				enemyTimeline.stop();
				enemyStepTimeline.stop();
				playerDeathTimeline.stop();
				playerViewTimeline.stop();
			
			}
			updatingView = true;
			deathScreen = new ImageView(new Image(
					new FileInputStream("src/media/img/uDied.png")));
			layout.getChildren().add(deathScreen);

			timeFrame = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
				layout.getChildren().remove(deathScreen);
				resetLevel(gameState.getPlayer1().getName(),
						(gameState.getPlayer2() == null ? 
								null : gameState.getPlayer2().getName()),
						file);
			}));
			timeFrame.setCycleCount(1);
			timeFrame.play();
		} catch (FileNotFoundException e1) {
			System.out.println("Cannot Load Image.");
			e1.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Moves on to the next level whenever the player wins
	 */
	private void completeLevel() {
		try {
			updatingView = true;
			deathScreen = new ImageView(new Image(
					new FileInputStream("src/media/img/uWon.png")));
			layout.getChildren().add(deathScreen);

			timeFrame = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
				layout.getChildren().remove(deathScreen);

				int level = gameState.getLevel();
				Player p = gameState.getPlayer1();
				int seconds = getSeconds();

				// not doing leaderboards for multiplayer so back to main
				if(multiPlayer) {
					backToMain(e);
				}else {
					if(level < 5) {
						updateLeaderBoard(p, level, seconds);

						// now lb updated, go to next level
						resetLevel(p.getName(),
								null,
								new File("src/levels/Level " + 
										(level + 1) + ".txt"));
					}else {
						// update and go to main
						updateLeaderBoard(p, level, seconds);
						backToMain(e);
					}
				}

			}));
			timeFrame.setCycleCount(1);
			timeFrame.play();
		} catch (FileNotFoundException e1) {
			System.out.println("Cannot Load Image.");
			e1.printStackTrace();
			System.exit(0);
		}
	}

	/**
	 * Adds the new time to the leaderboard
	 * @param p the player who completed the level
	 * @param level the level they completed
	 * @param seconds the time taken
	 */
	private void updateLeaderBoard(Player p, int level, int seconds) {
		try {
			Database db = new Database(
					"jdbc:mysql://localhost:3306/Reaching_Insanity",
					"root", "");

			ResultSet rs = db.query("SELECT * FROM leaderboard " + 
					"WHERE name='" + p.getName() + 
					"' AND level=" + level);
			
			if(rs.next()) {
				// player already has a time for this level, update
				int oldSeconds = rs.getInt("seconds");
				if(seconds < oldSeconds) {
					db.manipulate("UPDATE leaderboard "
							+ "SET seconds=" + getSeconds() + 
							" WHERE name='" + p.getName() + 
							"' AND level=" + level);
				}
			}else {
				// player does not have a time for this level, add new
				db.manipulate("INSERT INTO leaderboard VALUES (" + 
						level + ", '" + 
						p.getName() + "', " +
						seconds + ")");
				// as new entry, highest level may have changed.
				rs = db.query("SELECT * FROM player WHERE name='" +
				p.getName() + "'");
				if(rs.next() && rs.getInt("highest_level") <= level) {
					db.manipulate("UPDATE player SET highest_level="
							+ (level+1) + 
							" WHERE name='" + p.getName()+ "'");
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Takes the user back to the main menu
	 * @param e the event triggering the event
	 */
	private void backToMain(ActionEvent e) {
		Stage newWindow = (Stage) ((Node) e.getSource())
				.getScene().getWindow();
		newWindow.setScene(Main.getScene());
		newWindow.show();
	}


	/**
	 * Resets the level
	 * @param player1Name the player playing the level
	 * @param player2Name the other player if multiplayer
	 * @param levelFile the level to play
	 */
	private void resetLevel(String player1Name, 
			String player2Name, File levelFile) {
		try {
			currentPlayer = 1;
			updatingView = false;
			paused = false;
			if(timeline != null) {
				timeline.stop();
			}
			if (player2Name != null) {
				multiPlayer = true;
			}
			
			layout = new StackPane();

			ImageView background = new ImageView(
					new Image(new FileInputStream(
							"src/media/img/background.png")));
			layout.getChildren().add(background);

			borderPane = new BorderPane();

			HBox logoBox = new HBox();
			ImageView logo = new ImageView(
					new Image(new FileInputStream(LOGO_PATH)));
			logoBox.getChildren().addAll(logo);
			logoBox.setAlignment(Pos.CENTER);
			borderPane.setTop(logoBox);

			gameState = new GameState(player1Name, player2Name, levelFile);
			borderPane.setLeft(getLeft());
			borderPane.setRight(getRight());

			gridPane = getGrid();
			borderPane.setCenter(gridPane);

			layout.getChildren().add(borderPane);

			scene.setRoot(layout);

			background.fitHeightProperty().bind(scene.heightProperty());
			background.fitWidthProperty().bind(scene.widthProperty());

			currentPlayer = 1;

			file = levelFile;
		} catch (FileNotFoundException | SQLException e) {
			System.out.println("ERROR: Failed to load image(s)"
					+ " or SQL Exception.");
			e.printStackTrace();
			System.exit(0);
		}
	}
}
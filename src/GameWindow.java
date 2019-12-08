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
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameWindow {

	private final String LOGO_PATH = "src/media/img/logo.png";
	private final String IMG_FOLDER = "src/media/img/grid/";
	private ImageView deathScreen;
	private Timeline timeFrame;
	private File file;

	/*
	 * LAYOUT ATTRIBUTES
	 */

	/**
	 * The base layout of the window. The first item on the stack should be the background,
	 * Second should be the border pane Third should be (if dead) a player death screen.
	 */
	private StackPane layout;

	/**
	 * the main contents of the scene Top is logo Left is player Right is options Centre is
	 * grid
	 */
	private BorderPane borderPane;

	/** the pane containing the grid */
	private GridPane gridPane;

	/** The scene to be displayed on the window */
	private Scene scene;

	/*
	 * FUNCTIONALITY ATTRIBUTES
	 */

	/** true if the game is paused */
	private boolean paused;

	/** the number of hours spent on the current level */
	private static int hours;

	/** the number of minutes spent on the current level */
	private static int minutes;

	/** the number of seconds spent on the current level */
	private static int seconds;

	/** true if the view is currently paused to allow time to display a players move */
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

	private File levelFile;

	private Timeline enemyTimeline;
	private Timeline enemyStepTimeline;
	private Timeline playerDeathTimeline;
	private Timeline playerViewTimeline;
	
	/*
	 * NODES DISPLAYED ON THE SCREEN
	 */

	/** Used to update the timer */
	private Timeline timeline;

	/** Used to hold the view in place before moving on to the second player */
	private Timeline viewUpdater;

	public GameWindow(String player1Name, String player2Name, File levelFile) {
		this.levelFile = levelFile;
		scene = new Scene(new HBox(), 1200, 700);
		scene.setOnKeyPressed(e -> processKeyEvent(e));
		scene.getStylesheets().add("gameWindow.css");

		resetLevel(player1Name, player2Name, levelFile);
	}

	public Scene getScene() {
		return scene;
	}

	private String getTime() {
		String h = (hours >= 10 ? "" : "0") + hours;
		String m = (minutes >= 10 ? "" : "0") + minutes;
		String s = (seconds >= 10 ? "" : "0") + seconds;
		return h + ":" + m + ":" + s;
	}

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
					Cell cell = gameState.getGrid()[currentGridX][currentGridY];
					StackPane stack = new StackPane();
					if(cell.getType() == null) {
						System.out.println(currentGridX + ", " + currentGridY);
					}
					
					stack.getChildren().add(cell.getCellImage());

					if (gridX == 0 && gridY == 0) { // if drawing the center cell, add player
						stack.getChildren().add(player.getImage());
					} else {
						boolean enemyFound = false;
						for (Character enemy : gameState.getEnemies()) {
							if (enemy.getX() == currentGridX && enemy.getY() == currentGridY) {
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
							new Image(new FileInputStream(IMG_FOLDER + "black.png")));
					pane.add(img, gridX + 4, gridY + 4);
				}
			}
		}
		return pane;
	}

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
				"src/media/img/grid/player" + player.getImageID() + "_down.png")));
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
					new FileInputStream(IMG_FOLDER + c.toString().toLowerCase() + ".png")));

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

	private VBox getRight() {
		VBox right = new VBox(40);
		right.setMinWidth(300);
		right.setAlignment(Pos.CENTER);

		Label timeLabel = new Label();
		timeLabel.setId("timeLabel");

		Label optionsLabel = new Label("Options");
		optionsLabel.setId("optionsLabel");

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

		showEnemyMovement = new CheckBox("Show Enemy Movement");
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

			try {
				Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
				Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
				newWindow.setScene(new Scene(loadIn, 1200, 700));
				newWindow.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		if(multiPlayer) {
			right.getChildren().addAll(timeLabel, optionsLabel, pause, resume, exitGame);
		}else {
			right.getChildren().addAll(timeLabel, optionsLabel, showEnemyMovement, pause, resume, exitGame);
		}

		return right;
	}

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
			
			Timeline waitAfterPlayer = new Timeline(new KeyFrame(Duration.millis(250), e -> {
				
				// now view has been updated, check if player has won or died
				if (p1.isDead()) {
					killPlayer();
				} else if (p1.hasWon()) {
					completeLevel();
				} else {
					if(showEnemyMovement.isSelected()) { // only ever false in single player
						showViews();
						// when showViews() terminates the view is on the next player
						// and updatingView is set to false so the player can press a key.
					}else {
						for(Character enemy : gameState.getEnemies()) {
							enemy.move(gameState.getGrid());
							if(enemy.getX() == p1.getX() 
									&& enemy.getY() == p1.getY()) {
								killPlayer();
								break;
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

	// this is static because we need to be able to get the seconds
	// spent on the level from multiple locations that don't
	// have a reference to the game window and would make little
	// sense if they did.
	// e.g. the FileManager needs to be able to get the seconds spent on
	// the level for saving purposes, however it doesn't make sense for
	// the file manager to "store" the Game Window. Hence we need static
	// access to this.
	public static int getSeconds() {
		return seconds + (60 * minutes) + (60 * 60 * hours);
	}

	// same goes for the setters of the respective variables
	public static void setSeconds(int x) {
		seconds = x;
	}

	public static void setMinutes(int x) {
		minutes = x;
	}

	public static void setHours(int x) {
		hours = x;
	}

	private void showViews() {
		// deal with the enemies
		ArrayList<Character> enemies = gameState.getEnemies();
		currentEnemy = 0;

		// we want to display every enemy seperated by 1 seconds
		// and for each enemy we want to display their starting
		// position and ending position separated by 0.5 seconds.
		// So we need two timelines (set as class attributes).

		if(enemies.size() > 0) {
			// to deal with each enemy
			enemyTimeline = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {

				// firstly set the grid view to the enemies position
				Character enemy = enemies.get(currentEnemy);
				gridPane = getEnemyGrid(enemy);
				borderPane.setCenter(gridPane);
				layout.getChildren().set(1, borderPane);

				// move the enemy
				enemy.move(gameState.getGrid());

				// wait 0.75 seconds and update the view with the enemies new position
				enemyStepTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), e2 -> {
					
					gridPane = getEnemyGrid(enemy);
					borderPane.setCenter(gridPane);
					layout.getChildren().set(1, borderPane);

					playerDeathTimeline = new Timeline(new KeyFrame(Duration.seconds(0.2), e3 -> {
						// check if the enemy moved on to a player, if so deal with player death!
						Player p1 = gameState.getPlayer1();
						Player p2 = gameState.getPlayer2();

						if (enemy.getX() == p1.getX() && enemy.getY() == p1.getY()) {
							enemyTimeline.stop();
							enemyStepTimeline.stop();
							playerDeathTimeline.stop();
							playerViewTimeline.stop();
							killPlayer();// player 1 has died
						} else if (p2 != null && enemy.getX() == p2.getX() && enemy.getY() == p2.getY()) {
							killPlayer();// player 2 has died (in multiplayer just treat one player dying
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
		// once all of the enemies have moved we need to go back to the players view
		playerViewTimeline = new Timeline(
				new KeyFrame(Duration.seconds((1.5 * enemies.size()) + 0.5), e -> {
					if (multiPlayer) {
						// swap to next player
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
						stack.getChildren().add(gameState.getPlayer1().getImage());
						// else try and draw player 2
					} else if (gameState.getPlayer2() != null
							&& currentGridX == gameState.getPlayer2().getX()
							&& currentGridY == gameState.getPlayer2().getY()) {
						stack.getChildren().add(gameState.getPlayer2().getImage());

						// if not this enemy, or one of the players, check all other enemies.
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
								new Image(new FileInputStream(IMG_FOLDER + "black.png")));
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

	public void killPlayer() {
		try {
			updatingView = true;
			deathScreen = new ImageView(new Image(new FileInputStream("src/media/img/uDied.png")));
			layout.getChildren().add(deathScreen);

			timeFrame = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
				layout.getChildren().remove(deathScreen);
				resetLevel(gameState.getPlayer1().getName(),
						(gameState.getPlayer2() == null ? null : gameState.getPlayer2().getName()),
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

	public void completeLevel() {
		try {
			updatingView = true;
			deathScreen = new ImageView(new Image(new FileInputStream("src/media/img/uWon.png")));
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
								new File("src/levels/Level " + (level + 1) + ".txt"));
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

	private void updateLeaderBoard(Player p, int level, int seconds) {
		try {
			Database db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", "root", "");

			ResultSet rs = db.query("SELECT * FROM leaderboard " + 
					"WHERE name='" + p.getName() + 
					"' AND level=" + level);
			
			if(rs.next()) {
				// player already has a time for this level, update
				int oldSeconds = rs.getInt("seconds");
				if(seconds < oldSeconds) {
					db.manipulate("UPDATE leaderboard "
							+ "SET time=" + getSeconds() + 
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
				rs = db.query("SELECT * FROM player WHERE name='" + p.getName() + "'");
				if(rs.next() && rs.getInt("highest_level") <= level) {
					db.manipulate("UPDATE player SET highest_level=" + (level+1) + 
							" WHERE name='" + p.getName()+ "'");
				}
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void backToMain(ActionEvent e) {
		try {
			Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
			Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
			newWindow.setScene(new Scene(loadIn, 1200, 700));
			newWindow.show();
		} catch (IOException e1) {e1.printStackTrace();}
	}

	public void resetLevel(String player1Name, String player2Name, File levelFile) {
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
			

			// start with instantiating the base layout
			layout = new StackPane();

			// the first item on the stack is the background
			ImageView background = new ImageView(
					new Image(new FileInputStream("src/media/img/background.png")));
			layout.getChildren().add(background);

			// next we need to get the border pane
			borderPane = new BorderPane();

			// firstly we can get the logo as its a simple one line trick (top border)
			HBox logoBox = new HBox();
			ImageView logo = new ImageView(new Image(new FileInputStream(LOGO_PATH)));
			logoBox.getChildren().addAll(logo);
			logoBox.setAlignment(Pos.CENTER);
			borderPane.setTop(logoBox);

			// the left and right are slightly more complicated, so seperated into a seperate
			// method.
			// but as each of them rely on some of the data stored by the gameState we must create
			// that first.
			gameState = new GameState(player1Name, player2Name, levelFile);
			borderPane.setLeft(getLeft());
			borderPane.setRight(getRight());

			// the grid is even more complicated, so we separate again!
			gridPane = getGrid();
			borderPane.setCenter(gridPane);

			// we are done with the border pane so can add it to the stack
			layout.getChildren().add(borderPane);

			// now we are done with all things that should be displayed,
			// we need to set up the scene which contains the layouts
			// defined above.
			scene.setRoot(layout);

			// we also couldn't make the background "stretch" to fit the
			// the scene before making one, so we do that here.
			background.fitHeightProperty().bind(scene.heightProperty());
			background.fitWidthProperty().bind(scene.widthProperty());

			// now we are done with the view we need to consider the other
			// attributes of the class. We only deal here with the ones that
			// are not dealt with by the other methods (getLeft/right...)

			// most can be ignored as default to the expected values,
			// but some others need to be specified:
			currentPlayer = 1;


			// all data needed by this class is now stored. This is all
			// a constructor should really do so we are done.

			file = levelFile;
		} catch (FileNotFoundException | SQLException e) {
			System.out.println("ERROR: Failed to load image(s) or SQL Exception.");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static GameState getGameState() {
		return gameState;
	}

}
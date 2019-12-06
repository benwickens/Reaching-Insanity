import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameWindow {

	private final String LOGO_PATH = "src/media/img/logo.png";
	private final String IMG_FOLDER = "src/media/img/grid/";
	
	/*
	 *LAYOUT ATTRIBUTES 
	 */
	
	/**The base layout of the window.
	 * The first item on the stack should be the background,
	 * Second should be the border pane
	 * Third should be (if dead) a player death screen.*/
	private StackPane layout;
	
	/**the main contents of the scene
	 * Top is logo
	 * Left is player
	 * Right is options
	 * Centre is grid*/
	private BorderPane borderPane;
	
	/**the pane containing the grid*/
	private GridPane gridPane;
	
	/**The scene to be displayed on the window*/
	private Scene scene;
	
	
	/*
	 *FUNCTIONALITY ATTRIBUTES 
	 */
	
	/**true if the game is paused*/
	private boolean paused;
	
	/**the number of hours spent on the current level*/
	private static int hours;
	
	/**the number of minutes spent on the current level*/
	private static int minutes;
	
	/**the number of seconds spent on the current level*/
	private static int seconds;
	
	/**true if the view is currently paused to allow time to display a players move*/
	private boolean updatingView;
	
	/**Indicated whether the current game is multiplayer*/
	private boolean multiPlayer;
	
	/**In multiplayer mode it indicates the player to make the next move*/
	private int currentPlayer;

	/**Indicates the enemy to next make their move*/
	private int currentEnemy;
	
	/**holds a reference to the current game state*/
	private static GameState gameState;
	
	
	/*
	 * NODES DISPLAYED ON THE SCREEN
	 */
	
	/**Used to update the timer*/
	private Timeline timeline;
	
	/**Used to hold the view in place before moving on to the second player*/
	private Timeline viewUpdater;


	
	public GameWindow(String player1Name, String player2Name, File levelFile) {		
		try {
			// start with instantiating the base layout 
			layout = new StackPane();
			
			// the first item on the stack is the background
			ImageView background = new ImageView(new Image(new FileInputStream("src/media/img/background.png")));
			layout.getChildren().add(background);
			
			// next we need to get the border pane
			borderPane = new BorderPane();
			
			// firstly we can get the logo as its a simple one line trick (top border)
			ImageView logo = new ImageView(new Image(new FileInputStream(LOGO_PATH)));
			borderPane.setTop(logo);
			
			// the left and right are slightly more complicated, so seperated into a seperate method.
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
			scene = new Scene(layout, 1200, 700);
			scene.setOnKeyPressed(e -> processKeyEvent(e));
			scene.getStylesheets().add("gameWindow.css");
			
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
			
			if(player2Name != null) {
				multiPlayer = true;
			}
			
			// all data needed by this class is now stored. This is all
			// a constructor should really do so we are done.
		} catch (FileNotFoundException | SQLException e) {
			System.out.println("ERROR: Failed to load image(s) or SQL Exception.");
			e.printStackTrace();
			System.exit(-1);
		}
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
		if(currentPlayer == 1 || !multiPlayer) {
			player  = gameState.getPlayer1();
			otherPlayer  = gameState.getPlayer2();
		}else{
			player  = gameState.getPlayer2();
			otherPlayer  = gameState.getPlayer1();
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
					stack.getChildren().add(cell.getCellImage());
					
					if (gridX == 0 && gridY == 0) { // if drawing the center cell, add player
						stack.getChildren().add(player.getImage());
					}else {
						boolean enemyFound = false;
						for(Character enemy : gameState.getEnemies()) {
							if(enemy.getX() == currentGridX && enemy.getY() == currentGridY) {
								stack.getChildren().add(enemy.getImage());
								enemyFound = true;
							}
						}
						
						if(!enemyFound) {
							if(cell.getItem() != null) {
								stack.getChildren().add(cell.getItemImage());
							}
						}
						
						if(multiPlayer && otherPlayer.getX() == currentGridX && otherPlayer.getY() == currentGridY) {
							stack.getChildren().add(otherPlayer.getImage());
						}
						
						
					}
					pane.add(stack, gridX + 4, gridY + 4);
				} catch (ArrayIndexOutOfBoundsException e) {
					ImageView img = new ImageView(new Image(new FileInputStream(IMG_FOLDER + "black.png")));
					pane.add(img, gridX + 4, gridY + 4);
				}
			}
		}
		return pane;
	}

	private VBox getLeft() throws FileNotFoundException {
		Player player;
		if(currentPlayer == 1 || !multiPlayer) {
			player  = gameState.getPlayer1();
		}else {
			player  = gameState.getPlayer2();
		}
		
		VBox left = new VBox(60);
		left.setMinWidth(300);
		left.setAlignment(Pos.CENTER);
		Label nameLabel = new Label(player.getName());
		nameLabel.setId("playerName");

		ImageView playerIcon = new ImageView(new Image(new FileInputStream("src/media/img/grid/player" + player.getImageID() + "_down.png")));
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
		for(Collectable c : Collectable.values()) {
			ImageView img = new ImageView(new Image(new FileInputStream(
					IMG_FOLDER + c.toString().toLowerCase() + ".png")));
			
			int count = player.getAmount(c);
			
			Label countLabel = new Label(String.valueOf(count));
			countLabel.setId("countLabel");
			
			StackPane pane = new StackPane();
			pane.getChildren().addAll(img, countLabel);
			
			if(items < 4) {
				inv1.getChildren().add(pane);
			}else {
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
		
		if(multiPlayer) {
			exitGame.setText("Exit Game");
		}
		
		exitGame.setOnAction(e -> {
			timeline.pause();
			paused = true;
			
			if(!multiPlayer) {
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

		right.getChildren().addAll(timeLabel, optionsLabel, pause, resume, exitGame);
		return right;
	}

	private void processKeyEvent(KeyEvent event) {
		if (!paused && !updatingView) {
			Player player;
			if(currentPlayer == 1 || !multiPlayer) {
				player  = gameState.getPlayer1();
			}else {
				player  = gameState.getPlayer2();
			}
			
			// move player
			player.move(gameState.getGrid(), event);
			
			// check if player is dead after their move
			if(player.isDead()) {
				// show death screen
				// reset gameState 
				// remove death screem
			}else if(player.hasWon()) {
				// show win screen
				//create game state for next level
				// run an SQL query to update the database
				//		remember if multiplayer then we dont store 
				//		an entry on the database so skip this step
				// set the game windows game state to the new game state
				// reset game windows class variables (e.g. currentPlayer must reset to 1, timer...)
			}else {
				// move enemies and use life or die if enemy moves on to player
//				Iterator<Character> iter = gameState.getEnemies().iterator();
//				while(iter.hasNext()) {
//					Character e = iter.next();
//					e.move(gameState.getGrid());
//					if(e.getX() == player.getX() && e.getY() == player.getY()) {
//						if(player.hasItem(Collectable.LIFE, 1)) {
//							player.useItem(Collectable.LIFE, 1);
//							iter.remove();
//							
//							String bip = "src/media/sound/life_lost.mp3";
//							Media hit = new Media(new File(bip).toURI().toString());
//							MediaPlayer mediaPlayer = new MediaPlayer(hit);
//							mediaPlayer.play();
//						}else {
//							// kill player
//							/*
//							 * The player has died!
//							 * 1) display an image on the screen alerting the player that they died.
//							 * 		this is as simple as adding an imageview to the base layout.
//							 * 		remember it is a stack so you can just add to the top.
//							 * 2) wait a few seconds - maybe like 3.
//							 * 		during this time the player shouldn't be able to interract with the game
//							 * 		so set paused to true - it is a class variable of this class.
//							 * 3) remove the death image
//							 * 		remember the base layout is a stack, and the death image is the
//							 * 		last thing you pushed to it. Just remove the top item.
//							 * 		layout.children.remove(length - 1); is some psuedo code that may help
//							 * 4) reset the level.
//							 * 		to do this you will essentially need to reset the game window entirely.
//							 * 		we already have code to do this because it is done when the game window
//							 * 		is created, its all in the constructor!
//							 * 		So make a method that does pretty much exactly that and call it.
//							 * 		Seeing as this would be duplicated code, you could even move the code
//							 * 		from the constructor into a separate method itself.
//							 * 		
//							 * 		Remember, if you read the code, this area only deals with death
//							 * 		by the enemy moving on to the player. There are other causes of death
//							 * 		which are all caused by the player movement (which is obviously in the
//							 * 		player class). I reccomend you add a new class attribute isDead to the
//							 * 		player class, and whenever they die set this to true.
//							 * 		Then after this method calls player.move() it should check 
//							 * 		if(player.isDead()){
//							 * 			and then do some stuff in here (exactly what you would have done
//							 * 			for the enemy death).
//							 *		} 
//							 */
//						}
//					}
//				}
				// prevent the player from pressing a key whilst views are updated
				updatingView = true;
				
				// show the views of all of the enemies
				showViews();			
				

			}		
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
		// update the view for the player
		showPlayerView();
		
		// then deal with the enemies
		ArrayList<Character> enemies = gameState.getEnemies();
		currentEnemy = 0;
		
		// we want to display every enemy seperated by 1 seconds
		// and for each enemy we want to display their starting
		// position and ending position separated by 0.5 seconds.
		// So we need two timelines (set as class attributes).
		
		// to deal with each enemy
		Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1.0), e -> {
			
			// firstly set the grid view to the enemies position
			Character enemy = enemies.get(currentEnemy);
			gridPane = getEnemyGrid(enemy);
			borderPane.setCenter(gridPane);
			layout.getChildren().set(1, borderPane);
			
			// move the enemy
			enemy.move(gameState.getGrid());
			
			// wait 0.25 seconds and update the view again
			Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(0.5), e2 -> {
				gridPane = getEnemyGrid(enemy);
				borderPane.setCenter(gridPane);
				layout.getChildren().set(1, borderPane);
			}));
			timeline2.setCycleCount(1);
			timeline2.play();			
			currentEnemy ++;
		}));
		timeline1.setCycleCount(enemies.size());
		timeline1.play();
		
		// once all of the enemies have moved we need to go back to the players view
		Timeline timeline3 = new Timeline(new KeyFrame(Duration.seconds(1.5 * enemies.size()), e -> {
			if(multiPlayer) {
				// swap to next player
				if(currentPlayer == 1) {
					currentPlayer = 2;
				}else {
					currentPlayer = 1;
				}
				showPlayerView();
				updatingView = false;					
			}else {
				showPlayerView();
				updatingView = false;
			}
		}));
		timeline3.setCycleCount(1);
		timeline3.play();
				
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
					}else if(currentGridX == gameState.getPlayer1().getX() && currentGridY == gameState.getPlayer1().getY()){
						stack.getChildren().add(gameState.getPlayer1().getImage());
					// else try and draw player 2
					}else if(gameState.getPlayer2() != null &&
							currentGridX == gameState.getPlayer2().getX() &&
							currentGridY == gameState.getPlayer2().getY()){
						stack.getChildren().add(gameState.getPlayer2().getImage());
					
					// if not this enemy, or one of the players, check all other enemies.
					}else {
												
						boolean enemyFound = false;
						for(Character otherEnemy : gameState.getEnemies()) {
							if(otherEnemy.getX() == currentGridX && otherEnemy.getY() == currentGridY) {
								stack.getChildren().add(otherEnemy.getImage());
								enemyFound = true;
							}
						}
						
						if(!enemyFound) {
							if(cell.getItem() != null) {
								stack.getChildren().add(cell.getItemImage());
							}
						}				
					}
					pane.add(stack, gridX + 4, gridY + 4);
				} catch (ArrayIndexOutOfBoundsException e1) {
					ImageView img;
					try {
						img = new ImageView(new Image(new FileInputStream(IMG_FOLDER + "black.png")));
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
	
	

	

	

	

	
	
	
}
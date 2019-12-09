import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
/**
 * Represents the setup window which allows the user
 * to select a player/level and if they want to play multiplayer
 * @author Alan Tollett
 * @version 1.0
 */
public class SetupWindow {

	/**The folder containing the images*/
	private final String IMG_FOLDER = "src/media/img/";
	/**the connection to the database*/
	private Database db;
	/**the scene that can be added to the main stage*/
	private Scene scene;
	/**the base layout of the scene*/
	private StackPane baseLayout;
	/**Combo box containing possible levels to play*/
	private ComboBox<String> levelSelector;
	/**Combo box containing all of the existing players*/
	private ComboBox<String> player1Selector;
	/**Combo box containing all of the players again,
	 * but this one is for the second player when playing
	 * in the multiplayer mode*/
	private ComboBox<String> player2Selector;
	/**holds the image of player 1 (changes from player to player
	 * depending on what they select in the player editor)*/
	private ImageView player1Icon;
	/**holds the image of player 2 (changes from player to player
	 * depending on what they select in the player editor)*/
	private ImageView player2Icon;
	/**Holds the player selector combo boxes*/
	private HBox playerSelectors;
	/**if this is selected then the user wants to play single player*/         
	private RadioButton singlePlayer;
	/**if this is selected then the user wants to play multiplayer*/
	private RadioButton multiPlayer;
	/**timeline used to wait for the user to select yes or no
	 *  when prompted with using a new or old saved game*/
	private Timeline waiting;

	/**
	 * Constructs a setup window object.
	 */
	public SetupWindow() {				
		try {
			db = new Database("jdbc:mysql://localhost:3306"                    
					+ "/Reaching_Insanity", "root", "");
			// the base layout
			baseLayout = new StackPane();

			// background
			ImageView background = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "background.png")));

			baseLayout.getChildren().add(background);


			// main contents
			VBox contents = new VBox(20);
			contents.setAlignment(Pos.CENTER);

			ImageView title = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "setup_title.png")));			

			Button back = new Button("Back to Main Menu"); //Back button
			back.setOnAction(e -> {
				backToMain(e);
			});

			Button play = new Button("Play Game");
			play.setOnAction(e -> {
				playGame(e);
			});

			levelSelector = new ComboBox<String>();
			levelSelector.setEditable(false);
			levelSelector.setPrefHeight(30);
			levelSelector.setPrefWidth(150);
			levelSelector.setPromptText("Select a Level");

			playerSelectors = new HBox(50);
			playerSelectors.setAlignment(Pos.CENTER);
			playerSelectors.getChildren().addAll(getPlayerSelector(1));

			contents.getChildren().addAll(title, getSingleOrMulti(),
					playerSelectors, levelSelector, play, back);

			baseLayout.getChildren().add(contents);

			scene = new Scene(baseLayout, 1200, 700);
			scene.getStylesheets().add("style.css");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e2) {
			System.out.println("ERROR: SQL.");
			e2.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Get the scene representing the set up window
	 * @return the scene that can be added 
	 */
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Deals with the play game button being pressed
	 * @param e the event that triggers the play game action
	 */
	private void playGame(ActionEvent e) {
		Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		stage.setTitle("Reaching Insanity");
		
		if (singlePlayer.isSelected()) {
			if (player1Selector.getValue() != null 
					&& levelSelector.getValue() != null) {
					int level = Integer.parseInt(levelSelector.getValue().
						substring(levelSelector.getValue().length() - 1));

				boolean previouslySaved = false;
				File player1Folder = new File("src/SavedGames/" + 
						player1Selector.getValue());
				if (player1Folder.isDirectory()) {
					for (File f : player1Folder.listFiles()) {
						if (f.getName().startsWith(levelSelector.
								getValue())) {
							previouslySaved = true;
						}
					}
				}
				
				if (previouslySaved) {
					PopUpBool popUp = new PopUpBool("You have a previous save "
							+ "for this level, would you like to load it?");
					
					waiting = new Timeline(new KeyFrame(
							Duration.millis(100), e2 -> {
							if (popUp.getResult()) {
								File f = new File("src/SavedGames/" + 
										player1Selector.getValue() + 
										"/Level " + level + ".txt");
								GameWindow gameWindow = new GameWindow(
										player1Selector.getValue(), null, f);
								stage.setScene(gameWindow.getScene());
								waiting.stop();
							} else if (!popUp.getWaiting()) {
								File f = new File("src/levels/"
										+ "Level " + level + ".txt");
								GameWindow gameWindow = new GameWindow(
										player1Selector.getValue(), null, f);
								stage.setScene(gameWindow.getScene());
								waiting.stop();
							}
					}));
					waiting.setCycleCount(Animation.INDEFINITE);
					waiting.play();
				} else {
					File f = new File("src/levels/Level " + level + ".txt");
					GameWindow gameWindow = new GameWindow(
							player1Selector.getValue(), null, f);
					stage.setScene(gameWindow.getScene());
				}						
			} else {
				new PopUp("ERROR: You must select a player and a level",true);
			}
		} else {
			if (player1Selector.getValue() != null 
					&& player2Selector.getValue() != null 
					&& (!player1Selector.getValue()
							.equals(player2Selector.getValue()))) {
				File f = new File("src/levels/Level 100.txt");
				GameWindow gameWindow = new GameWindow(
						player1Selector.getValue(), 
						player2Selector.getValue(), f);
				stage.setScene(gameWindow.getScene());
			} else {
				new PopUp("ERROR: You must select two "
						+ "different players.", false);
			}
		}
	}
	
	/**
	 * Gets the vbox holding contents related to a player.
	 * @param playerNumber the player who's info you want to display
	 * @return the vbox holding the data to be added to the screen
	 */
	private VBox getPlayerSelector(int playerNumber) {
		VBox contents = new VBox(20);
		contents.setAlignment(Pos.CENTER);

		ObservableList<Player> players = getPlayers();
		ObservableList<String> playerNames = 
				FXCollections.observableArrayList();
		for (Player p : players) {
			playerNames.add(p.getName());
		}

		ComboBox<String> existingPlayers = new ComboBox<String>();
		existingPlayers.setItems(playerNames);
		existingPlayers.setEditable(false);
		existingPlayers.setPrefHeight(30);
		existingPlayers.setPrefWidth(150);
		existingPlayers.setPromptText("Select a Player");

		try {
			if (playerNumber == 1) {
				player1Icon = new ImageView(new Image(
						new FileInputStream(IMG_FOLDER + "grid/empty.png")));
				player1Selector = existingPlayers;
				contents.getChildren().addAll(existingPlayers, player1Icon);
			} else {
				player2Icon = new ImageView(new Image(new FileInputStream(
						IMG_FOLDER + "grid/empty.png")));
				player2Selector = existingPlayers;
				contents.getChildren().addAll(existingPlayers, player2Icon);
			}
		} catch(FileNotFoundException e) {
			System.out.println("ERROR: getting player image");
			e.printStackTrace();
			System.exit(-1);
		}

		existingPlayers.setOnAction(e -> {
			try {
				ResultSet rs = db.query("SELECT image_id, highest_level "
						+ "FROM player WHERE name=\"" + 
						existingPlayers.getValue() + "\"");
				rs.next(); // gets us to the first tuple, otherwise null

				int highestLevel = rs.getInt("highest_level");
				displayLevels(highestLevel);

				if (playerNumber == 1) {
					player1Icon.setImage(new Image(new FileInputStream(
							IMG_FOLDER + "grid/player" + 
							rs.getInt("image_id") + "_down.png")));
				} else {
					player2Icon.setImage(new Image(new FileInputStream(
							IMG_FOLDER + "grid/player" + 
							rs.getInt("image_id") + "_down.png")));
				}
			} catch (FileNotFoundException | SQLException e1) {
				System.out.println("ERROR: getting player image");
				e1.printStackTrace();
				System.exit(-1);
			}
		});
		return contents;
	}

	/**
	 * Gets a list of levels to display depending on the players highest level
	 * @param highestLevel the players highest level
	 */
	private void displayLevels(int highestLevel) {
		if (highestLevel == 0) {
			highestLevel = 1;
		}

		ObservableList<String> levels = FXCollections.observableArrayList();		
		for (int i = 0; i < highestLevel; i++) {
			levels.add("Level " + (i + 1));
		}

		levelSelector.setItems(levels);
	}


	/**
	 * Gets the node that can be added to the screen containing the
	 * radio buttons that allow the player to chose between single
	 * player and multiplayer
	 * @return the container 
	 */
	private HBox getSingleOrMulti() {
		HBox singleOrMulti = new HBox(75);
		singleOrMulti.setAlignment(Pos.CENTER);

		ToggleGroup singleOrMultiRadio = new ToggleGroup();

		singlePlayer = new RadioButton("Single Player");
		singlePlayer.setToggleGroup(singleOrMultiRadio);
		singlePlayer.setSelected(true);
		singlePlayer.setOnAction(e -> {
			if (playerSelectors.getChildren().size() == 2) {
				playerSelectors.getChildren().remove(1);
				levelSelector.setVisible(true);
			}
		});

		multiPlayer = new RadioButton("Multi-Player");
		multiPlayer.setToggleGroup(singleOrMultiRadio);
		multiPlayer.setOnAction(e -> {
			if (playerSelectors.getChildren().size() == 1) {
				playerSelectors.getChildren().add(getPlayerSelector(2));
				levelSelector.setVisible(false);
			}
		});

		singleOrMulti.getChildren().addAll(singlePlayer, multiPlayer);
		return singleOrMulti;
	}

	/**
	 * Takes the user back to the main screen
	 * @param e the event that triggered the method call
	 */
	private void backToMain(ActionEvent e) {
		Stage newWindow = (Stage) ((Node) e.getSource())
				.getScene().getWindow();
		newWindow.setScene(Main.getScene());
		newWindow.show();
	}

	/**
	 * gets a list containing the existing players that can
	 *  be added to the combo box
	 * @return the list of player names
	 */
	private ObservableList<Player> getPlayers(){
		ObservableList<Player> players = FXCollections.observableArrayList();
		try {			
			ResultSet result = db.query("SELECT name, image_id FROM player");
			while(result.next()) {
				String name = result.getString("name");
				int imageID = result.getInt("image_id");
				players.add(new Player(name, null, 0, imageID));
			}

		} catch (SQLException e) {
			System.out.println("ERROR: SQL");
			e.printStackTrace();
			System.exit(-1);
		}
		return players;
	}
}

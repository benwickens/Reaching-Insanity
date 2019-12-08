
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @version 1.0
 * @author Alan Tollett
 * <br>
 * Purpose:
 * Allow users to create, select or edit a player.
 */

public class PlayerEditor {


	/**
	 * Holds the location to the image folder
	 */
	private final String IMG_FOLDER = "src/media/img/";
	/**
	 * Holds the Database
	 */
	private Database db;
	/**
	 * Holds a scene
	 */
	private Scene scene;
	/**
	 * Base layout made from a StackPane
	 */
	private StackPane baseLayout;
	/**
	 * VBox that holds contents
	 */
	private VBox contents;
	/**
	 * HBox for new or old players
	 */
	private HBox newOrOld;
	/**
	 * Integer to hold the current player number
	 */
	private int currentPlayerNum;
	/**
	 * String for existing players
	 */
	private ComboBox<String> existingPlayers;
	/**
	 * HBox for the users name
	 */
	private HBox nameBox;
	/**
	 * Label for the name
	 */
	private Label nameLabel;
	/**
	 * Text field for the name
	 */
	private TextField name;
	/**
	 * Image of the avatars
	 */
	private ImageView playerIcon;
	/**
	 * next button for avatars
	 */
	private Button nextButton;
	/**
	 * continue button for the game
	 */
	private Button continueButton;
	/**
	 * back button to go to the main menu
	 */
	private Button backButton;
	/**
	 * delete button for a player
	 */
	private Button deleteButton;
	/**
	 * boolean for whether a player exists
	 */
	private boolean existingPlayer;

	/**
	 * The try block instantiates class variables to the expected first values.
	 * Exceptions are caught if the image isn't found or if there is are SQL errors.
	 */
	public PlayerEditor() {
		try {
			//
			db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity",
					"root", "");
			currentPlayerNum = 1;

			loadExistingPlayers();
			loadNameBox();
			loadPlayerIcon();
			loadNextButton();
			loadContinueButton();
			loadBackButton();
			loadDeleteButton();

			// basic layout stuff
			baseLayout = new StackPane();
			ImageView background = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "background.png")));
			baseLayout.getChildren().add(background);

			// main contents
			contents = new VBox(20);
			contents.setAlignment(Pos.CENTER);

			// firstly title
			ImageView title = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "editor_title.png")));
			contents.getChildren().add(title);

			// then the new or old buttons (and back)
			displayNewOrOld();

			baseLayout.getChildren().add(contents);

			scene = new Scene(baseLayout, 1200, 700);
            background.fitHeightProperty().bind(scene.heightProperty());
            background.fitWidthProperty().bind(scene.widthProperty());
			scene.getStylesheets().add("style.css");

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to load image(s).");
			e.printStackTrace();
			System.exit(-1);
		} catch (SQLException e2) {
			System.out.println("ERROR: SQL.");
			e2.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * @return scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Loads a HBox that the user enters their name into.
	 * Exception to handle if the user cannot connect to the database.
	 */
	private void loadNameBox() {
		nameBox = new HBox(25);
		nameBox.setAlignment(Pos.CENTER);

		nameLabel = new Label("Player Name: ");

		name = new TextField();
		name.setPromptText("Enter a Name");
		name.setOnKeyReleased(e ->{
			if(name.getText().length() >= 4) {
				try {
					ResultSet r = db.query("SELECT * FROM player WHERE name=\"" +
							name.getText() + "\"");
					if(r.next()) {
						continueButton.setId("danger");
					}else {
						continueButton.setId("safe");
					}
				} catch (SQLException e1) {
					new PopUp("ERROR: You cannot connect to the database.",
							false);
					e1.printStackTrace();
				}
			}else {
				continueButton.setId("danger");
			}
		});
		nameBox.getChildren().addAll(nameLabel, name);
	}

	/**
	 * Loads an image that the user can select as a avatar.
	 * Exception to handle if the file cannot be found.
	 */
	private void loadPlayerIcon() {
		try {
			playerIcon = new ImageView(new Image(new FileInputStream
					(IMG_FOLDER + "/grid/player1_down.png")));
			playerIcon.setScaleX(2);
			playerIcon.setScaleY(2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the next image that the user can select as a avatar.
	 * Exception to handle if the file cannot be found.
	 */
	private void loadNextButton() {
		nextButton = new Button("Next");
		nextButton.setOnAction(e ->{
			try {
				currentPlayerNum ++;
				if(currentPlayerNum > 7) {
					currentPlayerNum = 1;
				}
				playerIcon.setImage(new Image(new FileInputStream(
						IMG_FOLDER + "/grid/player" + currentPlayerNum +
								"_down.png")));
			} catch (FileNotFoundException e1) {
				System.out.println("ERROR: cannot load next image.");
				e1.printStackTrace();
			}
		});
	}

	/**
	 * Loads a back button used to get to the main menu.
	 * Exception to handle if the file cannot be found.
	 */
	private void loadBackButton() {
		backButton = new Button("Back to Main Menu"); //Back button
		backButton.setOnAction(e -> {
			backToMain(e);
		});
	}

	/**
	 * Displays New or Existing player(s) depending on what button is clicked.
	 */
	private void displayNewOrOld() {
		newOrOld = new HBox(50);
		newOrOld.setAlignment(Pos.CENTER);

		// when either button is clicked, remove newOrOld and replace
		// with respective layout
		Button newPlayerButton = new Button("New Player");
		newPlayerButton.setOnAction(e -> {
			contents.getChildren().remove(newOrOld);
			displayNewPlayerContent();
			existingPlayer = false;
		});

		Button oldPlayerButton = new Button("Existing Player");
		oldPlayerButton.setOnAction(e -> {
			contents.getChildren().remove(newOrOld);
			displayOldPlayerContent();
			existingPlayer = true;
		});

		newOrOld.getChildren().addAll(newPlayerButton, oldPlayerButton,
				backButton);
		contents.getChildren().add(newOrOld);
	}

	/**
	 * Takes the user back to the main menu.
	 * @param e the exception.
	 */
	private void backToMain(ActionEvent e) {
		try {
			Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
			Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
			newWindow.setScene(new Scene(loadIn, 1200, 700));
			newWindow.show();
		} catch (IOException e1) {e1.printStackTrace();}
	}

	/**
	 * Displays the new Player
	 */
	private void displayNewPlayerContent() {
		VBox box = new VBox(50);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(nameBox, playerIcon, nextButton,
				continueButton, backButton);
		box.getChildren().get(3).setId("danger");
		contents.getChildren().add(box);
	}

	/**
	 * Displays the old player
	 */
	private void displayOldPlayerContent() {
		VBox box = new VBox(30);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(existingPlayers, nameBox, playerIcon,
				nextButton, continueButton, backButton, deleteButton);
		box.getChildren().get(4).setId("safe");
		contents.getChildren().add(box);
	}

	/**
	 * Adds players to an arraylist.
	 * Catches an SQL exception
	 * @return players, an array of all players.
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

	/**
	 * Deletes a player from the game.
	 */
	private void loadDeleteButton() {
		deleteButton = new Button("Delete Player");
		deleteButton.setId("danger");
		deleteButton.setOnAction(e ->{
			// if sure...
			if(existingPlayers.getValue() == null) {
				new PopUp("ERROR: You have not selected a player!", false);
			}else {
				// if yes/no dialog returns true
				db.manipulate("DELETE FROM leaderboard WHERE name=\"" +
						existingPlayers.getValue() + "\"");
				db.manipulate("DELETE FROM player WHERE name=\"" +
						existingPlayers.getValue() + "\"");
				new PopUp("The player has been deleted.", true);
				backToMain(e);
			}
		});
	}

	/**
	 * Allows the user to continue playing.
	 */
	private void loadContinueButton() {
		continueButton = new Button("Confirm");
		continueButton.setOnAction(e ->{
			// if the entered name is >= 4 characters long
			if(name.getText() != null && name.getLength() >= 4) {
				// if player already exists then we just update their icon
				if(existingPlayer) {
					db.manipulate("UPDATE player SET image_id=" +
							currentPlayerNum + " WHERE name='" +
							name.getText() + "'");
					new PopUp("You have successfully updated the player!",
							true);
					backToMain(e);
				}
				// otherwise we create a new entry
				else {
					// but only if the name doesn't already exist
					if(!existingPlayers.getItems().contains(name.getText())) {
						db.manipulate("INSERT INTO player VALUES ('" +
								name.getText() + "', 0, " + currentPlayerNum + ")");
						new PopUp("You have successfully created a new player!"
								, true);
						backToMain(e);
					}else {
						new PopUp("ERROR: That name is already taken!", false);
					}
				}
			}else {
				new PopUp("ERROR: Name must have 4 or more characters!",
						false);
			}
		});
	}

	/**
	 * Loads all of the existing players when a user starts the game.
	 */
	private void loadExistingPlayers(){
		existingPlayers = new ComboBox<String>();
		ObservableList<Player> players = getPlayers();
		ObservableList<String> playerNames = FXCollections.observableArrayList();
		for(Player p : players) {
			playerNames.add(p.getName());
		}
		existingPlayers.setItems(playerNames);
		existingPlayers.setEditable(false);
		existingPlayers.setPrefHeight(30);
		existingPlayers.setPrefWidth(150);
		existingPlayers.setOnAction(e -> {
			name.setText(existingPlayers.getValue());
			name.setEditable(false);

			// update the player icon
			ResultSet rs = db.query("SELECT image_id FROM player "
					+ "WHERE name='" + existingPlayers.getValue() + "'");

			try {
				rs.next();
				playerIcon.setImage(new Image(new FileInputStream(
						IMG_FOLDER + "/grid/player" + rs.getInt("image_id") + "_down.png")));
			} catch (FileNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		});
	}

}

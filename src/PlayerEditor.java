
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.security.auth.callback.ConfirmationCallback;

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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerEditor {
	
	private final String IMG_FOLDER = "src/media/img/";
	private Database db;
	private Scene scene;
	private StackPane baseLayout;
	private VBox contents;
	
	private HBox newOrOld;
	private int currentPlayerNum;
	
	
	private ComboBox<String> existingPlayers;
	private HBox nameBox;
	private Label nameLabel;
	private TextField name;
	private ImageView playerIcon;
	private Button nextButton;	
	private Button continueButton;
	private Button backButton;
	private Button deleteButton;
	
	private boolean existingPlayer;
	
	public PlayerEditor() {				
		try {
			// instantiate clas variables to expected first values
			db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", "root", "");
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
					new PopUp("ERROR: You cannot connect to the database.", false);
					e1.printStackTrace();
				}
			}else {
				continueButton.setId("danger");
			}
		});
		nameBox.getChildren().addAll(nameLabel, name);		
	}
	
	private void loadPlayerIcon() {
		try {
			playerIcon = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "/grid/player1_down.png")));
			playerIcon.setScaleX(2);
			playerIcon.setScaleY(2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void loadNextButton() {
		nextButton = new Button("Next");
		nextButton.setOnAction(e ->{
			try {
				currentPlayerNum ++;
				if(currentPlayerNum > 7) {
					currentPlayerNum = 1;
				}
				playerIcon.setImage(new Image(new FileInputStream(
						IMG_FOLDER + "/grid/player" + currentPlayerNum + "_down.png")));
			} catch (FileNotFoundException e1) {
				System.out.println("ERROR: cannot load next image.");
				e1.printStackTrace();
			}
		});
	}

	private void loadBackButton() {
		backButton = new Button("Back to Main Menu"); //Back button
		backButton.setOnAction(e -> {
			backToMain(e);
		});
	}
	
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
		
		newOrOld.getChildren().addAll(newPlayerButton, oldPlayerButton, backButton);
		contents.getChildren().add(newOrOld);
	}
	
	private void backToMain(ActionEvent e) {
		Stage newWindow = (Stage) ((Node) e.getSource())
				.getScene().getWindow();
		newWindow.setScene(Main.getScene());
		newWindow.show();
	}

	
	private void displayNewPlayerContent() {
		VBox box = new VBox(50);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(nameBox, playerIcon, nextButton, continueButton, backButton);
		box.getChildren().get(3).setId("danger");
		contents.getChildren().add(box);
	}

	private void displayOldPlayerContent() {
		VBox box = new VBox(30);
		box.setAlignment(Pos.CENTER);
		box.getChildren().addAll(existingPlayers, nameBox, playerIcon, nextButton, continueButton, backButton, deleteButton);
		box.getChildren().get(4).setId("safe");
		contents.getChildren().add(box);
	}
	
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
	
	private void loadDeleteButton() {
		deleteButton = new Button("Delete Player");
		deleteButton.setId("danger");
		deleteButton.setOnAction(e ->{
			// if sure...
			if(existingPlayers.getValue() == null) {
				new PopUp("ERROR: You have not selected a player!", false);
			}else {
				// if yes/no dialog returns true
				db.manipulate("DELETE FROM leaderboard WHERE name=\"" + existingPlayers.getValue() + "\"");
				db.manipulate("DELETE FROM player WHERE name=\"" + existingPlayers.getValue() + "\"");
				new PopUp("The player has been deleted.", true);
				backToMain(e);
			}
		});
	}
	

	
	private void loadContinueButton() {
		continueButton = new Button("Confirm");
		continueButton.setOnAction(e ->{
			// if the entered name is >= 4 characters long
			if(name.getText() != null && name.getLength() >= 4) {
				// if player already exists then we just update their icon
				if(existingPlayer) {
					db.manipulate("UPDATE player SET image_id=" + currentPlayerNum + " WHERE name='" + name.getText() + "'");
					new PopUp("You have successfully updated the player!", true);
					backToMain(e);
				}
				// otherwise we create a new entry
				else {
					// but only if the name doesn't already exist
					if(!existingPlayers.getItems().contains(name.getText())) {
						db.manipulate("INSERT INTO player VALUES ('" + name.getText() + "', 0, " + currentPlayerNum + ")");
						new PopUp("You have successfully created a new player!", true);
						backToMain(e);
					}else {
						new PopUp("ERROR: That name is already taken!", false);
					}
				}
			}else {
				new PopUp("ERROR: Name must have 4 or more characters!", false);
			}
		});
	}
	
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
	
	
	public Scene getScene() {
		return scene;
	}

}

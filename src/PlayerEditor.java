
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
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PlayerEditor {
	
	private final String IMG_FOLDER = "src/media/img/";
	private Database db;
	private Scene scene;
	private StackPane baseLayout;
	private VBox contents;
	private ComboBox<String> existingPlayers;
	private HBox newOrOld;
	private ImageView playerIcon;
	private int currentPlayerNum;
	
	public PlayerEditor() {				
		try {
			db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", "root", "");
			currentPlayerNum = 1;
			
			// the base layout
			baseLayout = new StackPane();
			
			// background
			ImageView background = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "background.png")));

			baseLayout.getChildren().add(background);
			
			// main contents
			contents = new VBox(20);
			contents.setAlignment(Pos.CENTER);
			
			ImageView title = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "editor_title.png")));
			contents.getChildren().add(title);
			
			/*
			 * New or old player buttons, once clicked are removed from view
			 * and the correct view is made visible.
			 */
			newOrOld = new HBox(50);
			newOrOld.setAlignment(Pos.CENTER);
			Button newPlayerButton = new Button("New Player");
			newPlayerButton.setOnAction(e -> {
				contents.getChildren().remove(newOrOld);
				displayNewPlayerContent();
			});
			
			Button oldPlayerButton = new Button("Existing Player");
			oldPlayerButton.setOnAction(e -> {
				contents.getChildren().remove(newOrOld);
				displayOldPlayerContent();
			});
			
			newOrOld.getChildren().addAll(newPlayerButton, oldPlayerButton);
			
			Button back = new Button("Back to Main Menu"); //Back button
			back.setOnAction(e -> {
				backToMain(e);
			});
			
			contents.getChildren().addAll(newOrOld, back);
			
//			// add logo and title
//			VBox titles = new VBox(10);
//			titles.setAlignment(Pos.CENTER);
//			titles.getChildren().addAll(title);
//			
//			HBox playerSelect = new HBox(25);
//			playerSelect.setAlignment(Pos.CENTER);
//			Label oldPlayer = new Label("Select a Player:");
//			

//			
//			playerSelect.getChildren().addAll(oldPlayer, existingPlayers);
//			
//			
//			
//			Button delete = new Button("Delete Player");
//			delete.setOnAction(e -> {
//				deletePlayer(existingPlayers.getValue());
//			});
//			
//
//			contents.getChildren().addAll(titles, playerSelect, delete, back);
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
			messageSQL();
			System.out.println("ERROR: SQL.");
			e2.printStackTrace();
			System.exit(-1);
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
	
	private void displayNewPlayerContent() {
		try {
			VBox box = new VBox(50);
			box.setAlignment(Pos.CENTER);
			
			HBox nameBox = new HBox(25);
			nameBox.setAlignment(Pos.CENTER);
			Label nameLabel = new Label("Player Name: ");
			TextField name = new TextField();
			name.setPromptText("Enter a Name");
			nameBox.getChildren().addAll(nameLabel, name);
			
			playerIcon = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "/grid/player1_down.png")));
			playerIcon.setScaleX(2);
			playerIcon.setScaleY(2);
			
			Button nextImageButton = new Button("Next");
			nextImageButton.setOnAction(e ->{
				try {
					currentPlayerNum ++;
					if(currentPlayerNum > 2) {
						currentPlayerNum = 1;
					}
					playerIcon.setImage(new Image(
							new FileInputStream(IMG_FOLDER + "/grid/player" + currentPlayerNum
									+ "_down.png")));
				} catch (FileNotFoundException e1) {
					System.out.println("ERROR: images(s) not found.");
					e1.printStackTrace();
					System.exit(-1);
				}
			});
			
			Button continueButton = new Button("Continue");
			continueButton.setOnAction(e ->{
				boolean b;
				if(name.getText() != null && name.getLength() >= 1) {
					try {
						ResultSet r = db.query("SELECT * FROM player WHERE name=\"" + 
								name.getText() + "\"");
						if(r.next()) {
							// show error - name already taken
//							PopUp a = new PopUp("ERROR: name already taken",true);
//							Stage stageP = new Stage();
//							stageP.setScene(a.getScene());
//							stageP.show();
						}else {
							// name is fine, continue
							db.manipulate("INSERT INTO player VALUES (\"" + 
									name.getText() + "\", 0, " + currentPlayerNum + ")");
							// show success
//							PopUp a = new PopUp("success",false);
//							Stage stageP = new Stage();
//							stageP.setScene(a.getScene());
//							stageP.show();
							backToMain(e);
						}
					}catch(SQLException e2) {
						messageSQL();
						System.out.println("ERROR: SQL");
						e2.printStackTrace();
						System.exit(-1);
					}
				}
			});
			
			box.getChildren().addAll(nameBox, playerIcon, nextImageButton, continueButton);
			contents.getChildren().add(box);
			
		}catch(FileNotFoundException e) {
//			PopUp a = new PopUp("ERROR: Failed to load image(s).",false);
//			Stage stageP = new Stage();
//			stageP.setScene(a.getScene());
//			stageP.show();
			System.out.println("ERROR: Failed to load image(s).");
			e.printStackTrace();
		}		
	}

	private void displayOldPlayerContent() {
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
		
		contents.getChildren().add(existingPlayers);
		displayNewPlayerContent();
		// add delete button
	}
	
	private void deletePlayer(String name) {
		db.manipulate("DELETE FROM player WHERE name=\"" + name + "\"");
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
			messageSQL();
			System.out.println("ERROR: SQL");
			e.printStackTrace();
			System.exit(-1);
		}
		
		return players;
	}
	private void messageSQL(){
//		PopUp a = new PopUp("ERROR: SQL",true);
//		Stage stageP = new Stage();
//		stageP.setScene(a.getScene());
//		stageP.show();
	}

	public Scene getScene() {
		return scene;
	}

}


import java.io.File;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SetupWindow {
	
	private final String IMG_FOLDER = "src/media/img/";
	private Database db;
	private Scene scene;
	private StackPane baseLayout;
	private VBox contents;
	private ComboBox<String> existingPlayers;
	private ComboBox<String> levelSelector;
	private HBox singleOrMulti;
	private ImageView player1Icon;
	private ImageView player2Icon;
	
	public SetupWindow() {				
		try {
			db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", "root", "");
			// the base layout
			baseLayout = new StackPane();
			
			// background
			ImageView background = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "background.png")));
			
			baseLayout.getChildren().add(background);
			
			// set default player image
			player1Icon = new ImageView(new Image(new FileInputStream(
					IMG_FOLDER + "grid/player1_down.png")));
			
			// main contents
			contents = new VBox(20);
			contents.setAlignment(Pos.CENTER);
			
			ImageView title = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "setup_title.png")));
			contents.getChildren().add(title);
			
			/*
			 * Single Player or Multi-Player Selection
			 */
			singleOrMulti = new HBox(50);
			singleOrMulti.setAlignment(Pos.CENTER);
			
			ToggleGroup singleOrMultiRadio = new ToggleGroup();
			RadioButton singlePlayer = new RadioButton("Single Player");
			singlePlayer.setSelected(true);
			RadioButton multiPlayer = new RadioButton("Multi-Player");
			singlePlayer.setToggleGroup(singleOrMultiRadio);
			multiPlayer.setToggleGroup(singleOrMultiRadio);
			
			singleOrMulti.getChildren().addAll(singlePlayer, multiPlayer);
			
			/*
			 * USER SELECTOR
			 */
			HBox singleMultiMain = new HBox(50);
			singleMultiMain.setAlignment(Pos.CENTER);
			
			VBox singlePlayerBox = new VBox(20);
			singlePlayerBox.setAlignment(Pos.CENTER);
			
			ObservableList<Player> players = getPlayers();
			ObservableList<String> playerNames = FXCollections.observableArrayList();
			for(Player p : players) {
				playerNames.add(p.getName());
			}
			existingPlayers = new ComboBox<String>();
			existingPlayers.setItems(playerNames);
			existingPlayers.setEditable(false);
			existingPlayers.setPrefHeight(30);
			existingPlayers.setPrefWidth(150);
			
			existingPlayers.setOnAction(e -> {
				try {
					ResultSet rs = db.query("SELECT image_id FROM player WHERE name=\"" + 
							existingPlayers.getValue() + "\"");
					rs.next();
					player1Icon.setImage(new Image(new FileInputStream(
							IMG_FOLDER + "grid/player" + rs.getInt("image_id") + "_down.png")));
				} catch (FileNotFoundException | SQLException e1) {
					System.out.println("ERROR: getting player image");
					e1.printStackTrace();
					System.exit(-1);
				}
			});
			
			ObservableList<String> levels = FXCollections.observableArrayList();
			File levelFolder = new File("src/levels/");
			for(File level : levelFolder.listFiles()) {
				levels.add(level.getName().replace(".txt", ""));
			}
			levelSelector = new ComboBox<String>();
			levelSelector.setItems(levels);
			levelSelector.setEditable(false);
			levelSelector.setPrefHeight(30);
			levelSelector.setPrefWidth(150);
			
			
			
			singlePlayerBox.getChildren().addAll(existingPlayers, player1Icon, levelSelector);
			
			singleMultiMain.getChildren().add(singlePlayerBox);
			
			
			Button back = new Button("Back to Main Menu"); //Back button
			back.setOnAction(e -> {
				backToMain(e);
			});
			
			Button play = new Button("Play Game");
			play.setOnAction(e -> {
		        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		        stage.setTitle("Reaching Insanity");
		        GameWindow gameWindow = new GameWindow(
		        		existingPlayers.getValue(),
		        		new File("src/levels/" + levelSelector.getValue() + ".txt"));
		        stage.setScene(gameWindow.getScene());
			});
			
			contents.getChildren().addAll(singleOrMulti, singleMultiMain, play, back);
			
			baseLayout.getChildren().add(contents);
			
			scene = new Scene(baseLayout, 1200, 700);
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
	
	private void comboAction(ActionEvent e) {
		
	}
	
	private void backToMain(ActionEvent e) {
		try {
			Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
			Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
			newWindow.setScene(new Scene(loadIn, 1200, 700));
			newWindow.show();
		} catch (IOException e1) {e1.printStackTrace();}
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
	
	public Scene getScene() {
		return scene;
	}

}

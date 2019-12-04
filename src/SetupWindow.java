
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
	private ComboBox<String> levelSelector;
	private ComboBox<String> player1Selector;
	private ComboBox<String> player2Selector;
	private int player1Highest;
	private int player2Highest;
	private ImageView player1Icon;
	private ImageView player2Icon;
	private HBox playerSelectors;
	private RadioButton singlePlayer;
	private RadioButton multiPlayer;
	
	public SetupWindow() {				
		try {
			db = new Database("jdbc:mysql://localhost:3306/Reaching_Insanity", "root", "");
			// the base layout
			baseLayout = new StackPane();
			
			// background
			ImageView background = new ImageView(new Image(
					new FileInputStream(IMG_FOLDER + "background.png")));
			
			baseLayout.getChildren().add(background);
			
						
			// main contents
			VBox contents = new VBox(20);
			contents.setAlignment(Pos.CENTER);
			
			ImageView title = new ImageView(new Image(new FileInputStream(IMG_FOLDER + "setup_title.png")));			
			
			Button back = new Button("Back to Main Menu"); //Back button
			back.setOnAction(e -> {
				backToMain(e);
			});
			
			Button play = new Button("Play Game");
			play.setOnAction(e -> {
				if(player1Selector.getValue() != null) {
			        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			        stage.setTitle("Reaching Insanity");
					
			        if(levelSelector.getValue() != null) {
						 if(singlePlayer.isSelected()) {
						        GameWindow gameWindow = new GameWindow(
						        		player1Selector.getValue(),
						        		null,
						        		Integer.parseInt(levelSelector.getValue().substring(levelSelector.getValue().length() - 1)));
						        stage.setScene(gameWindow.getScene());
						 }else {
							 if(player2Selector.getValue() != null && !player1Selector.getValue().equals(player2Selector.getValue())) {
							     GameWindow gameWindow = new GameWindow(
							        		player1Selector.getValue(),
							        		player2Selector.getValue(),
							        		Integer.parseInt(levelSelector.getValue().substring(levelSelector.getValue().length() - 1)));
							     stage.setScene(gameWindow.getScene());
							 }else {
								 System.out.println("ERROR: Must select two different players."); // replace with pop up window
							 }
						 }	
			        }else {
			        	System.out.println("ERROR: Must select a level."); // replace with pop up window
			        }
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
			levelSelector.setPromptText("Select a Level");
			
			playerSelectors = new HBox(50);
			playerSelectors.setAlignment(Pos.CENTER);
			playerSelectors.getChildren().addAll(getPlayerSelector(1));
			
			contents.getChildren().addAll(title, getSingleOrMulti(), playerSelectors, levelSelector, play, back);
			
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
	
	private VBox getPlayerSelector(int playerNumber) {
		VBox contents = new VBox(20);
		contents.setAlignment(Pos.CENTER);
		
		ObservableList<Player> players = getPlayers();
		ObservableList<String> playerNames = FXCollections.observableArrayList();
		for(Player p : players) {
			playerNames.add(p.getName());
		}
		
		
		ComboBox<String> existingPlayers = new ComboBox<String>();
		existingPlayers.setItems(playerNames);
		existingPlayers.setEditable(false);
		existingPlayers.setPrefHeight(30);
		existingPlayers.setPrefWidth(150);
		existingPlayers.setPromptText("Select a Player");
				
		try {
			if(playerNumber == 1) {
				player1Icon = new ImageView(new Image(new FileInputStream(IMG_FOLDER + "grid/empty.png")));
				player1Selector = existingPlayers;
				contents.getChildren().addAll(existingPlayers, player1Icon);
			}else {
				player2Icon = new ImageView(new Image(new FileInputStream(IMG_FOLDER + "grid/empty.png")));
				player2Selector = existingPlayers;
				contents.getChildren().addAll(existingPlayers, player2Icon);
			}
		}catch(FileNotFoundException e) {
			System.out.println("ERROR: getting player image");
			e.printStackTrace();
			System.exit(-1);
		}
		
		existingPlayers.setOnAction(e -> {
			try {
				ResultSet rs = db.query("SELECT image_id, highest_level FROM player WHERE name=\"" + existingPlayers.getValue() + "\"");
				rs.next(); // gets us to the first tuple, otherwise null
				
				if(playerNumber == 1) {
					System.out.println("PLAYER 1: " + rs.getInt("image_id"));
					player1Icon.setImage(new Image(new FileInputStream(IMG_FOLDER + "grid/player" + rs.getInt("image_id") + "_down.png")));
				}else {
					System.out.println("PLAYER 2: " + rs.getInt("image_id"));
					player2Icon.setImage(new Image(new FileInputStream(IMG_FOLDER + "grid/player" + rs.getInt("image_id") + "_down.png")));
				}
			} catch (FileNotFoundException | SQLException e1) {
				System.out.println("ERROR: getting player image");
				e1.printStackTrace();
				System.exit(-1);
			}
		});
		return contents;
	}
	

	private HBox getSingleOrMulti() {
		HBox singleOrMulti = new HBox(75);
		singleOrMulti.setAlignment(Pos.CENTER);
		
		ToggleGroup singleOrMultiRadio = new ToggleGroup();
		
		singlePlayer = new RadioButton("Single Player");
		singlePlayer.setToggleGroup(singleOrMultiRadio);
		singlePlayer.setSelected(true);
		singlePlayer.setOnAction(e -> {
			if(playerSelectors.getChildren().size() == 2) {
				playerSelectors.getChildren().remove(1);
			}
		});
		
		multiPlayer = new RadioButton("Multi-Player");
		multiPlayer.setToggleGroup(singleOrMultiRadio);
		multiPlayer.setOnAction(e -> {
			if(playerSelectors.getChildren().size() == 1) {
				playerSelectors.getChildren().add(getPlayerSelector(2));
			}
		});
		
		singleOrMulti.getChildren().addAll(singlePlayer, multiPlayer);
		return singleOrMulti;
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
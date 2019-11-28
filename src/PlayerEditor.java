
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlayerEditor {
	
	private final String LOGO_PATH = "src/media/img/logo.png";
	private final String PLAYER_EDITOR_FOLDER = "src/media/img/Player_Editor/";
	
	private Scene scene;
	
	public PlayerEditor() {				
		try {
			// create a base layout for the scene
			VBox base = new VBox(50);
			base.setId("base");
			base.setAlignment(Pos.CENTER);
			
			// add the logo and title to the screen
			ImageView logo = new ImageView(new Image(new FileInputStream(LOGO_PATH)));
			ImageView title = new ImageView(new Image(new FileInputStream(PLAYER_EDITOR_FOLDER + "title.png")));

			ComboBox<String> box = new ComboBox<String>();
			ObservableList<Player> players = getPlayers();
			ObservableList<String> playerNames = FXCollections.observableArrayList();
			for(Player p : players) {
				playerNames.add(p.getName());
			}
			
			
			Button back = new Button("Back to Main Menu"); //Back button
			back.setOnAction(e -> {
				//save
				try {
					Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
					Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
					newWindow.setScene(new Scene(loadIn, 1200, 700));
					newWindow.show();
				} catch (IOException e1) {e1.printStackTrace();}
			});

			
			base.getChildren().addAll(logo, title, back);
			
			scene = new Scene(base, 1200, 700);
			scene.getStylesheets().add("playerEditor.css");
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to load image(s).");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private ObservableList<Player> getPlayers(){
		ObservableList<Player> players = FXCollections.observableArrayList();
		try {
			Database db = new Database("jdbc:mysql://localhost:3306/reaching-insanity", "root", "");
			
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
		
		return null;
	}
	
	public Scene getScene() {
		return scene;
	}

}

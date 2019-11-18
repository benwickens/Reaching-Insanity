import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class PlayerEditor {
	
	private Scene scene;
	
	public PlayerEditor() {
		// create a base layout for the scene
		VBox base = new VBox(50);
		base.setId("base");
		base.setAlignment(Pos.CENTER);
		
//		try {
//			ImageView background = new ImageView(new Image(new FileInputStream("img/profileBackground.jpg")));
//			baseContainer.getChildren().add(background);
//		} catch (FileNotFoundException e) {
//			System.out.println("ERROR: Could not load background image.");
//		}
		
		Label title = new Label("Player Editor");
		
		
		
		
		
		base.getChildren().addAll(title);
		
		scene = new Scene(base, 1200, 900);
		scene.getStylesheets().add("playerEditor.css");
	}
	
	public Scene getScene() {
		return scene;
	}

}


	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class Main extends Application {
	
	private final int GRID_SIZE = 8;
	
	@Override
	public void start(Stage primaryStage) {
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,900,650);
		
		HBox navBar = new HBox(225);
		Button save = new Button("Save Game");
		save.setPrefWidth(150);
		save.setPrefHeight(50);
		
		Label time = new Label("MM:SS"); 
		time.setPrefWidth(10);
		time.setPrefHeight(50);
		time.setTextAlignment(TextAlignment.CENTER);
		
		Button exit = new Button("Exit Game");
		exit.setPrefWidth(150);
		exit.setPrefHeight(50);
		
		navBar.getChildren().addAll(save, time, exit);
		
		root.setTop(navBar);
		root.setCenter(new Label("GRID"));
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

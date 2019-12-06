

import java.io.FileInputStream;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import motd.CipherSolver;
import motd.HttpRequest;



public class Main extends Application {

	public static Scene scene;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			StackPane baseLayout = new StackPane();
			ImageView background = new ImageView(new Image(
					new FileInputStream("src/media/img/background.png")));    

			VBox layout = new VBox(150);
			
			VBox buttons = new VBox(5);
			Button game = new Button("Play Game");
			game.setMinWidth(150);
			game.setOnAction(e -> {
		        Stage stage = (Stage) ((Node) e.getSource())
		        		.getScene().getWindow();
		        stage.setTitle("Reaching Insanity");
		        SetupWindow setupWindow = new SetupWindow();
		        stage.setScene(setupWindow.getScene());
			});
			
			Button leaderboard = new Button("Leaderboard");
			leaderboard.setMinWidth(150);
			leaderboard.setOnAction(e -> {
			       Stage stage = (Stage) ((Node) e.getSource())
			    		   .getScene().getWindow();
			       stage.setTitle("Reaching Insanity - Leader Board");
			       try {
					stage.setScene((new LeaderBoard().getScene()));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			});
			
			Button playerEditor = new Button("Player Editor");
			playerEditor.setMinWidth(150);
			playerEditor.setOnAction(e -> {
			       Stage stage = (Stage) ((Node) e.getSource())
			    		   .getScene().getWindow();
			       stage.setTitle("Reaching Insanity - Leader Board");
			       stage.setScene(new PlayerEditor().getScene());
			});
			
			Button exitGame = new Button("Exit Game");
			exitGame.setMinWidth(150);
			exitGame.setOnAction(e -> {
				Platform.exit();
			});
			buttons.getChildren().addAll(game, leaderboard,
					playerEditor, exitGame);
			
			ImageView title = new ImageView(new Image(
					new FileInputStream("src/media/img/logo.png")));
			
			
			
			layout.getChildren().addAll(title, buttons, motd());
			baseLayout.getChildren().addAll(background, layout);
			baseLayout.getStylesheets().add("style.css");
			
			primaryStage.setTitle("Reaching Insanity");
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(800);
			
			scene = new Scene(baseLayout, 1200, 700);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static Scene getScene() {
		return scene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private Label motd() {
		HttpRequest get = new HttpRequest();
        String result = get.newConnection("http://cswebcat.swan.ac.uk/puzzle");
        System.out.println(result);
        CipherSolver solve = new CipherSolver();
        String solvedCipher = solve.solved(result);
        System.out.println("output:" + solvedCipher);
        String cipherURL = "http://cswebcat.swan.ac.uk/message?solution=" + solvedCipher;
        System.out.println(cipherURL);
        System.out.println("Connection result with solved solution:");
        result = get.newConnection(cipherURL);
        return new Label(result);
	}
	
}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameWindow {
	
	private final String LOGO_PATH = "src/media/img/logo.png";
	private final String PLAYER_PATH = "src/media/img/player_idle.png";
	private Timeline timeline;
	private Scene scene;
	private boolean paused;
	private int hours;
	private int minutes;
	private int seconds;
	private Player player;
	private GridPane gridPane;
	
	
	public GameWindow(String playerName, File levelFile) {
		BorderPane layout = new BorderPane();
		try {
			
			player = new Player(playerName, null, 0); // replace 0 w db query result
			
			ImageView logo = new ImageView(new Image(new FileInputStream(LOGO_PATH)));
			HBox top = new HBox();
			top.getChildren().add(logo);
			top.setAlignment(Pos.CENTER);
			layout.setTop(top);
			layout.setLeft(getLeft());
			layout.setRight(getRight());
			
			gridPane = getGrid();
			layout.setCenter(gridPane);
			
			scene = new Scene(layout, 1200, 700);
			scene.getStylesheets().add("gameWindow.css");
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to load image(s).");
			e.printStackTrace();
		}
	}

	public Scene getScene() {
		return scene;
	}
	
	public String getTime() {
    	String h = (hours >= 10 ? "" : "0") + hours;
    	String m = (minutes >= 10 ? "" : "0") + minutes;
    	String s = (seconds >= 10 ? "" : "0") + seconds;
    	return h + ":" + m + ":" + s;
	}
	
	private GridPane getGrid() {
		GridPane pane = new GridPane();
		
		return pane;
		
	}
	
	private VBox getLeft() throws FileNotFoundException {
		VBox left = new VBox(40);
		left.setMinWidth(300);
		left.setAlignment(Pos.CENTER);
		Label nameLabel = new Label(player.getPlayerName());
		nameLabel.setId("playerName");
		
		ImageView playerIcon = new ImageView(new Image(new FileInputStream(PLAYER_PATH)));
		
		HBox inv1 = new HBox(20);
		inv1.setId("inventoryRow");
		inv1.setMinHeight(60);
		// add items
		
		HBox inv2 = new HBox(20);
		inv2.setId("inventoryRow");
		inv2.setMinHeight(60);
		// add items
		
		left.getChildren().addAll(nameLabel, playerIcon, inv1, inv2);
		return left;
	}
	
	private VBox getRight() {
		VBox right = new VBox(40);
		right.setMinWidth(300);
		right.setAlignment(Pos.CENTER);

		Label timeLabel = new Label();
		timeLabel.setId("timeLabel");
		
		Label optionsLabel = new Label("Options");
		optionsLabel.setId("optionsLabel");
		
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
        	if(!paused) {
            	seconds ++;
            	if(seconds == 60) {
            		seconds = 0;
            		minutes ++;
            		if(minutes >= 60) {
            			minutes = 0;
            			hours ++;
            		}
            	}
        	}
        	timeLabel.setText(getTime());
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
		
		Button pause = new Button("Pause");
		pause.setOnAction(e -> {
			timeline.pause();
			paused = true;
		});
		
		Button resume = new Button("Resume");
		resume.setOnAction(e -> {
			timeline.play();
			paused = false;
		});
		
		Button exitGame = new Button("Exit and Save");
		exitGame.setOnAction(e -> {
			timeline.pause();
			paused = true;
			//save
			try {
		        Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
		        Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
		        newWindow.setScene(new Scene(loadIn, 1200, 900));
		        newWindow.show();
			} catch (IOException e1) {e1.printStackTrace();}
		});
		
		right.getChildren().addAll(timeLabel, optionsLabel, pause, resume, exitGame);
		return right;
	}

}

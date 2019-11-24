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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameWindow {

	private final String LOGO_PATH = "src/media/img/logo.png";
	private final String PLAYER_PATH = "src/media/img/player_idle.png";
	private final String GRID_PATH = "src/media/img/grid/";
	private Timeline timeline;
	private Scene scene;
	private boolean paused;
	private int hours;
	private int minutes;
	private int seconds;
	private GridPane gridPane;
	private GameState gameState;
	private BorderPane layout;


	public GameWindow(String playerName, File levelFile) {
		layout = new BorderPane();
		try {
			gameState = new GameState(levelFile, playerName);

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
			scene.setOnKeyPressed(e -> processKeyEvent(e));
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

	private GridPane getGrid() throws FileNotFoundException {
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);

		int playerX = gameState.getPlayer().getX();
		int playerY = gameState.getPlayer().getY();

		for(int gridX = -4; gridX < 4; gridX++) {
			for(int gridY = -4; gridY < 4; gridY++) {
				try {
					if(gridX == 0 && gridY == 0) {
						System.out.println("Adding Player");
						StackPane stack = new StackPane();
						ImageView img = (gameState.getGrid()[playerX + gridX][playerY + gridY]).getImage();
						ImageView player = new ImageView(new Image(new FileInputStream(GRID_PATH + "player_down.png")));
						stack.getChildren().addAll(img, player);
						pane.add(stack, 4, 4);
					}else {
						ImageView img = (gameState.getGrid()[playerX + gridX][playerY + gridY]).getImage();
						pane.add(img, gridX + 4, gridY + 4);
						System.out.println("Added Cell");
					}
				}catch(ArrayIndexOutOfBoundsException e) {
					ImageView img = new ImageView(new Image(new FileInputStream(GRID_PATH + "black.png")));
					pane.add(img, gridX + 4, gridY + 4);
					System.out.println("Added out of bound cell");
				}
			}
		}
		return pane;
	}

	private VBox getLeft() throws FileNotFoundException {
		VBox left = new VBox(40);
		left.setMinWidth(300);
		left.setAlignment(Pos.CENTER);
		Label nameLabel = new Label(gameState.getPlayer().getPlayerName());
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
				newWindow.setScene(new Scene(loadIn, 1200, 700));
				newWindow.show();
			} catch (IOException e1) {e1.printStackTrace();}
		});

		right.getChildren().addAll(timeLabel, optionsLabel, pause, resume, exitGame);
		return right;
	}

	public void processKeyEvent(KeyEvent event) {
		switch (event.getCode()) {
		case RIGHT:
			gameState.getPlayer().setX(gameState.getPlayer().getX() + 1);
			break;
		case LEFT:
			gameState.getPlayer().setX(gameState.getPlayer().getX() - 1);
			break;
		case UP:
			gameState.getPlayer().setY(gameState.getPlayer().getY() - 1);
			break;
		case DOWN:
			gameState.getPlayer().setY(gameState.getPlayer().getY() + 1);
			break;
		default:
			break;
		}
		System.out.println("new player position: " + gameState.getPlayer().getX() + ", " + gameState.getPlayer().getY());
		update();
		event.consume();
	}

	public GameState getGameState() {
		return gameState;
	}

	public void update() {
		try {
			gridPane = getGrid();
			layout.setCenter(gridPane);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}

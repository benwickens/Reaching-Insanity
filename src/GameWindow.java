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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameWindow {

	private final String LOGO_PATH = "src/media/img/logo.png";
	private final String PLAYER_PATH = "src/media/img/grid/player_down.png";
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
	private Direction direction;


	public GameWindow(String playerName, File levelFile) {
		layout = new BorderPane();

		try {
			gameState = new GameState(levelFile, playerName, 0);
			direction = Direction.DOWN;

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

		for (int gridX = -4; gridX < 4; gridX++) {
			for (int gridY = -4; gridY < 4; gridY++) {
				try {
					Cell cell = gameState.getGrid()[playerX + gridX][playerY + gridY];
					StackPane stack = new StackPane();
					stack.getChildren().add(cell.getCellImage());
					
					if (gridX == 0 && gridY == 0) { // if drawing the center cell, add player
						stack.getChildren().add(getPlayerImage());
					} else { // otherwise, add item (if there is any)
						if(cell.getItem() != null) {
							stack.getChildren().add(cell.getItemImage());
						}
					}
					pane.add(stack, gridX + 4, gridY + 4);
				} catch (ArrayIndexOutOfBoundsException e) {
					ImageView img = new ImageView(new Image(new FileInputStream(GRID_PATH + "black.png")));
					pane.add(img, gridX + 4, gridY + 4);
				}
			}
		}
		return pane;
	}

	private VBox getLeft() throws FileNotFoundException {
		VBox left = new VBox(60);
		left.setMinWidth(300);
		left.setAlignment(Pos.CENTER);
		Label nameLabel = new Label(gameState.getPlayer().getName());
		nameLabel.setId("playerName");

		ImageView playerIcon = new ImageView(new Image(new FileInputStream(PLAYER_PATH)));
		playerIcon.setScaleX(2.5);
		playerIcon.setScaleY(2.5);

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
			if (!paused) {
				seconds++;
				if (seconds == 60) {
					seconds = 0;
					minutes++;
					if (minutes >= 60) {
						minutes = 0;
						hours++;
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
			gameState.save();
			try {
				Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
				Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
				newWindow.setScene(new Scene(loadIn, 1200, 700));
				newWindow.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		right.getChildren().addAll(timeLabel, optionsLabel, pause, resume, exitGame);
		return right;
	}

	public void processKeyEvent(KeyEvent event) {
		if (!paused) {
			int nextX = gameState.getPlayer().getX();
			int nextY = gameState.getPlayer().getY();
			
			switch (event.getCode()) {
				case RIGHT:
					nextX += 1;
					direction = Direction.RIGHT;
					break;
				case LEFT:
					nextX -= 1;
					direction = Direction.LEFT;
					break;
				case UP:
					nextY -= 1;
					direction = Direction.UP;
					break;
				case DOWN:
					nextY += 1;
					direction = Direction.DOWN;
					break;
				default:
					return;
			}
			
			Cell nextCell = gameState.getGrid()[nextX][nextY];
			
			switch(nextCell.getType()) {
			case WALL:
				// set the next location to the same values as before
				nextX = gameState.getPlayer().getX();
				nextY = gameState.getPlayer().getY();
				break; 
			case RED_DOOR:
				if(gameState.getPlayer().hasItem(Collectable.RED_KEY, 1)) {
					gameState.getPlayer().useItem(Collectable.RED_KEY, 1);
					nextCell.setType(CellType.EMPTY);
				}else {
					nextX = gameState.getPlayer().getX();
					nextY = gameState.getPlayer().getY();
				}
				break; 
			case GREEN_DOOR:
				if(gameState.getPlayer().hasItem(Collectable.GREEN_KEY, 1)) {
					gameState.getPlayer().useItem(Collectable.GREEN_KEY, 1);
					nextCell.setType(CellType.EMPTY);
				}else {
					nextX = gameState.getPlayer().getX();
					nextY = gameState.getPlayer().getY();
				}
				break; 
			case BLUE_DOOR:
				if(gameState.getPlayer().hasItem(Collectable.BLUE_KEY, 1)) {
					gameState.getPlayer().useItem(Collectable.BLUE_KEY, 1);
					nextCell.setType(CellType.EMPTY);
				}else {
					nextX = gameState.getPlayer().getX();
					nextY = gameState.getPlayer().getY();
				}
				break;
			case FIRE:
				if(!gameState.getPlayer().hasItem(Collectable.FIRE_BOOTS, 1)) {
					// resetLevel();
				}
				break;
			case WATER:
				if(!gameState.getPlayer().hasItem(Collectable.FLIPPERS, 1)) {
					// resetLevel();
				}
				break; 
			case ICE:
				if(!gameState.getPlayer().hasItem(Collectable.ICE_SKATES, 1)) {
					// resetLevel();
				}
				break;
			case TELEPORTER:
				if(event.getCode().equals(KeyCode.RIGHT)) {
					nextX = nextCell.getLinkX() + 1;
					nextY = nextCell.getLinkY();
				}else if(event.getCode().equals(KeyCode.LEFT)) {
					nextX = nextCell.getLinkX() - 1;
					nextY = nextCell.getLinkY();
				}else if(event.getCode().equals(KeyCode.UP)) {
					nextX = nextCell.getLinkX();
					nextY = nextCell.getLinkY() - 1;
				}else {
					nextX = nextCell.getLinkX();
					nextY = nextCell.getLinkY() + 1;
				}
			case EMPTY:
				for(Character enemy : gameState.getEnemies()) {
					if(enemy.getX() == nextX && enemy.getY() == nextY) {
						// resetLevel();
					}
				}
				
				// if next cell does not hold an enemy then check if has an item
				if(nextCell.getItem() != null) {
					gameState.getPlayer().addToInventory(nextCell.getItem());
					nextCell.setItem(null);
				}
				break; 
			default:
				break;
			}
			gameState.getPlayer().moveTo(nextX, nextY);
			update();
		}
		event.consume();
	}
	
	public GameState getGameState() {
		return gameState;
	}

	private ImageView getPlayerImage() throws FileNotFoundException {
		if (direction == Direction.LEFT) {
			return new ImageView(new Image(new FileInputStream(GRID_PATH + "player_left.png")));
		} else if (direction == Direction.RIGHT) {
			return new ImageView(new Image(new FileInputStream(GRID_PATH + "player_right.png")));
		} else if (direction == Direction.UP) {
			return new ImageView(new Image(new FileInputStream(GRID_PATH + "player_up.png")));
		} else {
			return new ImageView(new Image(new FileInputStream(GRID_PATH + "player_down.png")));
		}
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
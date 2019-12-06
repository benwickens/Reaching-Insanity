
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LeaderBoard{

	private final String IMG_FOLDER = "src/media/img/";
	private Scene scene;
	private Database db;
	private int level;

	@SuppressWarnings("unchecked")
	public LeaderBoard() throws IOException {	
		try {
			db = new Database("jdbc:mysql://localhost:3306/reaching_insanity", "root", "");

			// start with a stack pane, add the background and add the main contents
			StackPane layout = new StackPane();
			ImageView background = new ImageView(new Image(	new FileInputStream(IMG_FOLDER + "background.png")));
			// main contents
			VBox contents = new VBox(75);

			ImageView title = new ImageView(new Image(new FileInputStream(IMG_FOLDER + "leaderboard_title.png")));

			HBox levelButtons = new HBox(50);
			for(int i = 1; i < 6; i++) {
				Button b = new Button("Level " + i);
				b.setOnAction(e -> {
					level = Integer.parseInt(b.getText().replace("Level ", ""));
					try {
						contents.getChildren().set(2, getPlayers(level));
					} catch (FileNotFoundException | SQLException e1) {
						System.out.println("ERROR: bad SQL or file not found");
						e1.printStackTrace();
					}
				});
				levelButtons.getChildren().add(b);
			}

			level = 1; // as we can't display level zero.


			Button mainMenu = new Button("Back to Main Menu");
			mainMenu.setOnAction(e -> {
				Stage newWindow = (Stage) ((Node) e.getSource())
						.getScene().getWindow();
				newWindow.setScene(Main.getScene());
				newWindow.show();
			});	

			contents.getChildren().addAll(title, levelButtons, getPlayers(level), mainMenu);
			layout.getChildren().addAll(background, contents);
			scene = new Scene(layout, 1200, 700);
			scene.getStylesheets().add("style.css");

		} catch (SQLException e) {
			System.out.println("ERROR: failed to connect to database.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private HBox getPlayers(int level) throws FileNotFoundException, SQLException {
		HBox players = new HBox(50);
		players.setPrefHeight(225);

		ResultSet rs = db.query("SELECT player.name, image_id, seconds "
				+ "FROM player, leaderboard "
				+ "WHERE "
				+ "player.name = leaderboard.name "
				+ "AND "
				+ "level = " + level 
				+ " ORDER BY seconds");

		if(rs.next()) {
			rs.previous();
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
				try {
					// check, because if null then we dont want to display anything!
					if(rs.next()) {
						VBox player = new VBox(25);
						Label name = new Label(rs.getString("name"));
						ImageView img = new ImageView(new Image(new FileInputStream(
								"src/media/img/grid/player" + rs.getInt("image_id") + "_down.png")));
						
						Label time = new Label(getTime(rs.getInt("seconds")));
						player.getChildren().addAll(name, img, time);
						player.setOpacity(0);
						
						players.getChildren().add(player);
						
						FadeTransition ft = new FadeTransition(Duration.seconds(1), player);
						ft.setToValue(1.0);
						ft.play();
						playSound("src/media/sound/leaderboard.wav");	
					}
				}catch(SQLException | FileNotFoundException er) {
					er.printStackTrace();
					System.exit(-1);
				}
			}));
			timeline.setCycleCount(3);
			timeline.play();
		}else {
			playSound("src/media/sound/error.wav");	
		}
		


		return players;
	}

	private String getTime(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = ((seconds % 3600) % 60) % 60;

		String h = (hours >= 10 ? "" : "0") + hours;
		String m = (minutes >= 10 ? "" : "0") + minutes;
		String s = (seconds >= 10 ? "" : "0") + seconds;
		return h + ":" + m + ":" + s;
	}


	public Scene getScene() {
		return scene;
	}

    private void playSound(String path) {
		Media media = new Media(new File(path).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
    }


	//		// create a layout for the scene with element spacing 100px
	//		VBox layout = new VBox(100);
	//		layout.setAlignment(Pos.CENTER);
	//		
	//		// firstly display the title and apply css styles to it from styles.css
	//		Label title = new Label("Leader Board");
	//		title.setId("title");
	//		
	//		// add a choice box which allows the user to chose which data should be displayed.
	//		ChoiceBox<String> levelSelector = new ChoiceBox<String>();
	//		ObservableList<String> levels = FXCollections.observableArrayList();
	//		levels.addAll("Level 1", "Level 2", "Level 3", "Level 4", "Level 5");
	//		levelSelector.setItems(levels);
	//		
	//		layout.getChildren().addAll(title, levelSelector);
	//		
	//		// Set up the leaderboard table
	//		try {
	//			// first connect to the database, if this fails it will fall through to catch statements
	//			// (in which case there is no table to be displayed).
	//			db = new Database("jdbc:mysql://localhost:3306/reaching-insanity", "root", "");
	//			
	//			// define the table structure (column names and the values they should take from the type of object)
	//			TableColumn<LeaderBoardEntry, String> userNameCol = new TableColumn<>("User Name");
	//			userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
	//			
	//			TableColumn<LeaderBoardEntry, Integer> levelCol = new TableColumn<>("Level");
	//			levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
	//			
	//			TableColumn<LeaderBoardEntry, Integer> timeCol = new TableColumn<>("Time");
	//			timeCol.setCellValueFactory(new PropertyValueFactory<>("seconds"));
	//			
	//			// create the table and alter some settings
	//			TableView<LeaderBoardEntry> table = new TableView<>();
	//			table.getColumns().addAll(userNameCol, levelCol, timeCol);
	//			// prevents extra columns being added to take up empty space
	//			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	//			// prevents the table spanning to the edge of the window
	//			table.setPadding(new Insets(10, 50, 10, 50));
	//			
	//			// loads data into the table 
	//			table.setItems(getLeaderBoardEntries());
	//			table.getSortOrder().add(timeCol);
	//			
	//			// finally adds the leaderboard to the scene
	//			layout.getChildren().add(table);
	//		} catch (CommunicationsException e) {
	//			System.out.println("ERROR: Could not communicate with the MySQL Database");
	//			Label error = new Label("ERROR: Cannot connect to the database.");
	//			layout.getChildren().add(error);
	//		} catch (SQLException e) {
	//			System.out.println("ERROR: Bad SQL query.");
	//			Label error = new Label("ERROR: Bad SQL query.");
	//			layout.getChildren().add(error);
	//		}

	//	private ObservableList<LeaderBoardEntry> getLeaderBoardEntries() throws SQLException {
	//		// initialise an array list to which we will add leader board entries to
	//		ObservableList<LeaderBoardEntry> entries = FXCollections.observableArrayList();
	//		ResultSet rs = db.query("SELECT * FROM leaderboard");
	//		while(rs.next()) {
	//			String name = rs.getString(1);
	//			int level = rs.getInt(2);
	//			int seconds = rs.getInt(3);
	//			entries.add(new LeaderBoardEntry(name, level, seconds));
	//		}
	//		return entries;
	//	}


}

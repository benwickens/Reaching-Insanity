
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LeaderBoard{
	
	private final String IMG_FOLDER = "src/media/img/";
	private Scene scene;
	private Database db;
	private int level;
	
	@SuppressWarnings("unchecked")
	public LeaderBoard() throws IOException {	
		// start with a stack pane, add the background and add the main contents
		StackPane layout = new StackPane();
		ImageView background = new ImageView(new Image(	new FileInputStream(IMG_FOLDER + "background.png")));
		// main contents
		VBox contents = new VBox(100);
		
		ImageView title = new ImageView(new Image(new FileInputStream(IMG_FOLDER + "leaderboard_title.png")));
		
		HBox levelButtons = new HBox(50);
		System.out.println("level buttons");
		for(int i = 1; i < 6; i++) {
			System.out.println(i);
			Button b = new Button("Level " + i);
			b.setOnAction(e -> {
				level = Integer.parseInt(b.getText().replace("Level ", ""));
				System.out.println("level = " + level);
			});
			levelButtons.getChildren().add(b);
		}
		
		level = 1; // as we can't display level zero.
		
		
		Button mainMenu = new Button("Back to Main Menu");
		mainMenu.setOnAction(e -> {
			try {
		        Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
		        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		        stage.setMinHeight(800);
		        stage.setMinWidth(1000);
		        stage.setScene(new Scene(loadIn,1200,700));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});	
		
		contents.getChildren().addAll(title, levelButtons, mainMenu);
		layout.getChildren().addAll(background, contents);
		scene = new Scene(layout, 1200, 700);
		scene.getStylesheets().add("style.css");
	}
	
	
	public Scene getScene() {
		return scene;
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

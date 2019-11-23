
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LeaderBoard{
	
	private Scene scene;
	private Database db;
	
	@SuppressWarnings("unchecked")
	public LeaderBoard() throws IOException {
		// create a layout for the scene with element spacing 100px
		VBox layout = new VBox(100);
		layout.setAlignment(Pos.CENTER);
		
		// firstly display the title and apply css styles to it from styles.css
		Label title = new Label("Leader Board");
		title.setId("title");
		
		// add a choice box which allows the user to chose which data should be displayed.
		ChoiceBox<String> levelSelector = new ChoiceBox<String>();
		ObservableList<String> levels = FXCollections.observableArrayList();
		levels.addAll("Level 1", "Level 2", "Level 3", "Level 4", "Level 5");
		levelSelector.setItems(levels);
		
		layout.getChildren().addAll(title, levelSelector);
		
		// Set up the leaderboard table
		try {
			// first connect to the database, if this fails it will fall through to catch statements
			// (in which case there is no table to be displayed).
			db = new Database("jdbc:mysql://localhost:3306/reaching-insanity", "root", "");
			
			// define the table structure (column names and the values they should take from the type of object)
			TableColumn<LeaderBoardEntry, String> userNameCol = new TableColumn<>("User Name");
			userNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
			
			TableColumn<LeaderBoardEntry, Integer> levelCol = new TableColumn<>("Level");
			levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
			
			TableColumn<LeaderBoardEntry, Integer> timeCol = new TableColumn<>("Time");
			timeCol.setCellValueFactory(new PropertyValueFactory<>("seconds"));
			
			// create the table and alter some settings
			TableView<LeaderBoardEntry> table = new TableView<>();
			table.getColumns().addAll(userNameCol, levelCol, timeCol);
			// prevents extra columns being added to take up empty space
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			// prevents the table spanning to the edge of the window
			table.setPadding(new Insets(10, 50, 10, 50));
			
			// loads data into the table 
			table.setItems(getLeaderBoardEntries());
			table.getSortOrder().add(timeCol);
			
			// finally adds the leaderboard to the scene
			layout.getChildren().add(table);
		} catch (CommunicationsException e) {
			System.out.println("ERROR: Could not communicate with the MySQL Database");
			Label error = new Label("ERROR: Cannot connect to the database.");
			layout.getChildren().add(error);
		} catch (SQLException e) {
			System.out.println("ERROR: Bad SQL query.");
			Label error = new Label("ERROR: Bad SQL query.");
			layout.getChildren().add(error);
		}
		
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
		layout.getChildren().add(mainMenu);
		
		scene = new Scene(layout, 1200, 700);
		scene.getStylesheets().add("style.css");
	}
	
	private ObservableList<LeaderBoardEntry> getLeaderBoardEntries() throws SQLException {
		// initialise an array list to which we will add leader board entries to
		ObservableList<LeaderBoardEntry> entries = FXCollections.observableArrayList();
		ResultSet rs = db.query("SELECT * FROM leaderboard");
		while(rs.next()) {
			String name = rs.getString(1);
			int level = rs.getInt(2);
			int seconds = rs.getInt(3);
			entries.add(new LeaderBoardEntry(name, level, seconds));
		}
		return entries;
	}
	
	public Scene getScene() {
		return scene;
	}

}

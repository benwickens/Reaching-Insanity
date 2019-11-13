package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;



public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
			primaryStage.setTitle("Have Fun!");
			root.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			primaryStage.setScene(new Scene(root,1200,900));
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.layout.TilePane;

public class DecisionPopUp extends Application {

	//Just trying to get it to show with the CSS attached but it wont?
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("DecisionPopUp.fxml"));
			primaryStage.setTitle("Have Fun!");
			primaryStage.setMinHeight(200);
			primaryStage.setMinWidth(500);
			Scene popUp = new Scene(root,500,200);
			popUp.getStylesheets().add("style.css");
			
			primaryStage.setScene(popUp);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
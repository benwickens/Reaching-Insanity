import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Purpose:
 * Produces a pop up with a given message. 
 * <br>
 * @author Robbie Ko and Alan Tollett.
 * @version 1.0
 */ 
public class PopUp {

	/**
	 * Creates a small pop up window with a given message.
	 * @param message - The content of the label. 
	 * @param success - Boolean that represents the type of message being shown.
	 */
	public PopUp(String message, boolean success) {    	
		VBox dBox = new VBox();

		Label label = new Label(message);

		if (success) {
			label.setId("success");
		} else {
			label.setId("error");
		}

		dBox.getChildren().add(label);

		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		Scene scene = new Scene(dBox, 900, 100);
		scene.getStylesheets().add("style.css");
		window.setScene(scene);
		window.show();
	}
}
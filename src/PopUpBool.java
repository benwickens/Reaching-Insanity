import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Purpose:
 * Produces a pop up window with two choices.
 * <br>
 * @author Alan Tollet.
 * @version 1.0
 */ 
public class PopUpBool {
    /*The decision of the user.*/
	private boolean result;
	/*Represents if the user has pressed a button or not.*/
	private boolean waiting;
	
	/**
	 * Constructor -  Produces a pop up window with two buttons, 
	 * producing a boolean 
	 * @param message - The content of the label that is shown.
	 */
    public PopUpBool(String message) {
    	Stage window = new Stage();
    	VBox dBox = new VBox(15);
    	
    	Label label = new Label(message);
    	
    	Button yes = new Button("Yes");
    	yes.setOnAction(e -> {
    		result = true;
    		waiting = false;
    		window.close();
    	});
    	
    	Button no = new Button("No");
    	no.setOnAction(e -> {
    		result = false;
    		waiting = false;
    		window.close();
    	});
    	
    	HBox yesNo = new HBox(25);
    	yesNo.setAlignment(Pos.CENTER);
    	yesNo.getChildren().addAll(yes, no);
    	
    	dBox.getChildren().addAll(label, yesNo);
    	
        window.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(dBox, 900, 100);
        scene.getStylesheets().add("style.css");
        window.setScene(scene);
        window.show();
        
        waiting = true;
    }
    
    /**
     * Tracks if the user has made a decision on the pop-up.
     * @return boolean - True if the player hasn't made a decision yet.
     */
    public boolean getWaiting() {
    	return waiting;
    }

    /**
     * Gets the result from the users decision.
     * @return Boolean - True if the user clicks yes, false otherwise.
     */
	public boolean getResult() {
		return result;
	}

}

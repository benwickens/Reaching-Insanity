import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {
	
    private String message;
    private boolean success;
    
    public PopUp(String message, boolean success) {
    	this.message = message; 
    	this.success = success;
    	
    	VBox dBox = new VBox();
    	
    	Label label = new Label(message);
    	
    	if(success) {
    		label.setId("success");
    	}else {
    		label.setId("error");
    	}
    	
    	dBox.getChildren().add(label);
    	
    	Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(dBox, 500, 100);
        scene.getStylesheets().add("style.css");
        window.setScene(scene);
        window.show();
    }
}
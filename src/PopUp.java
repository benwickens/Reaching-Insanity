import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUp {
    private String message;
    boolean b;
    private String abc;
    VBox dialogVbox = new VBox();
    Button back = new Button("Back");
    private final Stage dialog = new Stage();
    Scene scene;

    public PopUp(String a, boolean b) {
        this.message = a;
        this.b = b;
    }

    public Scene getScene() {
        switch (message){
            case "ERROR: name already taken":
            case "ERROR: you need to select two different players":
            case "ERROR: you need to select a player and a level":
            case "Error: level file not found":
            case "ERROR: File not formatted properly.":
            case "ERROR: Failed to load image(s).":
            case "ERROR: SQL":
            case "ERROR: getting player image":
            case "ERORR: character image not found.":
            case "ERROR: Cannot create file.":
                abc = message;
                break;
            case "success":
                abc = "Player Created !";
                break;
        }
            if (b=true){
                dialogVbox.getChildren().addAll(new Label(abc),back);
            }else{
                dialogVbox.getChildren().addAll(new Label(abc));
            }
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialogVbox.setAlignment(Pos.CENTER);
            scene = new Scene(dialogVbox, 300, 100);
            back.setOnAction(e -> dialog.close());
        return scene;
    }
}

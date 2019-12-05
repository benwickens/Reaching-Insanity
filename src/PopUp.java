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
                abc = message;
                break;
            case "success":
                abc = "Player Created !";
                break;
            case "ERROR: you need to select two different players":
                abc = "you need to select two different players";
                break;
            case "ERROR: you need to select a player and a level":
                abc = " you need to select a player and a level";
                break;
            case "Error: level file not found":
                abc = "Error: level file not found.";
                break;
            case "ERROR: File not formatted properly.":
                abc = "ERROR: File not formatted properly.";
                break;
            case "ERROR: Failed to load image(s).":
                abc = "ERROR: Failed to load image(s).";
                break;
            case "ERROR: SQL":
                abc = "ERROR: SQL";
                break;
            case "ERROR: getting player image":
                abc = "ERROR: getting player image";
                break;
            case "ERORR: character image not found.":
                abc = "ERORR: character image not found.";
                break;
            case "ERROR: Cannot create file.":
                abc ="ERROR: Cannot create file.";
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

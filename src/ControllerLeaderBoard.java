import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * File name: ControllerLeaderBoard
 *
 * @version 1.0
 * Creation Date: 14/11/2019
 * Last Modification date: 15/11/2019
 * @author Robbie Ko
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * The Controller for the leaderboard
 * <br>
 * Version History
 * 1.0 - Created Button allow to go back to main menu nad some dummy label
 *
 */
public class ControllerLeaderBoard {
    @FXML
    private void backPress(ActionEvent event)throws IOException {
        Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene newScene = new Scene(loadIn);
        Stage newwindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        newwindow.setScene(newScene);
        newwindow.show();
    }
}

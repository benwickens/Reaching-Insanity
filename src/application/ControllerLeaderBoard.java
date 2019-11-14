package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
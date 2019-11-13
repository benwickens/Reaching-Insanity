package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerMenu implements Initializable {
    @FXML
    private MediaView see;
    private Media look;
    private MediaPlayer video;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String path = new File("DummyTest.mp4").getAbsolutePath();
        look = new Media(new File(path).toURI().toString());
        video = new MediaPlayer(look);
        see.setMediaPlayer(video);
        video.setAutoPlay(true);
        video.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @FXML
    private void playGamePress(ActionEvent event)throws IOException {
        video.stop();
        Parent loadIn = FXMLLoader.load(getClass().getResource("mainGame.fxml"));
        Scene newScene = new Scene(loadIn);
        Stage newwindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        newwindow.setScene(newScene);
        newwindow.show();
    }

    @FXML
    private void leaderBoardPress(ActionEvent event)throws IOException {
        video.stop();
        Parent loadIn = FXMLLoader.load(getClass().getResource("leaderBoard.fxml"));
        Scene newScene = new Scene(loadIn);
        Stage  newwindow = (Stage)((Node)event.getSource()).getScene().getWindow();

        newwindow.setScene(newScene);
        newwindow.show();
    }
    @FXML
    private void exitGamePress(ActionEvent event) {
        Platform.exit();
    }


}
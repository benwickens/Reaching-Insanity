import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ControllerGame implements Initializable {

    @FXML
    private Label timerCount;
    private Timeline timeline;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(0);
    private final IntegerProperty timeMinutes = new SimpleIntegerProperty(0);
    private final IntegerProperty timeHour = new SimpleIntegerProperty(0);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> display()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        timerCount.textProperty().bind(display());
    }

    @FXML
    private void pauseButton(ActionEvent event) {
        timeline.pause();
    }

    @FXML
    private void resumeButton(ActionEvent event) {
        timeline.play();
    }


    private StringExpression display() {
        int minutes = timeMinutes.get();
        int hour = timeHour.get();
        int seconds = timeSeconds.get();
        timeSeconds.set(seconds + 1);
        if (timeSeconds.getValue() == 60) {
            timeSeconds.set(0);
            timeMinutes.set(minutes + 1);
        }
        if (minutes == 60) {
            timeHour.set(hour + 1);
            timeMinutes.set(0);
        }
        return Bindings.concat("Current Time: ", timeHour.asString(), ":", timeMinutes.asString(), ":", timeSeconds.asString());
    }
}





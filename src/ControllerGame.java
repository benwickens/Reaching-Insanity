
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * File name: ControllerGame
 *
 * @version 1.0
 * Creation Date: 14/11/2019
 * Last Modification date: 15/11/2019
 * @author Robbie ,Ben
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * The controller for the Main Game
 * <br>
 * Version History
 * 1.0 - Added timer that auto start when player goes into this scene (Added pasues and resume buttons for later save and load funtions)
 * 1.01 - Added method to extracts the time - Ben
 *
 */

public class ControllerGame implements Initializable {
	
	private String currentTime; //used to get the current time.

    @FXML
    private Label timerCount;
    private Timeline timeline;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(0);
    private final IntegerProperty timeMinutes = new SimpleIntegerProperty(0);
    private final IntegerProperty timeHour = new SimpleIntegerProperty(0);
    @FXML
    private GridPane grids;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> display()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        timerCount.textProperty().bind(display());
        drawinggrid();
        grids.autosize();

    }

    private void drawinggrid() {
        lookFor("src/levels/1.txt");
    }

    public String getCurrentTime() {
    	return this.currentTime;
    }

    @FXML
    private void pauseButton(ActionEvent event) {
        timeline.pause();
    }

    @FXML
    private void resumeButton(ActionEvent event) {
        timeline.play();
    }

    @FXML
    private void backPressed(ActionEvent event) throws IOException {
        Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Stage newWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        newWindow.setScene(new Scene(loadIn,1200,900));
        newWindow.show();
    }

    // Added a temp file reader that will read the file and load the game map in - still needs to change since you need to update the map
    private void lookFor(String path) {
        File level = new File(path);
        FileReader a = null;
        try {
            a = new FileReader(level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(a);
        String line;
        int y = 0;
        int x = 0;
        try {
            while ((line = br.readLine()) != null) {
                char[] tokens = line.toUpperCase().toCharArray();
                for ( int i  = 0; i <tokens.length; i++){
                    if (tokens[i] == 'W') {
                        File image = new File("src/gridPictures/Wall.png");
                        grids.add(new ImageView(new Image(image.toURI().toString())),x,y);
                        x++;
                    } else if (tokens[i] == 'E') {
                        File image = new File("src/gridPictures/ice.png");
                        Image picture = new Image(image.toURI().toString());
                        grids.add(new ImageView(picture),x,y);
                        x++;
                    } else if (tokens[i] == ','){
                        //
                    }
                    else{
                        System.out.print("\n");
                        y++;
                        x=0;
                    }
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        
        this.currentTime = timeHour.asString() + ":" + timeMinutes.asString() + ":" + timeMinutes.asString();
        return Bindings.concat("Current Time: ", timeHour.asString(), ":", timeMinutes.asString(),
        		":", timeSeconds.asString());
    }

    /**
     * Testing purpose
     */
    /*
    public static void main(String[] args) throws IOException {
        File level = new File("src/Level1/levelOne.txt");
        FileReader fr = new FileReader(level);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int y = 0;
        int x = 0;
        try {
            while ((line = br.readLine()) != null) {
                char[] tokens = line.toUpperCase().toCharArray();
                for ( int i  = 0; i <tokens.length; i++){

                    if (tokens[i] == 'W') {
                        System.out.print('w');//"src/gridPictures/Wall.png");
                        System.out.print(x+".");
                        System.out.print(y);
                        x++;
                    } else if (tokens[i] == 'E') {
                        System.out.print('e');//"src/gridPictures/Wall.Green-Key.png");
                        System.out.print(x+".");
                        System.out.print(y);
                        x++;
                    } else if (tokens[i] == ','){
                        System.out.print("");
                    }
                    else{
                        System.out.print("\n");
                        y++;
                        x=0;
                    }

                }

            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    }






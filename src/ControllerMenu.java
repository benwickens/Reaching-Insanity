
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import motd.CipherSolver;
import motd.HttpRequest;

/**
 * File name: ControllerMenu
 *
 * @version 1.0 Creation Date: 14/11/2019 Last Modification date: 15/11/2019
 * @author Robbie Ko, Alan Tollett
 * 
 *         No copyright.
 * 
 *         Purpose: The Controller for the Menu
 * 
 *         Version History 1.0 - loads in a video, making the buttons workings
 *         with their purpose (Message of the day implemented) - Robbie
 */

public class ControllerMenu implements Initializable {

	/**
	 * the references to the media player in the fxml file
	 */
	@FXML
	/** the properties of the media*/
	private MediaView see;
	
	/**the actual media*/
	private Media look;
	
	/** the control for the media*/
	private MediaPlayer video;

	/**
	 * the references to the media player in the fxml file
	 */
	@FXML
	private Label messageDisplay;

	/**
	 * the references to the Anchor Pane in the fxml file
	 */
	@FXML
	private AnchorPane layout;

	/**
	 * the Method to load in a video and display the message of the day on the
	 * background
	 */
	public void initialize(URL location, ResourceBundle resources) {
		String path = 
				new File("src/media/video/DummyTest.mp4").getAbsolutePath();
		look = new Media(new File(path).toURI().toString());
		video = new MediaPlayer(look);
		see.setMediaPlayer(video);
		see.setPreserveRatio(false);
		see.fitHeightProperty().bind(layout.heightProperty());
		see.fitWidthProperty().bind(layout.widthProperty());
		video.setAutoPlay(true);
		video.setCycleCount(MediaPlayer.INDEFINITE);

		HttpRequest get = new HttpRequest();
		String result = get.newConnection("http://cswebcat.swan.ac.uk/puzzle");
		CipherSolver solve = new CipherSolver();
		String solvedCipher = solve.solved(result);
		String cipherURL = 
				"http://cswebcat.swan.ac.uk/message?solution=" + solvedCipher;
		result = get.newConnection(cipherURL);
		messageDisplay.setText(result);
		messageDisplay.setAlignment(Pos.CENTER);
		messageDisplay.prefWidth(result.length());

	}

	/**
	 * Handle when play game is pressed
	 * 
	 * @param event
	 * @throws Exception
	 */
	@FXML
	private void playGamePress(ActionEvent event) throws Exception {
		video.stop();
		Stage stage = 
				(Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle("Reaching Insanity");
		SetupWindow setupWindow = new SetupWindow();
		stage.setScene(setupWindow.getScene());
	}

	/**
	 * Handle where leader board is pressed
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void leaderBoardPress(ActionEvent event) throws IOException {
		video.stop();
		Stage stage = 
				(Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle("Reaching Insanity - Leader Board");
		stage.setScene((new LeaderBoard().getScene()));
	}

	/**
	 * Handles where the player editor is pressed
	 * 
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void playerEditorPress(ActionEvent event) throws IOException {
		video.stop();
		Stage stage = 
				(Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setMinWidth(920);
		stage.setMinHeight(700);
		stage.setTitle("Reaching Insanity - Player Editor");
		stage.setScene((new PlayerEditor().getScene()));
	}

	/**
	 * Handles where the exit game is pressed
	 * 
	 * @param event
	 */
	@FXML
	private void exitGamePress(ActionEvent event) {
		Platform.exit();
	}

}
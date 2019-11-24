
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlayerEditor {
	
	private final String LOGO_PATH = "src/media/img/logo.png";
	private final String PLAYER_PATH = "src/media/img/player_idle.png";
	private final String PLAYER_EDITOR_FOLDER = "src/media/img/Player_Editor/";
	
	private Scene scene;
	
	private ArrayList<Image> headIcons;
	private ImageView headIcon;
	private int headIndex;
	
	private ArrayList<Image> bodyIcons;
	private ImageView bodyIcon;
	private int bodyIndex;
	
	private ArrayList<Image> feetIcons;
	private ImageView feetIcon;
	private int feetIndex;
	
	public PlayerEditor() {				
		try {
			// create a base layout for the scene
			VBox base = new VBox(75);
			base.setId("base");
			base.setAlignment(Pos.CENTER);
			
			// add the logo and title to the screen
			ImageView logo = new ImageView(new Image(new FileInputStream(LOGO_PATH)));
			ImageView title = new ImageView(new Image(new FileInputStream(PLAYER_EDITOR_FOLDER + "title.png")));
			
			loadImages();
			System.out.println(headIcons.size());
			System.out.println(bodyIcons.size());
			System.out.println(feetIcons.size());
			
			headIcon = new ImageView(headIcons.get(0));
			bodyIcon = new ImageView(bodyIcons.get(0));
			feetIcon = new ImageView(feetIcons.get(0));
			

			
			base.getChildren().addAll(logo, title, getHeadHBox(), getBodyHBox(), getFeetHBox());
			
			scene = new Scene(base, 1200, 700);
			scene.getStylesheets().add("playerEditor.css");
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to load image(s).");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private HBox getHeadHBox() {
		HBox head = new HBox(50);
		head.setAlignment(Pos.CENTER);
		Button left = new Button("Left");
		left.setOnAction(e -> {
			headIndex += 1;
			if(headIndex >= headIcons.size()) {
				headIndex = 0;
			}
			headIcon.setImage(headIcons.get(headIndex));
		});
		
		Button right = new Button("Right");
		right.setOnAction(e -> {
			headIndex -= 1;
			if(headIndex < 0) {
				headIndex = headIcons.size() - 1;
			}
			headIcon.setImage(headIcons.get(headIndex));
		});
		head.getChildren().addAll(left, headIcon, right);
		return head;
	}
	
	private HBox getBodyHBox() {
		HBox body = new HBox(50);
		body.setAlignment(Pos.CENTER);
		Button left = new Button("Left");
		left.setOnAction(e -> {
			bodyIndex += 1;
			if(bodyIndex >= bodyIcons.size()) {
				bodyIndex = 0;
			}
			bodyIcon.setImage(bodyIcons.get(bodyIndex));
		});
		
		Button right = new Button("Right");
		right.setOnAction(e -> {
			bodyIndex -= 1;
			if(bodyIndex < 0) {
				bodyIndex = bodyIcons.size() - 1;
			}
			bodyIcon.setImage(bodyIcons.get(bodyIndex));
		});
		body.getChildren().addAll(left, bodyIcon, right);
		return body;
	}
	
	private HBox getFeetHBox() {
		HBox feet = new HBox(50);
		feet.setAlignment(Pos.CENTER);
		Button left = new Button("Left");
		left.setOnAction(e -> {
			feetIndex += 1;
			if(feetIndex >= feetIcons.size()) {
				feetIndex = 0;
			}
			feetIcon.setImage(feetIcons.get(feetIndex));
		});
		
		Button right = new Button("Right");
		right.setOnAction(e -> {
			feetIndex -= 1;
			if(feetIndex < 0) {
				feetIndex = feetIcons.size() - 1;
			}
			feetIcon.setImage(feetIcons.get(feetIndex));
		});
		
		Button back = new Button("Back to Main Menu"); //Back button 
		back.setOnAction(e -> {
			//save
			try {
				Parent loadIn = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
				Stage newWindow = (Stage) ((Node) e.getSource()).getScene().getWindow();
				newWindow.setScene(new Scene(loadIn, 1200, 700));
				newWindow.show();
			} catch (IOException e1) {e1.printStackTrace();}
		});
		feet.getChildren().addAll(left, feetIcon, right, back);
		return feet;
	}
	
	private void loadImages() throws FileNotFoundException{
		headIcons = new ArrayList<Image>();
		bodyIcons = new ArrayList<Image>();
		feetIcons = new ArrayList<Image>();
		
		File folder = new File(PLAYER_EDITOR_FOLDER);
		if(!folder.isDirectory()) {
			System.out.println("ERROR: Player Editor's folder path is incorrect.");
			System.exit(1);
		}
		
		for(File f : folder.listFiles()) {
			String fName = f.getName();
			if(fName.equals("Head") || fName.equals("Body") || fName.equals("Feet")) {
				for(File f2 : f.listFiles()) {
					if(f2.getName().contains("right")) {
						if(f2.getPath().contains("Head")) {
							headIcons.add(new Image(new FileInputStream(f2.getPath())));
						}else if(f2.getPath().contains("Body")) {
							bodyIcons.add(new Image(new FileInputStream(f2.getPath())));
						}else {
							feetIcons.add(new Image(new FileInputStream(f2.getPath())));
						}
					}
				}
			}
		}
	}
	
	public Scene getScene() {
		return scene;
	}

}

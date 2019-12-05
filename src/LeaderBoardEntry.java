import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LeaderBoardEntry {

	private String name;
	private int level;
	private int seconds;
	private ImageView image;
	
	public LeaderBoardEntry(String name, int level, int seconds, int imageID) {
		this.name = name;
		this.level = level;
		this.seconds = seconds;
		try {
			this.setImage(new ImageView(new Image(new FileInputStream(
					"src/media/img/grid/player" + imageID + "_down.png"))));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: Failed to load player image");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public ImageView getImage() {
		return image;
	}
	public void setImage(ImageView image) {
		this.image = image;
	}
	
}

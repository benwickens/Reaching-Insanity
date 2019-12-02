import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * File name: Character.java
 *
 * @version 1.1
 * Creation Date: 18/11/2019
 * Last Modification date: 26/11/2019
 * @author Gursimran Randhawa, Yassine Abdalass, Millie Quinn
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * Holds the behaviours and attributes that characters have.
 * <br>
 * Version History
 * 1.0 - structure of the class made
 * 1.1 - changed to implement more methods.
 */

public abstract class Character {
	
	protected int x;
	protected int y;
	
	private ImageView image;
	private final String IMAGE_PATH = "src/media/img/grid/";

	public Character(int x, int y, String imageName){
		this.x = x;
		this.y = y;
		
        try {
			image = new ImageView(new Image(new FileInputStream(IMAGE_PATH + imageName)));
		} catch (FileNotFoundException e) {
			System.out.println("ERORR: character image not found.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public Character() {
		//Added to avoid error
	}

	public int getX(){
		 return x;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getY(){
		return y;
	}

	public void setY(int y){
		this.y = y;
	}
	
	//public abstract void move(Cell[][] grid);

	public void moveTo(int exactX, int exactY) {
		x = exactX;
		y = exactY;
	}

	public ImageView getImage() {
		return image;
	}
}
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
	
	/**
	 * The character x Coordinate
	 */
	protected int x;
	
	/**
	 * The Character y coordinate
	 */
	protected int y;
	
	/**
	 * The Image to be loaded (specific for each character)
	 */
	private ImageView image;
	
	/**
	 * The path to the separate images for each individual character
	 */
	private final String IMAGE_PATH = "src/media/img/grid/";

	/**
	 * Constructs a Character at a specific x and y coordinate with a specific image
	 * 
	 * @param x The x Coordinate where a character is instantiated
	 * @param y The y Coordinate where a character is instantiated
	 * @param imageName The path that would be used to load the image for a specific Character
	 */
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
	}

	/**
	 * 
	 * @return The Character x coordinate
	 */
	public int getX(){
		 return x;
	}

	/**
	 * 
	 * @param x The Character x coordinate to be set
	 */
	public void setX(int x){
		this.x = x;
	}

	/**
	 * 
	 * @return The Character y coordinate
	 */
	public int getY(){
		return y;
	}

	/**
	 * 
	 * @param y The Character y coordinate to be set
	 */
	public void setY(int y){
		this.y = y;
	}
	
	/**
	 * Moves a Character along the grid
	 * @param grid The grid on which the Characters move
	 */
	public abstract void move(Cell[][] grid);

	/**
	 * Moves the Character to a specific X and Y coordinate
	 * @param exactX The exact X coordinate of a Character
	 * @param exactY The exact Y coordinate of a Character
	 */
	public void moveTo(int exactX, int exactY) {
		x = exactX;
		y = exactY;
	}

	/**
	 * 
	 * @return The Image specific to each character
	 */
	public ImageView getImage() {
		return image;
	}
}
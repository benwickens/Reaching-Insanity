import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * Represents a "cell" in the game (wall, empty, fire...)
 * @author Alan Tollett
 * @version 1.0
 */

public class Cell {

	/** path to the folder contaning the image files */
	private static final String GRID_IMAGES = "src/media/img/grid/";

	/** the type of the cell (e.g. wall, fire, door...) */
	private CellType type;
	/** the item which the cell holds (e.g. token, red key...) 
	 * - normally null */
	private Collectable item;
	/** an image which can be drawn to the screen representing this cell */
	private ImageView cellImage;
	/** an image which can be drawn to the screen representing this cell's
	 *  item */
	private ImageView itemImage;
	/** The X location this cell links to (if a teleporter) */
	private int linkX;
	/** The Y location this cell links to (if a teleporter) */
	private int linkY;
	/** The tokens required to open (if a token door) */
	private int tokens;

	/**
	 * Constructs a cell object
	 * 
	 * @param type the type of cell
	 * @param item the item that the cell holds (can be null)
	 */
	public Cell(CellType type, Collectable item) {
		this.type = type;
		this.item = item;

		try {
			cellImage = loadCellImage();
			itemImage = loadItemImage();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Removes the item from the cell
	 */
	public void removeItem() {
		this.item = null;
	}
	
	/**
	 * Getter for item
	 * @return the item held by the cell
	 */
	public Collectable getItem() {
		return item;
	}

	/**
	 * Set the item held by the cell
	 * @param item item the cell should store
	 */
	public void setItem(Collectable item) {
		this.item = item;
	}

	/**
	 *  Get the type
	 * @return the type of the cell
	 */
	public CellType getType() {
		return type;
	}

	/**
	 * Set the type of cell
	 * @param type the type 
	 */
	public void setType(CellType type) {
		this.type = type;
	}

	/**
	 * Get the image representing the cell
	 * @return the image represented by the cell
	 */
	public ImageView getCellImage() {
		return cellImage;
	}

	/**
	 * Set the image that represents the cell
	 * @param cellImage the new image
	 */
	public void setCellImage(ImageView cellImage) {
		this.cellImage = cellImage;
	}

	/**
	 * Get the image of the item held by the cell
	 * @return the image of the item
	 */
	public ImageView getItemImage() {
		return itemImage;
	}

	/**
	 * Set the image of the item held by the cell
	 * @param itemImage the new image
	 */
	public void setItemImage(ImageView itemImage) {
		this.itemImage = itemImage;
	}

	/**
	 * Get the x of the teleporter that this cell links to, 
	 * if there is one.
	 * @return the x of the other teleporter
	 */
	public int getLinkX() {
		return linkX;
	}

	/**
	 * Set the x of the teleporter that this cell links to,
	 * if there is one.
	 * @param linkX the x location of the other teleporter
	 */
	public void setLinkX(int linkX) {
		this.linkX = linkX;
	}

	/**
	 * Get the y of the teleporter that this cell links to, 
	 * if there is one.
	 * @return the y location of the other teleporter
	 */
	public int getLinkY() {
		return linkY;
	}

	/**
	 * Set the y of the teleporter that this cell links to,
	 * if there is one.
	 * @param linkY the y location of the other teleporter
	 */
	public void setLinkY(int linkY) {
		this.linkY = linkY;
	}

	/**
	 * Get the tokens required to open (if cell is a token door)
	 * @return the tokens required to open
	 */
	public int getTokens() {
		return tokens;
	}
	
	/**
	 * gets the image view of the item depending on its type
	 * @return the image view for the type
	 * @throws FileNotFoundException whenever any of the image 
	 * files cannot be found
	 */
	private ImageView loadItemImage() throws FileNotFoundException {
		// sets the item image
		FileInputStream fis = null;
		if (item != null) {
			switch (item) {
			case TOKEN:
				fis = new FileInputStream(GRID_IMAGES + "token.png");
				break;
			case RED_KEY:
				fis = new FileInputStream(GRID_IMAGES + "red_key.png");
				break;
			case GREEN_KEY:
				fis = new FileInputStream(GRID_IMAGES + "green_key.png");
				break;
			case BLUE_KEY:
				fis = new FileInputStream(GRID_IMAGES + "blue_key.png");
				break;
			case FIRE_BOOTS:
				fis = new FileInputStream(GRID_IMAGES + "fire_boots.png");
				break;
			case FLIPPERS:
				fis = new FileInputStream(GRID_IMAGES + "flippers.png");
				break;
			case ICE_SKATES:
				fis = new FileInputStream(GRID_IMAGES + "ice_skates.png");
				break;
			case LIFE:
				fis = new FileInputStream(GRID_IMAGES + "life.png");
				break;
			default:
				fis = new FileInputStream(GRID_IMAGES + "empty.png");
			}
			return new ImageView(new Image(fis));
		}
		return null;
	}

	/**
	 * gets the image view of the cell depending on its type
	 * @return the image view for the type
	 * @throws FileNotFoundException whenever any of the image 
	 * files cannot be found
	 */
	private ImageView loadCellImage() throws FileNotFoundException {
		// sets the cellImage
		FileInputStream fis;
		switch (type) {
		case WALL:
			fis = new FileInputStream(GRID_IMAGES + "wall.png");
			break;
		case RED_DOOR:
			fis = new FileInputStream(GRID_IMAGES + "redDoor.png");
			break;
		case GREEN_DOOR:
			fis = new FileInputStream(GRID_IMAGES + "greenDoor.png");
			break;
		case BLUE_DOOR:
			fis = new FileInputStream(GRID_IMAGES + "blueDoor.png");
			break;
		case TOKEN_DOOR:
			Random r = new Random();
			int num = r.nextInt(2);
			if (num == 0) {
				fis = new FileInputStream(GRID_IMAGES + "tokenDoor2.png");
				tokens = 2;
			} else {
				fis = new FileInputStream(GRID_IMAGES + "tokenDoor5.png");
				tokens = 5;
			}
			break;
		case FIRE:
			fis = new FileInputStream(GRID_IMAGES + "fire.png");
			break;
		case WATER:
			fis = new FileInputStream(GRID_IMAGES + "water.jpg");
			break;
		case ICE:
			fis = new FileInputStream(GRID_IMAGES + "ice.png");
			break;
		case GOAL:
			fis = new FileInputStream(GRID_IMAGES + "goal.png");
			break;
		case TELEPORTER:
			fis = new FileInputStream(GRID_IMAGES + "teleporter.png");
			break;
		default:
			fis = new FileInputStream(GRID_IMAGES + "empty.png");
		}
		return new ImageView(new Image(fis));
	}

}

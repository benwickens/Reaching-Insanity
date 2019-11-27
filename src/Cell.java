import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Represents a "cell" in the game (wall, empty, fire...)
 * @author Alan Tollett
 * @version 1.0 added attributes and methods (w/out implementation) <br>
 * 1.1 Implemented the constructor and added an ImageView attribute <br>
 */

public class Cell {

	/**	the type of the cell (e.g. wall, fire, door...) */
	private CellType type;
	/** the item which the cell holds (e.g. token, red key...) - normally null*/
	private Collectable item;
	/** an image which can be drawn to the screen representing this cell */
	private ImageView cellImage;
	/** an image which can be drawn to the screen representing this cell's item */
	private ImageView itemImage;
	/** path to the folder contaning the image files */
	private static final String GRID_IMAGES = "src/media/img/grid/";

	/**
	 * Constructs a cell object
	 * @param type the type of cell
	 * @param item the item that the cell holds (null or Collectable.SOME_ITEM)
	 */
	public Cell(CellType type, Collectable item){

		this.type = type;
		this.item = item;

		try {
			FileInputStream fis = null;
			
			// sets the cellImage
			switch (type) {
			case WALL :
				fis = new FileInputStream(GRID_IMAGES + "wall.png");
				break;
			case FIRE:
				fis = new FileInputStream(GRID_IMAGES + "fire.png");
				break;
			case RED_DOOR :
				fis = new FileInputStream(GRID_IMAGES + "RED-DOOR.png");
				break;
			case WATER :
				fis = new FileInputStream(GRID_IMAGES + "empty.png");
				break;
			case ICE :
				fis = new FileInputStream(GRID_IMAGES + "ice.png");
				break;
			case GOAL :
				fis = new FileInputStream(GRID_IMAGES + "empty.png");
				break;
			case BLUE_DOOR :
				fis = new FileInputStream(GRID_IMAGES + "empty.png");
				break;
			case GREEN_DOOR :
				fis = new FileInputStream(GRID_IMAGES + "empty.png");
				break;
			case TELEPORTER:
				fis = new FileInputStream(GRID_IMAGES + "Teleport.png");
				break;
			default: 
				fis = new FileInputStream(GRID_IMAGES + "empty.png");    
			}
			cellImage = new ImageView(new Image(fis));
			
			// sets the item image
			if(item != null) {
				switch (item) {
				case TOKEN :
					fis = new FileInputStream(GRID_IMAGES + "items/token.jpg");
					break;
				case RED_KEY:
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case GREEN_KEY :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case BLUE_KEY :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case FIRE_BOOTS :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case FLIPPERS :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case ICE_SKATES :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case DAGGER :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case SPEAR :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				case SHIELD :
					fis = new FileInputStream(GRID_IMAGES + "empty.png");
					break;
				default: 
					fis = new FileInputStream(GRID_IMAGES + "empty.png");    
				}
				itemImage = new ImageView(new Image(fis));
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: could not load image(s).");
			System.exit(-1);
		}

	}

	public void removeItem() {
		this.item = null;
	}

	public Collectable getItem() {
		return item;
	}

	public void setItem(Collectable item) {
		this.item = item;
	}

	public CellType getType() {
		return type;
	}

	public void setType(CellType type) {
		this.type = type;
	}

	public ImageView getCellImage() {
		return cellImage;
	}

	public void setCellImage(ImageView cellImage) {
		this.cellImage = cellImage;
	}

	public ImageView getItemImage() {
		return itemImage;
	}

	public void setItemImage(ImageView itemImage) {
		this.itemImage = itemImage;
	}

}

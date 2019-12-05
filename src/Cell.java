import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

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
	/**The X location this cell links to (if a teleporter)*/
	private int linkX;
	/**The Y location this cell links to (if a teleporter)*/
	private int linkY;
	
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
			case RED_DOOR :
				fis = new FileInputStream(GRID_IMAGES + "redDoor.png");
				break;
			case GREEN_DOOR :
				fis = new FileInputStream(GRID_IMAGES + "greenDoor.png");
				break;
			case BLUE_DOOR :
				fis = new FileInputStream(GRID_IMAGES + "blueDoor.png");
				break;
			case TOKEN_DOOR :
				fis = new FileInputStream(GRID_IMAGES + "tokenDoor.png");
				break;
			case FIRE:
				fis = new FileInputStream(GRID_IMAGES + "fire.png");
				break;
			case WATER :
				fis = new FileInputStream(GRID_IMAGES + "water.jpg");
				break;
			case ICE :
				fis = new FileInputStream(GRID_IMAGES + "ice.png");
				break;
			case GOAL :
				fis = new FileInputStream(GRID_IMAGES + "goal.png");
				break;
			case TELEPORTER:
				fis = new FileInputStream(GRID_IMAGES + "teleporter.png");
				break;
			default:
				Random r = new Random();
				fis = new FileInputStream(GRID_IMAGES + "empty.png");
				//(r.nextInt(3) + 1) + ".png");    
			}

			cellImage = new ImageView(new Image(fis));
			
			// sets the item image
			if(item != null) {
				switch (item) {
				case TOKEN :
					fis = new FileInputStream(GRID_IMAGES + "token.png");
					break;
				case RED_KEY:
					fis = new FileInputStream(GRID_IMAGES + "red_key.png");
					break;
				case GREEN_KEY :
					fis = new FileInputStream(GRID_IMAGES + "green_key.png");
					break;
				case BLUE_KEY :
					fis = new FileInputStream(GRID_IMAGES + "blue_key.png");
					break;
				case FIRE_BOOTS :
					fis = new FileInputStream(GRID_IMAGES + "fire_boots.png");
					break;
				case FLIPPERS :
					fis = new FileInputStream(GRID_IMAGES + "flippers.png");
					break;
				case ICE_SKATES :
					fis = new FileInputStream(GRID_IMAGES + "ice_skates.png");
					break;
				case LIFE :
					fis = new FileInputStream(GRID_IMAGES + "life.png");
					break;
				default: 
					fis = new FileInputStream(GRID_IMAGES + "empty.png");    
				}
				itemImage = new ImageView(new Image(fis));
			}
		} catch (FileNotFoundException e) {
//			System.out.println("ERROR: could not load image(s).");
//			PopUp a = new PopUp("ERROR: could not load image(s).",false);
//			Stage stageP = new Stage();
//			stageP.setScene(a.getScene());
//			stageP.show();
			e.printStackTrace();
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

	public int getLinkX() {
		return linkX;
	}

	public void setLinkX(int linkX) {
		this.linkX = linkX;
	}

	public int getLinkY() {
		return linkY;
	}

	public void setLinkY(int linkY) {
		this.linkY = linkY;
	}

}

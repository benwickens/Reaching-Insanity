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
    private ImageView image;
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
		
		// sets the imageView for the cell.
		try {
			switch (type) {
			 case WALL :
            	this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "wall.png")));
            	break;
			 case FIRE:
				this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "fire.png")));
				break;
			 case RED_DOOR :
	            	this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "RED-DOOR.png")));
	                break;
            case WATER :
                this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "empty.png")));
                break;
            case ICE :
            	this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "ice.png")));
                break;
            case GOAL :
            	this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "empty.png")));
                break;
            case BLUE_DOOR :
				this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "empty.png")));
				break;
            case GREEN_DOOR :
            	this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "empty.png")));
                break;
            default: 
            	this.image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "empty.png")));    

            //hasn't been implemented yet (
		/*
		case "K:#" :
			result = new Cell(CellType.,null);
			break;

		case "TD" : //token door TD:#
			result = new Cell(CellType.,null);
			break;
		case "T" : // teleporter
			result = new Cell(CellType.,null);
			break;	 */
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

	public ImageView getImage() {
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}

}

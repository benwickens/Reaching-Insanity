import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Cell {
	
	private CellType type;
    private Collectable item;
    private ImageView image;
    private static final String GRID_IMAGES = "src/media/img/grid/";

	public Cell(CellType type, Collectable item){
		this.type = type;
		this.item = item;
		
		try {
			switch(type) {
			case WALL:
				image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "wall.png")));
				break;
			case ICE:
				image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "ice.png")));
				break;
			case FIRE:
				image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "fire.png")));
				break;
			case EMPTY:
				image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "empty.png")));
				break;
			default:
				image = new ImageView(new Image(new FileInputStream(GRID_IMAGES + "empty.png")));
				break;
			}
			
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: could not load image(s).");
			System.exit(-1);
		}
		
	}
	
	public void removeItem() {
		
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

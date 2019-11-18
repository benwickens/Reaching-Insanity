
public class Cell {
	
	CellType type;
    Collectable item;

	Cell(){
		this.type = type;
		this.item = item;
	}
	
	public void removeItem() {
		
	}

	public Collectable getItem() {
		return item;
	}

	public void setItem(Collectable item) {
		this.item = item;
	}

	
}

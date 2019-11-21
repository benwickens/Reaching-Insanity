
public class Cell {
	
	private CellType type;
    private Collectable item;

	public Cell(CellType type, Collectable item){
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

	public CellType getType() {
		return type;
	}

	public void setType(CellType type) {
		this.type = type;
	}

	
}


public abstract class Enemy extends Character{
	private String type;
	
	public abstract void move();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}

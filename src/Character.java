
public abstract class Character {
	
	protected int x;
	protected int y;
	
	public void moveTo(int exactX, int exactY) {
		x = exactX;
		y = exactY;
	}
	
	public String toString() {
		return null;
	}

	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}
}
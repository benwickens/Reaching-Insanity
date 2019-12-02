/*
 * started 27th Nov
 */
import java.util.*;
public class SmartTargettingEnemy extends Character {

	public SmartTargettingEnemy(int x, int y) {
		super(x, y, "Enemy5.png");
	}
	
	public int[] getMove(Player player, char[][] map){
		int playerX = player.getX();
		int playerY = player.getY();
		char[] dirs  = {'u', 'd', 'l', 'r'};
		Queue<String> q = new LinkedList<>();
		q.add("");
		int[] outCoOrd = new int[2];
		while (!coordsMatch(playerX, playerY, q.peek())) {
				String start = q.remove();
					for (char dir:dirs) {			
						String newDir = start + dir;
						//add validation of direction
						if (checkValid(newDir, map)) {
							q.add(newDir);
						}
					}
		}
		System.out.println(q.peek());
		outCoOrd[0] = this.x + char2Coords(q.peek().charAt(0))[0];
		outCoOrd[1] = this.y + char2Coords(q.peek().charAt(0))[1];
		return outCoOrd;	
	}
	
	private Boolean checkValid(String start, char[][] map) {
		int length = start.length();
		int[] coords = movesNewCoOrd(start);
		int endX = coords[0];
		int endY = coords[1];
		if (map[endX][endY] == '_') {
//		if (map[x][y].getType() == CellType.EMPTY) {
			return true;
		} else {
			return false;
		}
	}
	
	private int[] movesNewCoOrd (String start) {
		int length = start.length();
		int endX = this.x;
		int endY = this.y;
		for (int x = 0; x < length; x++) {
			int[] coords = char2Coords(start.charAt(x));
			endX = endX + coords[0];
			endY = endY + coords[1];
		}
		int[] outCoOrd = new int[2];
		outCoOrd[0] = endX;
		outCoOrd[1] = endY;
		return outCoOrd;
	}
	
	private int[] char2Coords (char inChar) {
		int[][] coords = {{0,-1}, {0,1}, {-1,0}, {1,0}};
		switch (inChar) {
		case 'u' :
			return coords[0];
		case 'd' :
			return coords[1];
		case 'l' :
			return coords[2];
		case 'r' :
			return coords[3];
		default :
			return null;
		}
	}
	
	private boolean coordsMatch (int playerX, int playerY, String start) {
		int length = start.length();
		int endX = this.x;
		int endY = this.y;
		for (int x = 0; x < length; x++) {
			int[] coords = char2Coords(start.charAt(x));
			endX = endX + coords[0];
			endY = endY + coords[1];
		}
		if (endX == playerX && endY == playerY) {
			return true;
		} else {
			return false;
		}
	}
	
}
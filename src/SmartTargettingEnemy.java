/*
 * Author: Ben Wickens
 * 
 * 
 */
import java.util.*;

public class SmartTargettingEnemy extends Character {
	private Player p;

	public SmartTargettingEnemy(int x, int y, Player p) {
		super(x, y, "STE.png");
		this.p = p;
	}

	public void updatePlayerLoc(Player in) {
		this.p = in;
	}

	private boolean hasCellVisited(String start, ArrayList<int[]> cellsVisited) {
		int[] coords = movesNewCoOrd(start); //start = up?
		boolean visited = false;
		for (int c = 0; c < cellsVisited.size(); c++) {
			if (coords[0] == cellsVisited.get(c)[0] && coords[1] == cellsVisited.get(c)[1]) {
				visited = true;
			}
		}
		return visited;
	}

	public int[] getMove(Cell[][] map){ //Replace char with Cell
		int playerX = p.getX();
		int playerY = p.getY();
		char[] dirs  = {'u', 'd', 'l', 'r'};
		ArrayList<int[]> cellsVisited = new ArrayList<>();
		Queue<String> q = new LinkedList<>();
		q.add("");
		int[] outCoOrd = new int[2];
		while (!q.isEmpty() && !coordsMatch(playerX, playerY, q.peek())) {
			String start = q.remove();
			for (char dir:dirs) {			
				String newDir = start + dir;
				char ch = newDir.charAt(0);
				int[] dirToCoord = char2Coords(ch);
				if (!(hasCellVisited(newDir, cellsVisited)) &&  (checkValid(newDir, map))) { 
					q.add(newDir);
					cellsVisited.add(movesNewCoOrd(newDir));
				}
			}
		}

		System.out.println(q.peek());
		if (!q.isEmpty()) {
			outCoOrd[0] = this.x + char2Coords(q.peek().charAt(0))[0];
			outCoOrd[1] = this.y + char2Coords(q.peek().charAt(0))[1];
		} else {
			Boolean valid = false;

			while (!valid) {
				Random random = new Random();
				int randomInteger = random.nextInt(3);
				String move = "" + dirs[randomInteger];
				valid =  checkValid(move, map);
				outCoOrd[0] = this.x + char2Coords(move.charAt(0))[0];
				outCoOrd[1] = this.y + char2Coords(move.charAt(0))[1];

			}

		}

		return outCoOrd;

	}

	private Boolean checkValid(String start, Cell[][] map) { //Replace char with Cell
		int length = start.length();
		int[] coords = movesNewCoOrd(start);
		int endX = coords[0];
		int endY = coords[1];
		//if (map[endX][endY] == '_') {
		if (map[y][x].getType() == CellType.EMPTY) { // use this instead // CHANGED X AND Y
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

	@Override
	public void move(Cell[][] grid) {
		int[] output = new int[2];
		output = getMove(grid);
		
			x = output[0];
			y = output[1];



	}

}
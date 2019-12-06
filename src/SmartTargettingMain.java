//TESTING TO BE REMOVED
import java.util.*;
public class SmartTargettingMain {

	public static void main(String[] args) {
		SmartTargettingEnemy test = new SmartTargettingEnemy(1,2);
		Player player = new Player(4,4);
		char[][] map = {
				{'W', 'W', 'W', 'W', 'W', 'W'},
				{'W', '_', '_', 'W', '_', 'W'},
				{'W', '_', '_', 'W', '_', 'W'},
				{'W', '_', '_', 'W', '_', 'W'},
				{'W', '_', '_', 'W', '_', 'W'},
				{'W', 'W', 'W', 'W', 'W', 'W'}
		};
		for (int y = 0; y < 6; y++) {
			for (int x = 0; x < 6; x++) {
				if (x == test.getX() && y == test.getY()) {
					System.out.print("E");
				} else if (x == player.getX() && y == player.getY()) {
					System.out.print("P");
				} else {
					System.out.print(map[x][y]);
				}
			}
			System.out.println();
		}
		int[] nextMove = test.getMove(player, map);
		System.out.println(nextMove[0] + "," + nextMove[1]);
	}
}

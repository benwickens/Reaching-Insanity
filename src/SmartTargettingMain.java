//TESTING TO BE REMOVED
public class SmartTargettingMain {

	public static void main(String[] args) {
		SmartTargettingEnemy test = new SmartTargettingEnemy(1,1,Direction.RIGHT);
		String[][] map = new String[4][4];
		for (int i = 0; i < 4; i++) {
			for (int x = 0; x < 4; x++) {
				String input1 = Integer.toString(x);
				String input2 = Integer.toString(i);
				map[i][x] = input1 + " . " + input2;
			}
		test.adjacenyMatrix(map);
		
		}
	}
}

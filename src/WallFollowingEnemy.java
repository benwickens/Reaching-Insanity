
/*
 * Author Ben Wickens
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WallFollowingEnemy extends Character{
	private Direction findWall;
	private Cell[][] map;
	private Direction startDir;
	
	
	public WallFollowingEnemy(int x, int y, Direction startingDir) {
		super(x,y, "WFE.png");
		findWall = detectWall();
		startDir = startingDir;
	}
	
	//detects where the wall is to follow.
	private Direction detectWall() {
		Boolean valid = false;
		int[] wallCoords = new int[2];
		char[] direction  = {'u', 'd', 'l', 'r'};
		while (!valid) {
			Random random = new Random();
			int randomInteger = random.nextInt(3);
			char dir = direction[randomInteger];
			int tempX = this.x + char2Coords(dir)[0];
			int tempY = this.y + char2Coords(dir)[1]; 
			if (map[tempX][tempY].getType() == CellType.WALL) {
				valid = true;
				return  char2Direction(dir);
			}
		}
		return null;
	}
	
	private Direction char2Direction(char inChar) {
		switch (inChar) {
		case 'u' :
			return Direction.UP;
		case 'd' :
			return Direction.DOWN;
		case 'l' :
			return Direction.LEFT;
		case 'r' :
			return Direction.RIGHT;
		default :
			return null;
		}
	}
	
	private int[] char2Coords(char inChar) {
		int[][] Coords = {{0,-1}, {0,1}, {-1,0}, {1,0}};
		switch (inChar) {
		case 'u' :
			return Coords[0];
		case 'd' :
			return Coords[1];
		case 'l' :
			return Coords[2];
		case 'r' :
			return Coords[3];
		default :
			return null;
		}
	}
	
	//Maybe should be a switch but cba to find way round. soz
	private int[] direction2Coords(Direction in) {
		int[] Coords = new int[2];
		
		if (in == Direction.UP) {
			Coords[0] = -1;
			Coords[1] = 0;
			return Coords;
		}
		
		if (in == Direction.DOWN) {
			Coords[0] = 1;
			Coords[1] = 0;
			return Coords;
		}
		
		if (in == Direction.LEFT) {
			Coords[0] = 0;
			Coords[1] = -1;
			return Coords;
		}

		if (in == Direction.RIGHT) {
			Coords[0] = 0;
			Coords[1] = 1;
			return Coords;
		}
		
		return null;
	}
	
	public int[] getMove(Cell[][] map) {
		int[] outCoord = new int[2]; //return value 
		int tempX;
		int tempY;
		if (validWallOnMove()) {
			tempX = this.x + direction2Coords(startDir)[0];
			tempY = this.y + direction2Coords(startDir)[1]; 
			//check if this is a valid move to go to
			if (map[tempX][tempY].getType() == CellType.EMPTY) {
				outCoord[0] = tempX;
				outCoord[1] = tempY;
				
			}
		}
		
		return outCoord;
	}
	

	/*
	 * If there is a 
	 */
	public boolean validWallOnMove() {
		int[] Coords = new int[2];
		int tempX = this.x + direction2Coords(findWall)[0];
		int tempY = this.y + direction2Coords(findWall)[1]; 
		if (map[x][y].getType() == CellType.EMPTY) {
			return true;
		} else {
			return false;
		}
		
	}
	//only in bc breaks otherwise
	public void move(Cell[][] grid){}
}
//
//    private Direction currentDirection;
//    private int nextX;
//    private int nextY;
//    private Cell nextUp;
//    private Cell nextDown;
//    private Cell nextLeft;
//    private Cell nextRight;
//
//
//    public WallFollowingEnemy(int x, int y, Direction startingDirection){
//        super(x, y, "WFE.png");
//        currentDirection = startingDirection;        
//    }
//    
//    
//    
//
//    private void setNextXYUp() {
//        if(nextUp.getType().equals(CellType.EMPTY)) {
//        	nextY = y - 1;
//        }else {
//        	// change direction, preferably not the opposite
//        	if(nextLeft.getType().equals(CellType.EMPTY)) {
//        		nextX = x - 1;
//        		currentDirection = Direction.LEFT;
//        	}else {
//        		if(nextRight.getType().equals(CellType.EMPTY)) {
//        			nextX = x + 1;
//        			currentDirection = Direction.RIGHT;
//        		}else {
//        			nextY = y + 1;
//        			currentDirection = Direction.DOWN;
//        		}
//        	}
//        }
//    }
//    
//    private void setNextXYDown() {
//		if(nextDown.getType().equals(CellType.EMPTY)) {
//			nextY = y + 1;
//		}else {
//			// change direction, preferably not the opposite
//			if(nextLeft.getType().equals(CellType.EMPTY)) {
//				nextX = x - 1;
//				currentDirection = Direction.LEFT;
//			}else {
//				if(nextRight.getType().equals(CellType.EMPTY)) {
//					nextX = x + 1;
//					currentDirection = Direction.RIGHT;
//				}else {
//					nextY = y - 1;
//					currentDirection = Direction.UP;
//				}
//			}
//		}
//    }
//    
//    private void setNextXYLeft() {
//		if(nextLeft.getType().equals(CellType.EMPTY)) {
//			nextX = x - 1;
//		}else {
//			// change direction, preferably not the opposite
//			if(nextUp.getType().equals(CellType.EMPTY)) {
//				nextY = y - 1;
//				currentDirection = Direction.UP;
//			}else {
//				if(nextDown.getType().equals(CellType.EMPTY)) {
//					nextY = y +  1;
//					currentDirection = Direction.DOWN;
//				}else {
//					nextX = x + 1;
//					currentDirection = Direction.RIGHT;
//				}
//			}
//		}
//    }
//    
//    private void setNextXYRight() {
//		if(nextRight.getType().equals(CellType.EMPTY)) {
//			nextX = x + 1;
//		}else {
//			// change direction, preferably not the opposite
//			if(nextUp.getType().equals(CellType.EMPTY)) {
//				nextY = y - 1;
//				currentDirection = Direction.UP;
//			}else {
//				if(nextDown.getType().equals(CellType.EMPTY)) {
//					nextY = y + 1;
//					currentDirection = Direction.DOWN;
//				}else {
//					nextX = x - 1;
//					currentDirection = Direction.LEFT;
//				}
//			}
//		}
//    }
//    
//    private void updateNextXY() {
//    	switch(currentDirection) {
//    	case UP:
//    		setNextXYUp();
//            break;
//    	case DOWN:
//    		setNextXYDown();
//    		break;
//    	case LEFT:
//    		setNextXYLeft();
//    		break;
//    	case RIGHT:
//    		setNextXYRight();
//    		break;
//    	default:
//    		break;
//    	}
//    }
//    
//    public void move(Cell [][] grid){   	
//    	Cell[] neighbours = getNeighbours(grid, x, y);
//    	nextUp = neighbours[0];
//    	nextDown = neighbours[5];
//    	nextLeft = neighbours[3];
//    	nextRight = neighbours[4];
//    	
//    	nextX = x;
//    	nextY = y;
//    	
//    	updateNextXY(); // picks a nextX and nextY such that cell is not a barrier
//    	
//    	//remove these two lines when un-commenting
////    	x = nextX;
////    	y = nextY;
//    	
//    	
//    	if(directlyConnectedToBarrier(grid, nextX, nextY)) {
//    		System.out.println("directly connected to barrier so can move");
//        	x = nextX;
//        	y = nextY;
//    	}else {
//    		System.out.println("next cell not directly connected to wall");
//    		if(connectedToBarrier(grid, nextX, nextY)) {
//    			System.out.println("connected diagonaly to wall so change dir");
//    			
//    			
//    			for(Direction d : Direction.values()) {
//    				if(d != currentDirection) {
//    					currentDirection = d;
//    					System.out.println("trying " + d);
//    					move(grid);
//    					break;
//    				}
//    			}
//    			
//    		}
//    	}
//    }
//
//    private Cell[] getNeighbours(Cell[][] grid, int nextX, int nextY) {
//    	Cell[] neighbours = new Cell[8];
//    	
//    	neighbours[0] = grid[nextX][nextY - 1]; // up
//    	neighbours[1] = grid[nextX - 1][nextY - 1]; // up left
//    	neighbours[2] = grid[nextX + 1][nextY - 1]; // up right
//    	
//    	neighbours[3] = grid[nextX - 1][nextY]; // center left
//    	neighbours[4] = grid[nextX + 1][nextY]; // center right
//    	
//    	neighbours[5] = grid[nextX][nextY + 1]; // down
//    	neighbours[6] = grid[nextX - 1][nextY + 1]; // down left
//    	neighbours[7] = grid[nextX + 1][nextY + 1]; // down right
//    	
//    	return neighbours;
//    }
//    
//    private Cell[] getDirectNeighbours(Cell[][] grid, int nextX, int nextY) {
//    	Cell[] neighbours = new Cell[8];
//    	
//    	neighbours[0] = grid[nextX][nextY - 1];  //up
//    	neighbours[1] = grid[nextX - 1][nextY]; // left
//    	neighbours[2] = grid[nextX + 1][nextY]; // right
//    	neighbours[3] = grid[nextX][nextY + 1]; // down
//    	
//    	return neighbours;
//    }
//    
//    private boolean connectedToBarrier(Cell[][] grid, int nextX, int nextY) {
//    	Cell[] neighbours = getDirectNeighbours(grid, nextX, nextY);
//    	
//    	for(Cell c : neighbours) {
//        	if(!c.getType().equals(CellType.EMPTY)) {
//        		return true;
//        	}
//    	}
//    	return false;
//    }
//    
//    private boolean directlyConnectedToBarrier(Cell[][] grid, int nextX, int nextY) {
//    	Cell[] neighbours = getDirectNeighbours(grid, nextX, nextY);
//    	
//    	for(Cell c : neighbours) {
//        	if(!c.getType().equals(CellType.EMPTY)) {
//        		return true;
//        	}
//    	}
//    	return false;
//    }
//    
//    
//	public Direction getCurrentDirection() {
//		return currentDirection;
//	}
//
//}
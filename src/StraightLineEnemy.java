/**
 * File name: StraightLineEnemy.java
 *
 * @version 1.3
 * Creation Date: 18/11/2019
 * Last Modification date: 2/12/2019
 * @author Gursimran Randhawa, Yassine Abdalass
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * Cover the implementation of the move method for all
 * possible directions that a StraightLineEnemy can move in.
 * <br>
 * Version History
 * 1.0 - basic structure and methods laid out
 * 1.1 - started the move method implementation
 * 1.2 - changed the move method to use the new direction enum.
 * 1.3 - Fixed the Y position alterations in the move method for up and down, so that
 * for up the y value is decreased by 1 (rather than increased) and vice versa for down.
 */

public class StraightLineEnemy extends Character{

    /* This enemy simply moves in a straight line, hits an obstacle and
    reverses direction. Each straight line enemy either moves vertically
    or horizontally over the map.
    vertical - y incremented by 1 , horizontal - x incremented by 1
    then decrement by 1 once a wall is hit
     */
    private Direction currentDirection;

    public StraightLineEnemy(int x, int y, Direction currentDirection){
        super(x, y, "SLE.png");
        this.currentDirection = currentDirection;
    }

    public void move(Cell [][] grid){
    	boolean canMoveHorizontally = true;
    	CellType nextType;
    	
    	
        if(currentDirection.equals(Direction.RIGHT)){
            nextType = grid[x + 1][y].getType();
            if(nextType.equals(CellType.EMPTY)){
                x += 1;
                System.out.println("SLE moved right");
            }else{
            	nextType = grid[x - 1][y].getType();
            	if(nextType.equals(CellType.EMPTY)) {
            		currentDirection = Direction.LEFT;
                    x -= 1;
                    System.out.println("SLE moved left");
            	}else {
            		canMoveHorizontally = false;
            		currentDirection = Direction.UP;
                    System.out.println("SLE now going up");
            	}
            }
        }else if(currentDirection.equals(Direction.LEFT)){
            nextType = grid[x - 1][y].getType();
            if(nextType.equals(CellType.EMPTY)){
                x -= 1;
                System.out.println("SLE moved left");
            }else{
            	nextType = grid[x + 1][y].getType();
            	if(nextType.equals(CellType.EMPTY)) {
            		currentDirection = Direction.RIGHT;
                    x += 1;
                    System.out.println("SLE moved right");
            	}else {
            		canMoveHorizontally = false;
            		currentDirection = Direction.UP;
                    System.out.println("SLE now going up");
            	}
            }
        }else {
        	canMoveHorizontally = false;
        }
            
        if(!canMoveHorizontally) {
            System.out.println("couldnt move horizontally so");
            if(currentDirection.equals(Direction.UP)){
                nextType = grid[x][y - 1].getType();
                if(nextType.equals(CellType.EMPTY)){
                    System.out.println("SLE moved up");
                    y -= 1;
                }else{
                	nextType = grid[x][y + 1].getType();
                	if(nextType.equals(CellType.EMPTY)) {
                		currentDirection = Direction.DOWN;
                        y += 1;
                        System.out.println("SLE moved down");
                	}
                }
            }else if(currentDirection.equals(Direction.DOWN)){
                nextType = grid[x][y + 1].getType();
                if(nextType.equals(CellType.EMPTY)){
                    y += 1;
                    System.out.println("SLE moved down");
                }else{
                	nextType = grid[x][y - 1].getType();
                	if(nextType.equals(CellType.EMPTY)) {
                		currentDirection = Direction.UP;
                        y -= 1;
                        System.out.println("SLE moved up");
                	}
                }
            }
        }
    }
}
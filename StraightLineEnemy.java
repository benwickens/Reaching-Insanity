/**
 * File name: StraightLineEnemy.java
 *
 * @version 1.2
 * Creation Date: 18/11/2019
 * Last Modification date: 23/11/2019
 * @author Gursimran Randhawa, Yassine Abdalass, Millie Quinn
 * <br>
 * No copyright.
 * <br>
 * Purpose:
 * Cover the implementation of the move method for all
 * possible directions that a StraightLineEnemy can move in.
 * <br>
 * Version History
 * 1.0 - basic structure and methods laid out
 * 1.1 - started the move method implemention
 * 1.2 - changed the move method to use the new direction enum.
 *
 */

public class StraightLineEnemy extends Enemy{

    /* This enemy simply moves in a straight line, hits an obstacle and
    reverses direction. Each straight line enemy either moves vertically
    or horizontally over the map.
    vertical - y incremented by 1 , horizontal - x incremented by 1
    then decrement by 1 once a wall is hit
     */
    private Direction currentDirection;

    public StraightLineEnemy(int xLocation, int yLocation, String enemyType, Direction currentDirection){
        super(xLocation, yLocation, enemyType);
        this.currentDirection = currentDirection;
    }

    public void move(Cell [][] grid){
        //vertical movement
        if(currentDirection.equals(Direction.RIGHT)){
            int nextX = this.xLocation + 1;
            int nextY = this.yLocation;

            CellType nextType = grid[nextX][nextY].getType();
            if(nextType.equals(CellType.EMPTY)){
                this.xLocation += 1;
            }else{
                currentDirection = Direction.LEFT;
                this.xLocation -= 1;
            }
        }else if(currentDirection.equals(Direction.LEFT)){
            int nextX = this.xLocation - 1;
            int nextY = this.yLocation;

            CellType nextType = grid[nextX][nextY].getType();
            if(nextType.equals(CellType.EMPTY)){
                this.xLocation -= 1;
            }else{
                currentDirection = Direction.RIGHT;
                this.xLocation += 1;
            }
        }else if(currentDirection.equals(Direction.UP)){
            int nextX = this.xLocation;
            int nextY = this.yLocation + 1;

            CellType nextType = grid[nextX][nextY].getType();
            if(nextType.equals(CellType.EMPTY)){
                this.yLocation += 1;
            }else{
                currentDirection = Direction.DOWN;
                this.yLocation -= 1;
            }
        }else{
            int nextX = this.xLocation;
            int nextY = this.yLocation - 1;

            CellType nextType = grid[nextX][nextY].getType();
            if(nextType.equals(CellType.EMPTY)){
                this.yLocation -= 1;
            }else{
                currentDirection = Direction.UP;
                this.yLocation += 1;
            }
        }
    }
}
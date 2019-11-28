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

public class StraightLineEnemy extends Character{

    /* This enemy simply moves in a straight line, hits an obstacle and
    reverses direction. Each straight line enemy either moves vertically
    or horizontally over the map.
    vertical - y incremented by 1 , horizontal - x incremented by 1
    then decrement by 1 once a wall is hit
     */
    private Direction currentDirection;

    public StraightLineEnemy(int x, int y, Direction currentDirection){
        super(x, y, "Enemy1.png");
        this.currentDirection = currentDirection;
    }

    public void move(Cell [][] grid){
        if(currentDirection.equals(Direction.RIGHT)){
            CellType nextType = grid[x + 1][y].getType();
            if(nextType.equals(CellType.EMPTY)){
                x += 1;
            }else{
                currentDirection = Direction.LEFT;
                x -= 1;
            }
        }else if(currentDirection.equals(Direction.LEFT)){
            CellType nextType = grid[x - 1][y].getType();
            if(nextType.equals(CellType.EMPTY)){
                x -= 1;
            }else{
                currentDirection = Direction.RIGHT;
                x += 1;
            }
        }else if(currentDirection.equals(Direction.UP)){
            CellType nextType = grid[x][y + 1].getType();
            if(nextType.equals(CellType.EMPTY)){
                y += 1;
            }else{
                currentDirection = Direction.DOWN;
                y -= 1;
            }
        }else{
            CellType nextType = grid[x][y - 1].getType();
            if(nextType.equals(CellType.EMPTY)){
                y -= 1;
            }else{
                currentDirection = Direction.UP;
                y += 1;
            }
        }
    }
}
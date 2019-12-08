

/**
 * A Enemy that follows the wall
 * 
 * @version 3.0
 * @author Robbie Ko
 *
 */


public class WallFollowingEnemy extends Character {
	
	/**A Horizontal direction tracker*/
	private Direction currtDir;
	
	/** A Veritical direction tracker*/
	private Direction vertiDir;
	
	/** A reference back to the grid*/
	private Cell [] [] grid;
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param startDir
	 * @param vertiDir
	 */
	public WallFollowingEnemy(int x, int y, 
			Direction startDir,Direction vertiDir) {
		super(x, y, "WFE.png");
		currtDir = startDir;
		this.vertiDir = vertiDir;
	}
	
	/**
	 * The move that Moves the enemy
	 */
	@Override
	public void move(Cell[][] grid) {
		this.grid = grid;
		validWallOnMove();
	}
	
	/**
	 * Method that checks the top Cell type
	 * @param cellType
	 * @return true or false 
	 */
	private boolean directionUP(CellType cellType) {
		CellType nextType = grid[x][y - 1].getType();
		if (nextType.equals(cellType)) {
			if(nextType != CellType.WALL || nextType != CellType.EMPTY){
				nextType.equals(CellType.WALL);
			}else{
				if (nextType.equals(cellType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that checks the top left Cell type
	 * @param cellType
	 * @return true or false 
	 */
	private boolean directionUPLeft(CellType cellType) {
		CellType nextType = grid[x - 1][y - 1].getType();
		if (nextType.equals(cellType)) {
			if(nextType != CellType.WALL || nextType != CellType.EMPTY){
				nextType.equals(CellType.WALL);
			}else{
				if (nextType.equals(cellType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that checks top right Cell type
	 * @param cellType
	 * @return true or false
	 */
	private boolean directionUPRight(CellType cellType) {
		CellType nextType = grid[x + 1][y - 1].getType();
		if (nextType.equals(cellType)) {
			if(nextType != CellType.WALL || nextType != CellType.EMPTY){
				nextType.equals(CellType.WALL);
			}else{
				if (nextType.equals(cellType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that checks right Cell type
	 * @param cellType
	 * @return true or false
	 */
	private boolean directionRight(CellType cellType) {
		CellType nextType = grid[x + 1][y].getType();
		if (nextType.equals(cellType)) {
			if(nextType != CellType.WALL || nextType != CellType.EMPTY){
				nextType.equals(CellType.WALL);
			}else{
				if (nextType.equals(cellType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method checks the left Cell type
	 * @param cellType
	 * @return true or false
	 */
	private boolean directionLeft(CellType cellType) {
		CellType nextType = grid[x - 1][y].getType();
		if (nextType.equals(cellType)) {
			if(nextType != CellType.WALL || nextType != CellType.EMPTY){
				nextType.equals(CellType.WALL);
			}else{
				if (nextType.equals(cellType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that checks down left Cell type
	 * @param cellType
	 * @return true or false
	 */
	private boolean directionDownLeft(CellType cellType) {
		CellType nextType = grid[x - 1][y + 1].getType();
		if (nextType.equals(cellType)) {
			if(nextType != CellType.WALL || nextType != CellType.EMPTY){
				nextType.equals(CellType.WALL);
			}else{
				if (nextType.equals(cellType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that Checks down Cell type
	 * @param cellType
	 * @return true or false
	 */
	private boolean directionDown(CellType cellType) {
		CellType nextType = grid[x][y + 1].getType();
		if (nextType.equals(cellType)) {
			if(nextType != CellType.WALL || nextType != CellType.EMPTY){
				nextType.equals(CellType.WALL);
			}else{
				if (nextType.equals(cellType)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * Method that checks down right Cell type
	 * @param cellType
	 * @return true or false
	 */
	private boolean directionDownRight(CellType cellType) {
		CellType nextType = grid[x + 1][y + 1].getType();
		if(nextType != CellType.WALL || nextType != CellType.EMPTY){
			nextType.equals(CellType.WALL);
		}else{
			if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
		}
		return false;
	}

	

	/**
	 *  A Method to decides which direction that it should move to 
	 * 
	 * This method has gone over the soft cap limit
	 *  as this is not possible to break down
	 * @return the movement for the enemy
	 */
	private int validWallOnMove() {
        if (directionUP(CellType.WALL)) {
            if (directionLeft(CellType.WALL) && directionRight(CellType.WALL) 
            		&& directionDown(CellType.EMPTY)){
                vertiDir = Direction.DOWN;
                return y+=1;
            } else if(directionLeft(CellType.WALL) 
            		&& directionDown(CellType.WALL)){
                currtDir = Direction.RIGHT;
                return x +=1;
            }else if(directionRight(CellType.WALL) 
            		&& directionDown(CellType.WALL)){
                currtDir = Direction.LEFT;
                return x-=1;
            }else if (directionRight(CellType.EMPTY) 
            		&& directionLeft(CellType.EMPTY)){
                if(currtDir == Direction.RIGHT){
                   vertiDir = Direction.UP;
                   return x +=1;
                }else{
                    vertiDir = Direction.UP;
                    return x-=1;
                }
            }else if(directionUPRight(CellType.EMPTY) 
            		&& directionLeft(CellType.WALL)){
                if(vertiDir == Direction.UP && currtDir == Direction.RIGHT){
                    currtDir = Direction.RIGHT;
                    return x+=1;
                }else{
                    if(vertiDir == Direction.DOWN
                    		&& !directionLeft(CellType.WALL)){
                       return x+=1;
                    }else {
                        vertiDir = Direction.DOWN;
                        return y += 1;
                    }
                }
            }else if(directionUPLeft(CellType.EMPTY) 
            		&& directionRight(CellType.WALL)) {
                if (vertiDir == Direction.UP && currtDir == Direction.LEFT) {
                    return x -= 1;
                } else if (vertiDir == Direction.UP 
                		&& currtDir == Direction.RIGHT) {
                    return x += 1;
                }else if(vertiDir == Direction.DOWN 
                		&& currtDir ==Direction.RIGHT){
                    vertiDir= Direction.DOWN;
                    return y+=1;
                }else{
                    vertiDir = Direction.UP;
                    return y+=1;
                }
            }else if(directionLeft(CellType.WALL) 
            		&& directionRight(CellType.EMPTY) 
            		&& directionUPRight(CellType.WALL)){
                if(directionDown(CellType.EMPTY) 
                		&& directionDownRight(CellType.WALL)){
                    if(currtDir == Direction.LEFT ){
                        vertiDir = Direction.DOWN;
                        return y+=1;
                    }else {
                        currtDir = Direction.RIGHT;
                        return x += 1;
                    }
                }else if (currtDir == Direction.LEFT 
                		&& vertiDir == Direction.UP){
                    currtDir = Direction.RIGHT;
                    vertiDir = Direction.DOWN;
                    return y+=1;
                }else{
                    if (currtDir==Direction.LEFT 
                    		&& vertiDir == Direction.DOWN){
                        currtDir = Direction.RIGHT;
                        return y+=1;
                    }
                    vertiDir = Direction.DOWN;
                    currtDir = Direction.RIGHT;
                    return x+=1;
                }
            }else if (directionRight(CellType.WALL)
            		&&directionLeft(CellType.EMPTY)
            		&& directionUPLeft(CellType.WALL)){
                if(currtDir == Direction.LEFT && vertiDir== Direction.UP){
                    return x -=1;
                }else{
                    if(directionDownLeft(CellType.WALL) 
                    		&& vertiDir == Direction.UP 
                    		&& currtDir == Direction.RIGHT){
                        currtDir = Direction.LEFT;
                        vertiDir = Direction.DOWN;
                        return y+=1;
                    }else {
                        currtDir = Direction.LEFT;
                        vertiDir = Direction.DOWN;
                        return y += 1;
                    }
                }
            }
        } else if (directionLeft(CellType.WALL) 
        		&& !directionRight(CellType.WALL)) {
            if (directionDown(CellType.EMPTY) && directionUP(CellType.EMPTY)){
             if(vertiDir == Direction.UP){
                 currtDir = Direction.RIGHT;
                 return y-=1;
             }else{
                 currtDir = Direction.RIGHT;
                 return y+=1;
             }
            }else if(directionDown(CellType.WALL)
            		&&directionDownRight(CellType.WALL) 
            		&& directionRight(CellType.EMPTY)){
                if(directionUPRight(CellType.WALL)
                		&&directionUP(CellType.EMPTY)){
                    if(currtDir == Direction.LEFT && vertiDir == Direction.UP){
                        return y-=1;
                    }else {
                        currtDir = Direction.RIGHT;
                        return x += 1;
                    }
                }else if (currtDir == Direction.LEFT 
                		&& vertiDir == Direction.UP){
                    vertiDir = Direction.UP;
                    currtDir = Direction.RIGHT;
                    return y-=1;
                }else{
                    if(currtDir == Direction.LEFT 
                    		&& vertiDir== Direction.DOWN){
                        vertiDir = Direction.UP;
                        currtDir = Direction.RIGHT;
                        return y-=1;
                    }else {
                        currtDir = Direction.RIGHT;
                        return x += 1;
                    }
                }
            }else if(directionDown(CellType.WALL)
            		&&directionDownRight(CellType.EMPTY)){
                if (vertiDir == Direction.DOWN){
                    return x+=1;
                }else{
                    if (currtDir == Direction.LEFT) {
                        vertiDir = Direction.UP;
                        return y-=1;
                    }else {
                        vertiDir = Direction.UP;
                        return y += 1;
                    }
                }
            }else if(directionRight(CellType.WALL)
            		&&directionDown(CellType.WALL)){
                    vertiDir =Direction.UP;
                    return y-=1;
            }
        } else if (directionRight(CellType.WALL)) {
            if(directionLeft(CellType.WALL)
            		&& directionDown(CellType.WALL)){
                vertiDir = Direction.UP;
                currtDir = Direction.RIGHT;
                return y-=1;
            }else  if (directionDown(CellType.EMPTY) 
            		&& directionUP(CellType.EMPTY)){
                if(vertiDir == Direction.UP){
                    if(directionUPRight(CellType.EMPTY)){
                        currtDir = Direction.LEFT;
                    }
                    currtDir = Direction.LEFT;
                    return y-=1;
                }else{
                    currtDir = Direction.LEFT;
                    vertiDir = Direction.DOWN;
                    return y+=1;
                }
            }else if(directionDown(CellType.WALL)
            		&&directionDownLeft(CellType.WALL) 
            		&& directionLeft(CellType.EMPTY)) {
                if(directionUPLeft(CellType.WALL) 
                		&& directionUP(CellType.EMPTY)){
                    if(vertiDir == Direction.DOWN) {
                        currtDir = Direction.LEFT;
                        return x -=1;
                    }else {
                        vertiDir = Direction.UP;
                        return y -= 1;
                    }
                }else if (currtDir == Direction.RIGHT 
                		&& vertiDir == Direction.DOWN) {
                    currtDir = Direction.LEFT;
                    vertiDir = Direction.UP;
                    return y -= 1;
                }else if(currtDir == Direction.RIGHT 
                		&& directionUPLeft(CellType.WALL)
                		&& directionDown(CellType.WALL)){
                    currtDir = Direction.LEFT;
                    return x -=1;
            }else{
                    if(currtDir == Direction.LEFT){
                        return x-=1;
                    }else {
                        vertiDir = Direction.UP;
                        return x += 1;
                    }
                }
            }else if(directionDown(CellType.WALL)
            		&&directionDownLeft(CellType.EMPTY)){
                if (vertiDir == Direction.DOWN){
                    currtDir = Direction.RIGHT;
                    return x-=1;
                }else{
                    if (currtDir == Direction.RIGHT) {
                        currtDir = Direction.RIGHT;
                        vertiDir = Direction.UP;
                        return y-=1;
                    }else {
                        currtDir = Direction.RIGHT;
                        vertiDir = Direction.UP;
                        return y += 1;
                    }
                }
            }
        } else if (directionDown(CellType.WALL)) {
            if(directionLeft(CellType.EMPTY)&&directionRight(CellType.EMPTY)){
                if (currtDir == Direction.RIGHT){
                    vertiDir = Direction.DOWN;
                    return x +=1;
                }else{
                    vertiDir = Direction.DOWN;
                    return x-=1;
                }
            }else if(directionLeft(CellType.EMPTY)
            		&&directionDownLeft(CellType.EMPTY)){
                vertiDir =Direction.DOWN;
                return x-=1;
            }else if(directionRight(CellType.EMPTY)
            		&&directionDownRight(CellType.EMPTY)){
                vertiDir = Direction.DOWN;
                return x+=1;
            }
        }
        else if(directionDownRight(CellType.WALL) 
        		&& directionDown(CellType.EMPTY) 
        		&& directionRight(CellType.EMPTY)){
            if(vertiDir == Direction.DOWN) {
                if (currtDir == Direction.RIGHT 
                		&& directionDownRight(CellType.WALL)) {
                    currtDir = Direction.LEFT;
                    return y+=1;
                }else{
                    currtDir = Direction.LEFT;
                    return y+=1;
                }
            }else {
                if (currtDir == Direction.LEFT 
                		&& directionDownRight(CellType.WALL)) {
                    currtDir = Direction.RIGHT;
                        return x+=1;
                } else if(currtDir == Direction.RIGHT
                		&& directionDownRight(CellType.WALL)) {
                        return x+=1;
                }else{
                    vertiDir = Direction.UP;
                    currtDir = Direction.LEFT;
                    return y += 1;
                }
            }
        }
        else if(directionDownLeft(CellType.WALL)
        		&& directionDown(CellType.EMPTY) 
        		&& directionLeft(CellType.EMPTY)){
            if(vertiDir == Direction.UP) {
                if (currtDir == Direction.LEFT 
                		&& directionDownRight(CellType.WALL)) {
                    currtDir = Direction.RIGHT;
                    vertiDir = Direction.DOWN;
                    return x+=1;
                }else{
                    currtDir = Direction.LEFT;
                    return x -= 1;
                }
            }else {
                return y+=1;
            }
        }
        else if(directionUPLeft(CellType.WALL) && directionUP(CellType.EMPTY) 
        		&& directionLeft(CellType.EMPTY)){
            if(vertiDir == Direction.UP && currtDir== Direction.RIGHT) {
                return y -= 1;
            }else if(vertiDir == Direction.UP && currtDir == Direction.LEFT) {
                return x+=1;
            }else {
                if(vertiDir == Direction.DOWN){
                    if(directionUPLeft(CellType.WALL)){
                        currtDir = Direction.LEFT;
                        return x-=1;
                    }else {
                        currtDir = Direction.RIGHT;
                        vertiDir = Direction.UP;
                        return y -= 1;
                    }
                }else {
                    vertiDir = Direction.UP;
                    currtDir = Direction.LEFT;
                    return x -= 1;
                }
            }
        }
        else if(directionUPRight(CellType.WALL) 
        		&& directionUP(CellType.EMPTY) 
        		&& directionRight(CellType.EMPTY)) {
            if (vertiDir == Direction.UP && currtDir== Direction.LEFT) {
                return y -= 1;
            } else if(vertiDir == Direction.UP
            		&& currtDir == Direction.RIGHT) {
                if(directionUPRight(CellType.WALL)){
                    
                    vertiDir = Direction.DOWN;
                }else {
                    vertiDir = Direction.UP;
                }
                return x+=1;
            }else{
                currtDir = Direction.RIGHT;
                if(directionUPRight(CellType.WALL)){
                    vertiDir = Direction.DOWN;
                }else {
                    vertiDir = Direction.UP;
                }
                return x += 1;
            }
        }
        return 0;
    }
}

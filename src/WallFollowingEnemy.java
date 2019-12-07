/*
 * Author Ben Wickens
 */
public class WallFollowingEnemy extends Character {
	private Direction currtDir;
	private Direction vertiDir;
	private Cell [] [] grid;
	public WallFollowingEnemy(int x, int y, Direction startDir) {
		super(x, y, "WFE.png");
		currtDir = startDir;
	}


	private boolean directionUP(CellType cellType) {
		CellType nextType = grid[x][y - 1].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean directionUPLeft(CellType cellType) {
		CellType nextType = grid[x - 1][y - 1].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean directionUPRight(CellType cellType) {
		CellType nextType = grid[x + 1][y - 1].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean directionRight(CellType cellType) {
		CellType nextType = grid[x + 1][y].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean directionLeft(CellType cellType) {
		CellType nextType = grid[x - 1][y].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean directionDownLeft(CellType cellType) {
		CellType nextType = grid[x - 1][y + 1].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean directionDown(CellType cellType) {
		CellType nextType = grid[x][y + 1].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private boolean directionDownRight(CellType cellType) {
		CellType nextType = grid[x + 1][y + 1].getType();
		if (nextType.equals(cellType)) {
			return true;
		} else {
			return false;
		}
	}

	private int validWallOnMove() {
		if(directionUP(CellType.WALL)) {
            System.out.println("This is Up");
            if (directionLeft(CellType.WALL) && directionRight(CellType.WALL)) {
                System.out.println("This is Up W +W");
                vertiDir = Direction.DOWN;
                return y += 1;
            } else if (directionRight(CellType.EMPTY) && directionLeft(CellType.WALL) && directionDown(CellType.WALL)) {
                System.out.println("This is R:E + L:W + D:W");
                if (currtDir == Direction.LEFT) {
                    currtDir = Direction.RIGHT;
                    System.out.println("Direction Right");
                    return x += 1;
                } else {
                    System.out.println("Direction Left");
                    currtDir = Direction.RIGHT;
                    return x += 1;
                }
            } else if (directionRight(CellType.WALL) && directionLeft(CellType.EMPTY) && directionDown(CellType.WALL)) {
                System.out.println("This is R:W + L:E + D:W");
                if (currtDir == Direction.LEFT) {
                    currtDir = Direction.RIGHT;
                    return x -= 1;
                } else {
                    currtDir = Direction.LEFT;
                    return x -= 1;
                }
            } else if (directionRight(CellType.EMPTY) && directionLeft(CellType.EMPTY) && directionDown(CellType.WALL)) {
                System.out.println("This is R:E + L:E + D:W");
                if (currtDir == Direction.LEFT) {
                    System.out.println("Direction Right");
                    return x -= 1;
                } else {
                    System.out.println("Direction Left");
                    return x += 1;
                }
            } else if (directionRight(CellType.EMPTY) && directionLeft(CellType.WALL) && directionDown(CellType.EMPTY) && !directionUPRight(CellType.WALL) && vertiDir == Direction.UP) {
                System.out.println("should be it ");
                vertiDir = Direction.DOWN;
                return y += 1;
            } else if (directionRight(CellType.EMPTY) && directionLeft(CellType.WALL) && directionDown(CellType.EMPTY)) {
                System.out.println("Direction Here");

                if (currtDir == Direction.LEFT) {
                    System.out.println("Direction Here1");
                    currtDir = Direction.RIGHT;
                    vertiDir = Direction.DOWN;
                    return y += 1;
                } else {
                    System.out.println("Direction Her2e");
                    currtDir = Direction.RIGHT;
                    return x += 1;
                }
            } else if (directionRight(CellType.WALL) && directionLeft(CellType.EMPTY) && directionDown(CellType.EMPTY) && !directionUPLeft(CellType.WALL) && vertiDir == Direction.UP) {
                System.out.println("should be it ");
                vertiDir = Direction.DOWN;
                return y += 1;
            } else if (directionRight(CellType.WALL) && directionLeft(CellType.EMPTY) && directionDown(CellType.EMPTY)) {
                System.out.println("Direction Here3");
                if (currtDir == Direction.RIGHT) {
                    vertiDir = Direction.DOWN;
                    currtDir = Direction.LEFT;
                    return y += 1;
                } else {
                    return x -= 1;
                }
            }else  if (directionRight(CellType.EMPTY) && directionLeft(CellType.EMPTY) && directionDown(CellType.EMPTY) && !directionUPLeft(CellType.WALL)){
                currtDir = Direction.RIGHT;
                return x +=1;
            }else  if (directionRight(CellType.EMPTY) && directionLeft(CellType.EMPTY) && directionDown(CellType.EMPTY) && !directionUPRight(CellType.WALL)){
                currtDir = Direction.LEFT;
                return x -=1;
            }else if (directionRight(CellType.EMPTY) && directionLeft(CellType.EMPTY) && directionDown(CellType.EMPTY)){
			    if(currtDir == Direction.LEFT){
			        return x-=1;
                }else{
			        return x+=1;
                }
            }
		}else if(directionDown(CellType.WALL)) {
            System.out.println("This is Down");
            if (directionLeft(CellType.WALL) && directionRight(CellType.WALL)) {
                System.out.println("This is Down W+W");
                vertiDir = Direction.UP;
                return y -= 1;
            } else if (directionRight(CellType.EMPTY) && directionLeft(CellType.WALL) && directionUP(CellType.EMPTY) && directionDownRight(CellType.EMPTY)) {
                System.out.println("should be it ");
                vertiDir = Direction.UP;
                return y -= 1;
            } else if (directionRight(CellType.EMPTY) && directionLeft(CellType.WALL) && directionUP(CellType.EMPTY)) {
                System.out.println("This is Down E + W +W");
                if (vertiDir == Direction.DOWN && currtDir == Direction.RIGHT) {
                    currtDir = Direction.LEFT;
                    vertiDir = Direction.UP;
                    return x += 1;
                } else if (vertiDir == Direction.DOWN && currtDir == Direction.LEFT) {
                    currtDir = Direction.RIGHT;
                    vertiDir = Direction.UP;
                    return x -= 1;
                } else {
                    currtDir = Direction.RIGHT;
                    return y -= 1;
                }
            } else if (directionRight(CellType.EMPTY) && directionLeft(CellType.WALL) && directionUP(CellType.WALL)) {
                System.out.println("This is Down W + E +W last 2");
                if (vertiDir == Direction.DOWN) {
                    currtDir = Direction.RIGHT;
                    return x += 1;
                } else {
                    vertiDir = Direction.UP;
                    return y -= 1;
                }
            }else if(directionRight(CellType.WALL) && directionLeft(CellType.EMPTY) && directionUP(CellType.EMPTY) && directionDownLeft(CellType.EMPTY)){
                System.out.println("should be it ");
                vertiDir = Direction.UP;
                return y -= 1;
        }else if(directionRight(CellType.WALL) && directionLeft(CellType.EMPTY) && directionUP(CellType.EMPTY)){
                System.out.println("This is Down W + E +W last ");
			    if(vertiDir == Direction.DOWN ){
			        currtDir = Direction.RIGHT;
			        vertiDir = Direction.UP;
			        x-=1;
                }else {
                    vertiDir = Direction.UP;
                    return y -= 1;
                }
			}else if(directionRight(CellType.EMPTY) && directionLeft(CellType.EMPTY)){
			    if(currtDir == Direction.RIGHT){
                    return x-=1;
                }else{
                    return x+=1;
                }
            }

		}else if(directionRight(CellType.WALL)){
			System.out.println("This is Right");
			if(directionUP(CellType.EMPTY)&&directionDown(CellType.EMPTY) && directionDownRight(CellType.EMPTY)){
				System.out.println("This is Right Dected");
                if(vertiDir == Direction.DOWN) {
                    vertiDir = Direction.UP;
					return y -= 1;
				}else {

					return y+=1;
				}
			}else if(directionDown(CellType.WALL) && directionDownLeft(CellType.WALL) && directionUP(CellType.EMPTY)){
			    if(vertiDir == Direction.DOWN){
			        vertiDir = Direction.UP;
			        currtDir = Direction.LEFT;
			        return x-+1;
                }else{
			        vertiDir = Direction.UP;
			        return y-=1;
                }
            } else if (directionUP(CellType.EMPTY)&&directionDown(CellType.EMPTY) && directionDownRight(CellType.WALL)){
				System.out.println("This is Right Dected Wrong");
				if(vertiDir == Direction.DOWN) {
					return y += 1;
				}else{
				    vertiDir = Direction.UP;
					return y-=1;
				}
			}
		}else if(directionLeft(CellType.WALL)) {
            System.out.println("This is Left");
            if (directionDown(CellType.WALL) && directionDownLeft(CellType.WALL) && directionUP(CellType.EMPTY)) {
                if (vertiDir == Direction.DOWN) {
                    vertiDir = Direction.UP;
                    currtDir = Direction.LEFT;
                    return x - +1;
                } else {
                    vertiDir = Direction.UP;
                    return y -= 1;
                }
            } else if (directionUP(CellType.EMPTY) && directionDown(CellType.EMPTY) && directionDownLeft(CellType.WALL)) {
                System.out.println("This is Left Dected Wrong");
                if (vertiDir == Direction.DOWN) {
                    return y += 1;
                } else {
                    return y -= 1;
                }
            }else if(directionUP(CellType.EMPTY)&& directionDownLeft(CellType.EMPTY)){
                System.out.println("This is Left Dected Wrong Here");
                vertiDir = Direction.UP;
                currtDir = Direction.RIGHT;
                return y-=1;
            }
        }
		return 0;
	}




	@Override
	public void move(Cell[][] grid) {
		this.grid = grid;
		validWallOnMove();
	}
}

/*
if (directionUP(CellType.WALL) && directionDown(CellType.WALL)){
			System.out.println("Error on Some");
			if((directionDownRight(CellType.WALL) || directionUPRight(CellType.WALL)) && directionRight(CellType.EMPTY)){
				if (currtDir == Direction.RIGHT) {
					return x += 1;
				} else {
					return x -= 1;
				}
			}else{
				if (currtDir == Direction.RIGHT) {
					return x += 1;
				} else {
					return x -= 1;
				}
			}
		}else if (directionDown(CellType.WALL) && !directionRight(CellType.WALL) && !directionLeft(CellType.WALL)) {
			System.out.println("Error on D");
			if (directionDownRight(CellType.WALL)) {
				System.out.println("Error on DDD");
				if (directionRight(CellType.EMPTY)) {
					System.out.println("Error on D123");
					if (currtDir == Direction.RIGHT) {
						return x += 1;
					} else {
						return x -= 1;
					}
				}
			} else if (directionDownLeft(CellType.WALL)) {
				System.out.println("Error on D3");
				if (directionLeft(CellType.EMPTY)) {
					System.out.println("Error on D4");
					if (currtDir == Direction.LEFT) {
						return x -= 1;
					} else {
						return x += 1;
					}
				}
			}
		} else if (directionUP(CellType.WALL) && !directionLeft(CellType.WALL) && !directionRight(CellType.WALL)) {
			System.out.println("Error on U1");
			if (directionUPLeft(CellType.WALL) || directionDownLeft(CellType.WALL)) {
				System.out.println("Error on U2");
				if (directionLeft(CellType.EMPTY) && directionLeft(CellType.EMPTY)) {
					System.out.println("Error on U3");
					if (currtDir == Direction.RIGHT) {
						return x += 1;
					} else {
						return x -= 1;
					}
				}
				}else{
				System.out.println("Error on U3SSS");
					if (currtDir == Direction.RIGHT) {
						currtDir = Direction.LEFT;
						return x -= 1;
					}else{
						currtDir = Direction.RIGHT;
						return  x+=1;
					}
			}
		} else if (directionLeft(CellType.WALL)) {
			System.out.println("Error on L1");
			if (directionRight(CellType.WALL) || directionLeft(CellType.WALL) &&directionUP(CellType.EMPTY) ){
				System.out.println("Error on L2");
				return y += 1;
			}
			else if (directionRight(CellType.WALL) || directionLeft(CellType.WALL) &&directionDown(CellType.EMPTY)) {
				System.out.println("Error on L3");
				return y -= 1;
			} else{
				System.out.print('a');
				currtDir = Direction.RIGHT;
				return x += 1;
			}
		} else if (directionRight(CellType.WALL)) {
			System.out.println("Error on R1");
			if (directionUP(CellType.EMPTY) && directionLeft(CellType.WALL)) {
				System.out.println("1a");
				return y -= 1;
			} else if (directionDown(CellType.EMPTY) && directionDownLeft(CellType.WALL)) {
				System.out.println("1b");
				return y += 1;
			} else if ( directionDown(CellType.WALL)
					&& directionRight(CellType.WALL) && currtDir == Direction.RIGHT){
				return y -= 1;
			}else if(directionDown(CellType.EMPTY) && directionRight(CellType.WALL) && !directionDownRight(CellType.WALL)) {
				return y -= 1;
			}else{
				System.out.println("1e");
				currtDir = Direction.LEFT;
				return x -= 1;
			}
		}
		return 0;
 */

/*

 */
/**
 * @author Ben Wickens
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LoadMap {
    private String fileLocation;
    private File fileInput;
    private Cell[][] loadedMap = null;

    public LoadMap(String file) throws FileNotFoundException {
        this.fileLocation = file;
        this.fileInput = new File(fileLocation);
        readFile(file);

    }

    public Cell[][] getMap(){
        return loadedMap;
    }

    private void readFile(String file) {
        Scanner in = null;


        //Loads file into file reader for input.
        try {
            in = new Scanner(fileInput);
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(0);
        }

        String arrayInput = in.nextLine();
        String[] size = arrayInput.split("A", 2);

        String strX = size[0];
        String strY = size[1];
        int x = Integer.parseInt(strX);
        int y = Integer.parseInt(strY);


        Cell[][] map = new Cell[x][y]; //2D array of type cell, dimensions from file.
        //temp counts for where in array to add item
        int tempY = 0;
        //reads in each line, adds it to corresponding position in array.
        while (in.hasNext()) {
            String line = in.next();
            //code currently requires a , before each element so that it can split.
            String[] elems = line.split(",", x+1);
            for (int i = 0; i < x; i++) {
                //System.out.println("Position: " + i + " Element: " +  elems[i]);
                map[i][tempY] = getCellType(elems[i]);
            }
            tempY += 1;
        }
        for (int col = 0; col < x; col++) {
            for (int row = 0; row < x; row++) {
                System.out.println(map[row][col]);
            }
            System.out.println("##### NEW LINE #####");
        }

        this.loadedMap = map;
    }

    private Cell getCellType(String input) {
        Cell result = null;
        switch (input) {
            case "W" :
                result = new Cell(CellType.WATER, null);
                break;

            case "E" :
                result = new Cell(CellType.EMPTY,null);
                break;

            case "G" :
                result = new Cell(CellType.GOAL, null);
                break;

            case "F" :
                result = new Cell(CellType.FIRE, null);
                break;

            case "CDB" :
                result = new Cell(CellType.BLUE_DOOR, null);
                break;

            case "CDR" :
                result = new Cell(CellType.RED_DOOR, null);
                break;

            case "CDG" :
                result = new Cell(CellType.GREEN_DOOR, null);
                break;

            case "CT" :
                result = new Cell(CellType.EMPTY, Collectable.TOKEN);
                break;

            case "CF" :
                result = new Cell(CellType.EMPTY, Collectable.FLIPPERS);
                break;

            case "CB" :
                result = new Cell(CellType.EMPTY, Collectable.FIRE_BOOTS);
                break;

            case "CI" :
                result = new Cell(CellType.EMPTY, Collectable.ICE_SKATES);
                break;

            case "CD" :
                result = new Cell(CellType.EMPTY, Collectable.DAGGER);
                break;

            case "CS" :
                result = new Cell(CellType.EMPTY, Collectable.SPEAR);

            case "S" :
                result = new Cell(CellType.EMPTY, Collectable.SHIELD);
                break;

            case "GR" :
                result = new Cell(CellType.EMPTY, Collectable.GHOST_REPELLENT);
                break;
            //Enemies created as blank cells..?
            case "SLE" :
                result = new Cell(CellType.EMPTY, null);
                break;

            case "WFE" :
                result = new Cell(CellType.EMPTY, null);
                break;

            case "DTE" :
                result = new Cell(CellType.EMPTY, null);
                break;

            case "SME" :
                result = new Cell(CellType.EMPTY, null);
                break;

            case "GE" :
                result = new Cell(CellType.EMPTY,null);
                break;

            case "P" :
                result = new Cell(CellType.EMPTY,null);
                break;

            //hasn't been implemented yet (
		/*
		case "K:#" :
			result = new Cell(CellType.,null);
			break;

		case "TD" : //token door TD:#
			result = new Cell(CellType.,null);
			break;
		case "T" : // teleporter
			result = new Cell(CellType.,null);
			break;	 */
        }

        return result;
    }

}

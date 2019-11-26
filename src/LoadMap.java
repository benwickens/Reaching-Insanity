/**
 * Reads in the ASCII text file and converts it to a 2D array of type Cell.
 * @author Ben Wickens
 * @version 1.0 - Takes in the text file, creates an array using the first line to define size.
 * Creates a 2D array of type Cell. <br>
 * 1.1 - Now handles error of file not found. Also splits certain inputs at ':' for cells that
 * require a number such a token door. Not fully implemented as door isn't finished.
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
		String[] size = arrayInput.split(",", 2);

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
			String[] elems = line.split(",", x+1);
			String cellNum[] = null;
			
			for (int i = 0; i < x; i++) {
				if (elems[i].contains(":")) { //TD:5 (token door requiring 5 tokens to open)
					cellNum = elems[i].split(":", 2);
					System.out.println("SPLIT TEST: " + cellNum[0] + " + " + cellNum[1]);
				}
				map[i][tempY] = getCellType(elems[i]);
			}
			
			tempY += 1;
		}
		//For testing, prints the cell. Originally had a toString function in Cell but removed it as wasn't my class.
		for (int col = 0; col < y; col++) {
			for (int row = 0; row < x; row++) {
				System.out.println(map[row][col]);
			}
			
			System.out.println("##### NEW LINE TESTING #####"); //Indicates the next line of the text file
		}
		this.loadedMap = map;
	}

	private Cell getCellType(String input) {
		Cell result = null;
		switch (input) {
		case "WA" :
			result = new Cell(CellType.WATER, null);
			break;
		case "W" :
			result = new Cell(CellType.WALL, null);
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
		default: 
			result = new Cell(CellType.EMPTY,null);

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

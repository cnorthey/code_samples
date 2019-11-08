
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * This is the main class for the project. It simulates how populations of hares
 * and pumas change over time for a given landscape based on some provided
 * parameters. This program outputs:
 *
 * - image files of how populations are distributed across the landscape at
     regular timesteps
 * - average values of hare and puma populations at regular intervals
 * - once finished, the total execution time
 *
 * To run main, you need to include arguments for the source file of the map,
 * and then the 8 parameters used in the equations:
 *
 * - r birth rate of hares (default 0.08)
 * - a predation rate at which pumas eat hares (0.04)
 * - b birth rate of pumas per one hare eaten (0.02)
 * - m puma mortality rate (0.06)
 * - k diffusion rate for hares (0.2)
 * - l diffusion rate for pumas (0.2)
 * - deltaT size of time step (0.4)
 * - T number of timesteps between outputs (5)
 *
 * For example: ./data/sample.dat 0.08 0.04 0.02 0.06 0.2 0.2 0.4 5
 *
 * (In Eclipse, you can set these from Run > Run Configuration > Arguments tab.)
 */

public class PopulationSimulation {

	private static int NUM_PARAMS = 9;
	private static double MAX_TIME = 500;
	private static String OUTPUT_PATH = "./outputPPM/";

	public static void main(String[] args) throws FileNotFoundException, IOException {

		long startTime = System.currentTimeMillis();

		//verify input map file and args
		if (checkPath(args[0]) == false) {
			System.out.println("Path to map file does not exist! Terminating.");
			long stopTime = System.currentTimeMillis();
			System.out.print("Total runtime: " + (stopTime - startTime)/1000.0 + " s");
			return;
		}

		if (checkArgs(args) == false) {
			long stopTime = System.currentTimeMillis();
			System.out.print("Total runtime: " + (stopTime - startTime)/1000.0 + " s");
			return;
		}

		//verify output path exists
		if (checkPath(OUTPUT_PATH) == false) {
			System.out.println("Your output path is " + OUTPUT_PATH + ", which does not exist! Terminating.");
			long stopTime = System.currentTimeMillis();
			System.out.print("Total runtime: " + (stopTime - startTime)/1000.0 + " s");
			return;
		}

		//parse arguments as doubles for use in equations
		double[] equationParams = new double[NUM_PARAMS - 1];
		String mapSourcePath = args[0];
		for (int i = 0; i < args.length - 1; i++) {
			equationParams[i] = Double.parseDouble(args[i + 1]);
		}
		double T = equationParams[7];

		//try to initialize map from file
		GridSquare[][] map = initMap(mapSourcePath);

		if(map == null){
			System.out.println("Error reading map file. Verify it is correctly formatted. Terminating.");
			long stopTime = System.currentTimeMillis();
			System.out.print("Total runtime: " + (stopTime - startTime)/1000.0 + " s");
			return;
		}

		UpdateHandler updateHandler = new UpdateHandler(equationParams);
		OutputHandler outputHandler = new OutputHandler();

		// create array containing the colours for each pixel in the PPM output files
		String[] ppmStringsHares = initialisePPMstring(map);
		String[] ppmStringsPumas = initialisePPMstring(map);

		// output initial averages and heatmaps
		updatePPMstringsForInitialPopulations(map, updateHandler, ppmStringsHares, ppmStringsPumas);
		outputHandler.outputAverageHPvalues(map);
		outputHandler.outputImage(map, ppmStringsHares, ppmStringsPumas, 0, OUTPUT_PATH);

		//MAIN LOOP: update populations MAX_TIME times, printing outputs
		//every T steps
		for (int t = 1; t <= MAX_TIME; t++) {

			//these happen every time
			updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
			updateHandler.updateNewPop(map);

			//only generate output every T timesteps
			if (t % T == 0) {
				outputHandler.outputAverageHPvalues(map);
				outputHandler.outputImage(map, ppmStringsHares, ppmStringsPumas, t, OUTPUT_PATH);
			}
		}

		//print total run time
		long stopTime = System.currentTimeMillis();
		System.out.print("Total runtime: " + (stopTime - startTime)/1000.0 + " s");

	}

	/**
	 * Reads in the file that contains the specifications for the map used for this
	 * population simulation, and creates a 2D array of GridSquare objects to
	 * represent that map (surrounded by a "halo" of water cells).
	 *
	 * @param mapSourcePath - path to the file containing the map specification
	 * @return GridSquare[][] map - 2D array of GridSqaure objects
	 */
	public static GridSquare[][] initMap(String mapSourcePath) {

		int numCols = 0;
		int numRows = 0;
		GridSquare[][] map = new GridSquare[0][0];

		try {
			// open given data file
			File file = new File(mapSourcePath);
			if (!file.exists() || file.isDirectory()) {
				throw new FileNotFoundException();
			}

			BufferedReader buffer = new BufferedReader(new FileReader(file));

			// read in first line of file to find dimensions of map (N_col, N_row)
			String currentLine = buffer.readLine();

			// add extra rows/cols for padding of water
			numCols = Integer.parseInt(currentLine.split(" ")[0]) + 2;
			numRows = Integer.parseInt(currentLine.split(" ")[1]) + 2;

			if(checkDimensions(numCols-2, numRows-2) == false) {
				buffer.close();
				return null;
			}

			// initialize map of GridSquares (all equal to null)
			map = new GridSquare[numRows][numCols];
			GridSquare gridSquare;

			// add top row of padding with water
			for (int i = 0; i < numCols; i++) {
				gridSquare = new GridSquare(0);
				map[0][i] = gridSquare;
			}

			// read in rest of file and finish initializing GridSquares
			// according to file (1 = land, 0 = water)
			currentLine = buffer.readLine();
			String[] lineData;
			int row = 1;

			while (currentLine != null) {

				lineData = currentLine.split(" ");
				;

				// pad first col in row w water
				gridSquare = new GridSquare(0);
				map[row][0] = gridSquare;

				// fill in rest of row
				for (int i = 0; i < lineData.length; i++) {
					gridSquare = new GridSquare(Integer.parseInt(lineData[i]));
					map[row][i + 1] = gridSquare;
				}

				// pad last col in row w water
				gridSquare = new GridSquare(0);
				map[row][lineData.length + 1] = gridSquare;

				row++;
				currentLine = buffer.readLine(); // read the next line

			}

			// add bottom row of padding with water
			for (int i = 0; i < numCols; i++) {
				gridSquare = new GridSquare(0);
				map[numRows - 1][i] = gridSquare;
			}

			buffer.close();

		} catch (IOException e) {
			System.out.print("ERROR: " + e.toString()+"\n");
			return null;
		}

		System.out.print("Initialized map w dimensions " + (numCols - 2) + "x" + (numRows - 2) + ":\n");
		return map;

	}

	/**
	 * verifies that there are the correct number of arguments and they are
	 * of the correct type.
	 *
	 * @param String[] args
	 * @return boolean
	 */
	public static boolean checkArgs(String[] args) {
		if (args.length != NUM_PARAMS) {
			System.out.print("IMPROPER USAGE - incorrect argument number.\n"
					+ "INPUTS EXAMPLE: ./data/sample.dat 0.08 0.04 0.02 0.06 0.2 0.2 0.4 50\n"
					+ "WHERE THESE ARE: inputMapFilePath, r, a, b, m, k, l, deltaT, T");
			return false;
		}

		try {

			for (int i = 1; i < NUM_PARAMS; i++) {
				Double.parseDouble(args[i]);
			}

		} catch (NumberFormatException e) {
			System.out.print("IMPROPER USAGE - incorrect argument type(s).\n"
					+ "INPUTS EXAMPLE: ./data/sample.dat 0.08 0.04 0.02 0.06 0.2 0.2 0.4 50\n"
					+ "WHERE THESE ARE: inputMapFilePath, r, a, b, m, k, l, deltaT, T");
			return false;
		}

		return true;
	}

	/**
	 * verifies that the given file exists.
	 *
	 * @param String path
	 * @return boolean
	 */
	public static boolean checkPath(String path) {
		File file = new File(path);
		return file.exists();
	}

	/**
	 * verifies that given integers are acceptable map dimensions (greater
	 * than zero and less than 2000).
	 *
	 * @param int numCols, int numRows
	 * @return boolean
	 */
	private static boolean checkDimensions(int numCols, int numRows) {
		return (numCols > 0 && numCols <= 2000) && (numRows > 0 && numRows <= 2000);
	}


	/**
	 * creates an array of strings corresponding to each pixel in the output PPM files
	 * @param map
	 * @return String[]
	 */
	private static String[] initialisePPMstring(GridSquare[][] map) {
		int width = map.length;
		int height = map[1].length;

		String[] ppmStrings = new String[width * height];
		String blue = "0 191 255 ";
		Arrays.fill(ppmStrings, blue);
		return ppmStrings;
	}

	/**
	 * creates an array of strings corresponding to each pixel in the output PPM files
	 * @param map
	 * @param ppmStringsHares
	 * @param ppmStringsPumas
	 * @return String[]
	 */
	private static void updatePPMstringsForInitialPopulations(GridSquare[][] map, UpdateHandler updateHandler, String[] ppmStringsHares, String[] ppmStringsPumas) {
		for(int row = 1; row < map.length-1; row++) {
    		for(int col = 1; col < map[row].length-1; col++) {
    			if (map[row][col].getIsLand()) {
    				int block = (row*map[row].length) + col;
        			double harePop = map[row][col].getCurPopHares();
        			double pumaPop = map[row][col].getCurPopPumas();
        			updateHandler.updateSquareColour(block, harePop, pumaPop, ppmStringsHares, ppmStringsPumas);
    			}
    		}
		}

	}

}

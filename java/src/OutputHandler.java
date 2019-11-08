import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class can handle all outputs neatly away form the main function
 */
public class OutputHandler {
	private static String ppmStringHares="";
    private static String ppmStringPumas="";

	/**
	 * This methods will output the average values for
	 * Hares and Pumas at regular intervals chosen by us
	 *
	 */
	public double[] outputAverageHPvalues(GridSquare[][] map) {
		// Calculate the total number of hares, pumas and land squares in the map
		double hares = 0.0;
		double pumas = 0.0;
		int landSquares = 0;
		for(int row = 0; row < map.length; row++) {
    		for(int col = 0; col < map[row].length; col++) {
    			if (map[row][col].getIsLand()) {
    				landSquares++;
    			}
    	    	hares += map[row][col].getCurPopHares();
    	    	pumas += map[row][col].getCurPopPumas();
    		}
    	}

		// Calculate the averages
		double avgHares = hares/landSquares;
		double avgPumas = pumas/landSquares;

		double[] averages = {avgHares, avgPumas};

		// Print out averages
        System.out.print("Average number of Hares per land square: " + Double.toString(avgHares) + "\n");
        System.out.print("Average number of Pumas per land square: " + Double.toString(avgPumas)+ "\n");

		return averages;
	}


	/**
	 * This methods will create the PPM file string
	 * @throws IOException
	 * @throws FileNotFoundException
	 *
	 */
	public void outputImage(GridSquare[][] map, String[] ppmStringsHares, String[] ppmStringsPumas, int t, String outputPath) throws FileNotFoundException, IOException {

		// Pad the t integer with 0s to keep files alphabetically ordered
		String stringT = "";
		if (t<10) {
			stringT = "00" + t;
		} else if (t<100) {
			stringT = "0" + t;
		} else {
			stringT += t;
		}

		// Make the names for the PPM files, including the timestep T
		String name1 = outputPath + "/haresPPM/harePopulationTime" + stringT + ".ppm";
		String name2 = outputPath + "/pumasPPM/pumaPopulationTime" + stringT + ".ppm";

		// Create start of PPM file containing the image formatting information
		int height = map.length;
        int width = map[1].length;
		String head = "P3\n" + width + "\n" + height + "\n255\n";

		// Create entire PPM image string
		ppmStringHares = head;
		ppmStringHares += String.join("\n", ppmStringsHares);

	    ppmStringPumas = head;
	    ppmStringPumas += String.join("\n", ppmStringsPumas);

	    // Save strings as files
        saveImage(name1, ppmStringHares);
        saveImage(name2, ppmStringPumas);

        System.out.println("Heatmaps of hare and puma populations at time " + t + " created in outputs folder as PPM images\n");

	}

	/**
	 * This methods will create the PPM file in the outputPPM folder
	 */
	public static void saveImage(String filename, String ppmString) throws FileNotFoundException, IOException {
    	FileOutputStream fos = new FileOutputStream(filename);
    	fos.write(new String(ppmString).getBytes());
    	fos.close();
    }
}

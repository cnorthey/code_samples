/**
 * This class computes the new population of both hares and pumas
 * for each grid square at time each timestep T
 */

public class UpdateHandler {

	private double r,a,b,m,k,l,deltaT;

	/**
	 * This constructor sets the private variables of this class
	 * which are the parameters chosen by the user
	 * and the processed map
	 *
	 */
	public UpdateHandler(double [] vars) {

		this.r = vars[0];
		this.a = vars[1];
		this.b = vars[2];
		this.m = vars[3];
		this.k = vars[4];
		this.l = vars[5];
		this.deltaT = vars[6];

	}

	/**
	 * This methods recurses through the GridSquare map
	 * and calculates the new populations for each square
	 * which are saved in the respective GridSquare objects
	 *
	 */
	public void calculateNewPop(GridSquare[][] map, String[] ppmStringsHares, String[] ppmStringsPumas) {
		for(int row = 1; row < map.length-1; row++) {
    		for(int col = 1; col < map[row].length-1; col++) {
                if (map[row][col].getIsLand()) {
                	// get all the required variables for the updates
                	GridSquare top = map[row-1][col];
        			GridSquare bottom = map[row+1][col];
        			GridSquare left = map[row][col-1];
        			GridSquare right = map[row][col+1];

        			int count = countSurroundingLand(top, bottom, left, right);
                    double currHares = map[row][col].getCurPopHares();
                    double currPumas = map[row][col].getCurPopPumas();

                    //calculate new population of hares
                    double newHarePop = calculateHarePopulation(count, currHares, currPumas, top, bottom, left, right);
                    map[row][col].setNewPopHares(newHarePop);

                    //calculate new population of pumas
                    double newPumaPop = calculatePumaPopulation(count, currHares, currPumas, top, bottom, left, right);
                    map[row][col].setNewPopPumas(newPumaPop);

                    // update colour for corresponding pixel
                	int block = (row*map[row].length) + col;
                	updateSquareColour(block, newHarePop, newPumaPop, ppmStringsHares, ppmStringsPumas);
            	}
    		}
    	}
	}

	/**
	 * This methods recurses through the GridSquare map
	 * and invokes the update method of each square
	 * which saves the "new" populations as "current" ones
	 * after having finished calculating all of them
	 *
	 */
	public void updateNewPop(GridSquare[][] map) {

		for(int row = 1; row < map.length -1; row++) {
    		for(int col = 1; col < map[row].length -1; col++) {
    			map[row][col].updatePopulations();

    		}
    	}

	}

	/**
	 * Helper method which counts the number of land squares
	 * adjacent to the current one
	 */
	public int countSurroundingLand(GridSquare top, GridSquare bottom, GridSquare left, GridSquare right) {
		int count = 0;
		count =  top.getIsLand() ? count+1 : count;
        count =  bottom.getIsLand() ? count+1 : count;
        count =  left.getIsLand() ? count+1 : count;
        count =  right.getIsLand() ? count+1 : count;

		return count;
	}

	/**
	 * Helper method which implements the equation needed for the Hare population
	 */
	public double calculateHarePopulation(int count, double currHares, double currPumas, GridSquare top, GridSquare bottom, GridSquare left, GridSquare right) {
		double term1 = this.r * currHares;
        double term2 = this.a * currHares * currPumas;
        double term3 = this.k * (top.getCurPopHares() + bottom.getCurPopHares() + left.getCurPopHares() + right.getCurPopHares());
        double term4 = this.k * count * currHares;

        double newHarePop = currHares + (this.deltaT * (term1 - term2 + term3 - term4));
        return newHarePop;

	}

	/**
	 * Helper method which implements the equation needed for the Puma population
	 */
	public double calculatePumaPopulation(int count, double currHares, double currPumas, GridSquare top, GridSquare bottom, GridSquare left, GridSquare right){
		double term1 = this.b * currHares * currPumas;
		double term2 = this.m * currPumas;
		double term3 = this.l * (top.getCurPopPumas() + bottom.getCurPopPumas() + left.getCurPopPumas() + right.getCurPopPumas());
		double term4 = this.l * count * currPumas;

        double newPumaPop = currPumas + (this.deltaT * (term1 - term2 + term3 - term4));
        return newPumaPop;

	}

	/**
	 * Helper method which updates the colour for a pixel in the outputted heatmaps
	 * @param newHarePop
	 * @param ppmStringsHares
	 * @param block
	 * @param newPumaPop
	 * @param ppmStringsPumas
	 */
	public void updateSquareColour(int block, double newHarePop, double newPumaPop, String[] ppmStringsHares, String[] ppmStringsPumas){
		String blue = "0 191 255 ";
        String green = "50 205 50 ";
        String greenDark = "0 100 0 ";
        String greenLight = "152 251 152 ";
        String orange = "255 215 0 ";
        String orangeDark = "184 134 11 ";
        String orangeLight = "238 232 170 ";

		if(newHarePop >= 3.5)
			ppmStringsHares[block] = greenDark;
		if(newHarePop > 2.5 && newHarePop<3.5)
			ppmStringsHares[block] = green;
        if(newHarePop <= 2.5)
        	ppmStringsHares[block] = greenLight;

        if(newPumaPop >=3.5)
        	ppmStringsPumas[block] = orangeDark;
		if(newPumaPop > 2.5 && newPumaPop<3.5)
			ppmStringsPumas[block] = orange;
        if(newPumaPop <= 2.5)
        	ppmStringsPumas[block] = orangeLight;

	}
}

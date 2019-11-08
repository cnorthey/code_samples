import java.util.Random;
import java.text.DecimalFormat;

/**
 * This class stores information about the individual squares of the map grid,
 * including the type of terrain (land or water) and populations of hares and
 * pumas. Note that water squares always have populations of zero.
 * 
 * On land squares, two populations are stored per animal: the current timestep's
 * pop and the next timestep's  pop. This is to prevent inaccuracies during 
 * the calculations for new populations since the equations require information
 * about surrounding GridSquare populations.
 *
 */

public class GridSquare {

	private boolean isLand;
	private double newPopHares;
	private double newPopPumas;
	private double curPopHares;
	private double curPopPumas;

	/**
	 * Initializes the private data members of this class.
	 * 
	 * @param isLand - 1 means land, 0 means water
	 */
	public GridSquare(int isLand) {
		this.isLand = (isLand == 1) ? true : false;

		// populations are densities per area of square
		// if this is a land square, initialize current populations
		// to a random density between 0.0 and 5.0.
		Random rand = new Random();
		this.curPopHares = this.isLand ? rand.nextDouble() * 10 / 2 : 0.0;
		this.curPopPumas = this.isLand ? rand.nextDouble() * 10 / 2 : 0.0;
		this.newPopHares = 0.0;
		this.newPopPumas = 0.0;

	}

	/**
	 * This method updates the current population of the square to the population
	 * calculated for the next timestep. It is called at the end of each timestep
	 * after all new GridSquare populations have been calculated.
	 * 
	 * @param none
	 * @return null
	 */
	public void updatePopulations() {
		if (this.isLand) {
			this.curPopHares = this.newPopHares;
			this.curPopPumas = this.newPopPumas;
		}
	}

	// GETTERS

	public boolean getIsLand() {
		return this.isLand;
	}

	public double getNewPopHares() {
		return this.newPopHares;
	}

	public double getNewPopPumas() {
		return this.newPopPumas;
	}

	public double getCurPopHares() {
		return this.curPopHares;
	}

	public double getCurPopPumas() {
		return this.curPopPumas;
	}

	// SETTERS

	public void setIsLand(boolean isLand) {
		this.isLand = isLand;
	}

	public void setNewPopHares(double pop) {
		//populations cannot be negative
		this.newPopHares = (pop > 0.0) ? pop : 0.0;
	}

	public void setNewPopPumas(double pop) {
		//populations cannot be negative
		this.newPopPumas = (pop > 0.0) ? pop : 0.0;
	}

	public void setCurPopHares(double pop) {
		this.curPopHares = pop;
	}

	public void setCurPopPumas(double pop) {
		this.curPopPumas = pop;
	}

}

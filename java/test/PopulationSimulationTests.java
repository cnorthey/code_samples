import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class PopulationSimulationTests {
	private PopulationSimulation simulation;

	@Test
	public void GridSquareInitiateWater() {
		// This tests the GridSquares initiate correctly when presented with water (0)
		// in the input
		GridSquare water = new GridSquare(0);

		boolean isLand = water.getIsLand();
		double pumas = water.getCurPopPumas();
		double hares = water.getCurPopHares();

		double resultPumas = 0.0;
		double resultHares = 0.0;

		Assert.assertFalse(isLand);
		Assert.assertEquals(resultPumas, pumas, 0.00001);
		Assert.assertEquals(resultHares, hares, 0.00001);
	}

	@Test
	public void GridSquareInitiateLand() {
		// This tests the GridSquares initiate correctly when presented with land (1) in
		// the input
		// setting the population between 0 and 5
		GridSquare land = new GridSquare(1);

		boolean isLand = land.getIsLand();
		double pumas = land.getCurPopPumas();
		double hares = land.getCurPopHares();

		Assert.assertTrue(isLand);
		Assert.assertTrue(pumas >= 0.0);
		Assert.assertTrue(pumas <= 5.0);
		Assert.assertTrue(hares >= 0.0);
		Assert.assertTrue(hares <= 5.0);
	}

	@Test
	public void GridSquareSetGet() {
		// This tests the GridSquares initiate correctly when presented with water (0)
		// in the input
		GridSquare land = new GridSquare(1);

		land.setCurPopHares(4.5);
		land.setCurPopPumas(2.5);
		land.setNewPopHares(3.8);
		land.setNewPopPumas(2.8);

		double haresCur = land.getCurPopHares();
		double pumasCur = land.getCurPopPumas();
		double haresNew = land.getNewPopHares();
		double pumasNew = land.getNewPopPumas();

		double resultHaresCur = 4.5;
		double resultPumasCur = 2.5;
		double resultHaresNew = 3.8;
		double resultPumasNew = 2.8;

		Assert.assertEquals(resultHaresCur, haresCur, 0.00001);
		Assert.assertEquals(resultPumasCur, pumasCur, 0.00001);
		Assert.assertEquals(resultHaresNew, haresNew, 0.00001);
		Assert.assertEquals(resultPumasNew, pumasNew, 0.00001);
	}

	@Test
	public void GridSquareUpdateLand() {
		// This tests the GridSquares updates land populations correctly
		GridSquare land = new GridSquare(1);

		land.setCurPopHares(4.5);
		land.setCurPopPumas(2.5);
		land.setNewPopHares(3.8);
		land.setNewPopPumas(2.8);

		land.updatePopulations();

		double haresCur = land.getCurPopHares();
		double pumasCur = land.getCurPopPumas();
		double haresNew = land.getNewPopHares();
		double pumasNew = land.getNewPopPumas();

		double resultHaresNew = 3.8;
		double resultPumasNew = 2.8;

		Assert.assertEquals(resultHaresNew, haresCur, 0.00001);
		Assert.assertEquals(resultPumasNew, pumasCur, 0.00001);
		Assert.assertEquals(resultHaresNew, haresNew, 0.00001);
		Assert.assertEquals(resultPumasNew, pumasNew, 0.00001);
	}

	public void GridSquareUpdateWater() {
		// This tests the GridSquares updates water populations correctly (always sets
		// them to 0)
		GridSquare water = new GridSquare(0);

		water.setCurPopHares(4.5);
		water.setCurPopPumas(2.5);
		water.setNewPopHares(3.8);
		water.setNewPopPumas(2.8);

		water.updatePopulations();

		double haresCur = water.getCurPopHares();
		double pumasCur = water.getCurPopPumas();
		double haresNew = water.getNewPopHares();
		double pumasNew = water.getNewPopPumas();

		double resultHaresCur = 0.0;
		double resultPumasCur = 0.0;
		double resultHaresNew = 3.8;
		double resultPumasNew = 2.8;

		Assert.assertEquals(resultHaresCur, haresCur, 0.00001);
		Assert.assertEquals(resultPumasCur, pumasCur, 0.00001);
		Assert.assertEquals(resultHaresNew, haresNew, 0.00001);
		Assert.assertEquals(resultPumasNew, pumasNew, 0.00001);
	}

	@Test
	public void MapDimentionsSetup() {
		// This tests the map is initiated with correct padding
		String miniature = "./data/sample.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);
		int rows = map.length;
		int columns = map[0].length;

		int resultRows = 12;
		int resultColumns = 22;

		Assert.assertEquals(resultRows, rows);
		Assert.assertEquals(resultColumns, columns);

	}

	@Test
	public void MapLandscapeSetup() {
		// Tests the land and water are initiated correctly
		String miniature = "./data/miniature.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);

		boolean[][] mapLandscape = new boolean[map.length][map[0].length];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				mapLandscape[i][j] = map[i][j].getIsLand();
			}
		}

		boolean[][] result = new boolean[][] { { false, false, false, false }, { false, true, true, false },
				{ false, true, true, false }, { false, false, false, false } };

		Assert.assertArrayEquals(result, mapLandscape);

	}

	@Test
	public void MapSetHarePopulation() {
		// This tests the getters and setters of the map
		String miniature = "./data/miniature.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);
		map[1][1].setCurPopHares(1.0);
		map[1][2].setCurPopHares(4.0);
		map[2][1].setCurPopHares(2.0);
		map[2][2].setCurPopHares(5.0);
		double[][] mapPopulations = new double[map.length][map[0].length];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				mapPopulations[i][j] = map[i][j].getCurPopHares();
			}
		}

		double[][] result = new double[][] { { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 4.0, 0.0 }, { 0.0, 2.0, 5.0, 0.0 },
				{ 0.0, 0.0, 0.0, 0.0 } };
//		System.out.println(result[2][2]);
		Assert.assertArrayEquals(mapPopulations, result);
	}

	@Test
	public void MapSetPumaPopulation() {
		// This tests the getters and setters of the map
		String miniature = "./data/miniature.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);
		map[1][1].setCurPopPumas(1.0);
		map[1][2].setCurPopPumas(4.0);
		map[2][1].setCurPopPumas(2.0);
		map[2][2].setCurPopPumas(5.0);
		double[][] mapPopulations = new double[map.length][map[0].length];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				mapPopulations[i][j] = map[i][j].getCurPopPumas();
			}
		}

		double[][] result = new double[][] { { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 1.0, 4.0, 0.0 }, { 0.0, 2.0, 5.0, 0.0 },
				{ 0.0, 0.0, 0.0, 0.0 } };

		Assert.assertArrayEquals(result, mapPopulations);

	}

	@Test
	public void UpdatePopulationsMapStep1() {
		// Test one set of calculations
		String miniature = "./data/miniature.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);
		map[1][1].setCurPopHares(1.0);
		map[1][2].setCurPopHares(4.0);
		map[2][1].setCurPopHares(2.0);
		map[2][2].setCurPopHares(5.0);
		map[1][1].setCurPopPumas(5.0);
		map[1][2].setCurPopPumas(3.0);
		map[2][1].setCurPopPumas(4.0);
		map[2][2].setCurPopPumas(1.0);

		// Need this to run the UpdateHandler but will not be tested here
		int width = map.length;
		int height = map[1].length;
		String[] ppmStringsHares = new String[width * height];
		String[] ppmStringsPumas = new String[width * height];
		String blue = "0 191 255 ";
		Arrays.fill(ppmStringsHares, blue);
		Arrays.fill(ppmStringsPumas, blue);
		// end of not-tested segment

		double[] equationParams = new double[] { 0.08, 0.04, 0.02, 0.06, 0.2, 0.2, 0.4 };
		UpdateHandler updateHandler = new UpdateHandler(equationParams);
		updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
		updateHandler.updateNewPop(map);

		double[][] mapPopulationPumas = new double[map.length][map[0].length];
		double[][] mapPopulationHares = new double[map.length][map[0].length];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				mapPopulationPumas[i][j] = map[i][j].getCurPopPumas();
				mapPopulationHares[i][j] = map[i][j].getCurPopHares();
			}
		}

		double[][] resultHares = new double[][] { { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 1.272, 3.776, 0.0 },
				{ 0.0, 2.096, 4.76, 0.0 }, { 0.0, 0.0, 0.0, 0.0 } };
		double[][] resultPumas = new double[][] { { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 4.68, 3.024, 0.0 },
				{ 0.0, 3.808, 1.416, 0.0 }, { 0.0, 0.0, 0.0, 0.0 } };

		double delta = 0.00001;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Assert.assertEquals(resultPumas[i][j], mapPopulationPumas[i][j], delta);
				Assert.assertEquals(resultHares[i][j], mapPopulationHares[i][j], delta);

			}
		}
//		Assert.assertArrayEquals(resultHares, mapPopulationHares, delta); refuses to compile
	}

	@Test
	public void UpdatePopulationsMapStep2() {
		// Test two sets of calculations
		String miniature = "./data/miniature.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);
		map[1][1].setCurPopHares(1.0);
		map[1][2].setCurPopHares(4.0);
		map[2][1].setCurPopHares(2.0);
		map[2][2].setCurPopHares(5.0);
		map[1][1].setCurPopPumas(5.0);
		map[1][2].setCurPopPumas(3.0);
		map[2][1].setCurPopPumas(4.0);
		map[2][2].setCurPopPumas(1.0);

		// Need this to run the UpdateHandler but will not be tested here
		int width = map.length;
		int height = map[1].length;
		String[] ppmStringsHares = new String[width * height];
		String[] ppmStringsPumas = new String[width * height];
		String blue = "0 191 255 ";
		Arrays.fill(ppmStringsHares, blue);
		Arrays.fill(ppmStringsPumas, blue);
		// end of not-tested segment

		double[] equationParams = new double[] { 0.08, 0.04, 0.02, 0.06, 0.2, 0.2, 0.4 };
		UpdateHandler updateHandler = new UpdateHandler(equationParams);
		// Step 1
		updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
		updateHandler.updateNewPop(map);

		// Step 2
		updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
		updateHandler.updateNewPop(map);

		double[][] mapPopulationPumas = new double[map.length][map[0].length];
		double[][] mapPopulationHares = new double[map.length][map[0].length];

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				mapPopulationPumas[i][j] = map[i][j].getCurPopPumas();
				mapPopulationHares[i][j] = map[i][j].getCurPopHares();
			}
		}

		double[][] resultHares = new double[][] { { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 1.48369664, 3.592534016, 0.0 },
				{ 0.0, 2.182566912, 4.51263744, 0.0 }, { 0.0, 0.0, 0.0, 0.0 } };
		double[][] resultPumas = new double[][] { { 0.0, 0.0, 0.0, 0.0 }, { 0.0, 4.41306368, 3.046612992, 0.0 },
				{ 0.0, 3.658860544, 1.75593728, 0.0 }, { 0.0, 0.0, 0.0, 0.0 } };

		double delta = 0.00001;

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				Assert.assertEquals(resultPumas[i][j], mapPopulationPumas[i][j], delta);
				Assert.assertEquals(resultHares[i][j], mapPopulationHares[i][j], delta);

			}
		}
	}

	@Test
	public void UpdateColoursMapStep1() {
		// Test two sets of calculations
		String miniature = "./data/miniature.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);
		map[1][1].setCurPopHares(1.0);
		map[1][2].setCurPopHares(4.0);
		map[2][1].setCurPopHares(2.0);
		map[2][2].setCurPopHares(5.0);
		map[1][1].setCurPopPumas(5.0);
		map[1][2].setCurPopPumas(3.0);
		map[2][1].setCurPopPumas(4.0);
		map[2][2].setCurPopPumas(1.0);

		int width = map.length;
		int height = map[1].length;
		String[] ppmStringsHares = new String[width * height];
		String[] ppmStringsPumas = new String[width * height];
		String blue = "0 191 255 ";
		Arrays.fill(ppmStringsHares, blue);
		Arrays.fill(ppmStringsPumas, blue);

		double[] equationParams = new double[] { 0.08, 0.04, 0.02, 0.06, 0.2, 0.2, 0.4 };
		UpdateHandler updateHandler = new UpdateHandler(equationParams);
		// Step 1
		updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
		updateHandler.updateNewPop(map);

		// Step 2
//		updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
//		updateHandler.updateNewPop(map);

		String green = "50 205 50 ";
		String greenDark = "0 100 0 ";
		String greenLight = "152 251 152 ";
		String orange = "255 215 0 ";
		String orangeDark = "184 134 11 ";
		String orangeLight = "238 232 170 ";

		String[] coloursHares = new String[] { blue, blue, blue, blue, blue, greenLight, greenDark, blue, blue,
				greenLight, greenDark, blue, blue, blue, blue, blue };
		String[] coloursPumas = new String[] { blue, blue, blue, blue, blue, orangeDark, orange, blue, blue, orangeDark,
				orangeLight, blue, blue, blue, blue, blue };

		Assert.assertArrayEquals(coloursHares, ppmStringsHares);
		Assert.assertArrayEquals(coloursPumas, ppmStringsPumas);
	}

	@Test
	public void UpdateColoursMapStep2() {
		// Test two sets of calculations
		String miniature = "./data/miniature.dat";
		GridSquare[][] map = PopulationSimulation.initMap(miniature);
		map[1][1].setCurPopHares(1.0);
		map[1][2].setCurPopHares(4.0);
		map[2][1].setCurPopHares(2.0);
		map[2][2].setCurPopHares(5.0);
		map[1][1].setCurPopPumas(5.0);
		map[1][2].setCurPopPumas(3.0);
		map[2][1].setCurPopPumas(4.0);
		map[2][2].setCurPopPumas(1.0);

		int width = map.length;
		int height = map[1].length;
		String[] ppmStringsHares = new String[width * height];
		String[] ppmStringsPumas = new String[width * height];
		String blue = "0 191 255 ";
		Arrays.fill(ppmStringsHares, blue);
		Arrays.fill(ppmStringsPumas, blue);

		double[] equationParams = new double[] { 0.08, 0.04, 0.02, 0.06, 0.2, 0.2, 0.4 };
		UpdateHandler updateHandler = new UpdateHandler(equationParams);
		// Step 1
		updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
		updateHandler.updateNewPop(map);

		// Step 2
		updateHandler.calculateNewPop(map, ppmStringsHares, ppmStringsPumas);
		updateHandler.updateNewPop(map);

		String green = "50 205 50 ";
		String greenDark = "0 100 0 ";
		String greenLight = "152 251 152 ";
		String orange = "255 215 0 ";
		String orangeDark = "184 134 11 ";
		String orangeLight = "238 232 170 ";

		String[] coloursHares = new String[] { blue, blue, blue, blue, blue, greenLight, greenDark, blue, blue,
				greenLight, greenDark, blue, blue, blue, blue, blue };
		String[] coloursPumas = new String[] { blue, blue, blue, blue, blue, orangeDark, orange, blue, blue, orangeDark,
				orangeLight, blue, blue, blue, blue, blue };

		Assert.assertArrayEquals(coloursHares, ppmStringsHares);
		Assert.assertArrayEquals(coloursPumas, ppmStringsPumas);
	}

	@Test
	public void InputDataPathTest() {
		Assert.assertTrue("Input path error", PopulationSimulation.checkPath("./data/islands.dat"));
		Assert.assertFalse("Input path error", PopulationSimulation.checkPath("./error.dat"));
	}

	@Test
	public void OutputDataPathTest() {
		Assert.assertTrue("Output path error", PopulationSimulation.checkPath("./outputPPM"));
		Assert.assertFalse("Output path error", PopulationSimulation.checkPath("./error"));
	}

	@Test
	public void ArgsFormTest() {
		String correctForm = "./data/sample.dat 0.08 0.04 0.02 0.06 0.2 0.2 0.4 50";
		String errorForm = "./data/sample.dat 0.08 0.04 0.02";
		Assert.assertTrue("CheckArgs function is error", PopulationSimulation.checkArgs(correctForm.split(" ")));
		Assert.assertFalse("CheckArgs function is error", PopulationSimulation.checkArgs(errorForm.split(" ")));
	}

	@Test
	public void AveragePopsTest() {

		String miniature = "./data/teeny_tiny_sample.dat";
		GridSquare [][] map = PopulationSimulation.initMap(miniature);

		for(int i = 1; i < map.length - 1; i++) {
			for(int j = 1; j < map[i].length - 1; j++) {
				map[i][j].setCurPopHares(1.0);
				map[i][j].setCurPopPumas(1.0);
			}

		}

		OutputHandler outputHandler = new OutputHandler();

		double[] averages = outputHandler.outputAverageHPvalues(map);
		Assert.assertEquals(averages[0],1.0,0.000001);
		Assert.assertEquals(averages[1],1.0,0.000001);

	}
}

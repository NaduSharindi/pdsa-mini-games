package services;

import java.util.List;

import models.entities.KnightTourResult;
import models.entities.Position;
import models.exceptions.DatabaseException;
import utils.DatabaseConnection;
import utils.constants.KnightTourConstant;
import utils.dsa.bruteforce2.BruteForce2;
import utils.dsa.warnsdorff.Warnsdorff;

public class KnightTourService {
	// data structures to store game data
	private boolean[][] visited;
	private int visitedCount;
	private int[][] calculatedMovements;
	private int[][] userManualMovement;

	/**
	 * Initialize initial values
	 */
	public KnightTourService() {
		visited = new boolean[KnightTourConstant.BOARD_SIZE][KnightTourConstant.BOARD_SIZE];
		visitedCount = 0;
		calculatedMovements = new int[KnightTourConstant.BOARD_SIZE][KnightTourConstant.BOARD_SIZE];
		userManualMovement = new int[KnightTourConstant.BOARD_SIZE][KnightTourConstant.BOARD_SIZE];
	}

	/**
	 * save data into the database
	 * 
	 * @throws DatabaseException
	 */
	public void saveResult(String playerName, String algorithm, List<Position> path, long timeTaken)
			throws DatabaseException {
		DatabaseConnection dc = DatabaseConnection.getInstance();
		dc.getDatastore().save(new KnightTourResult(playerName, algorithm, path, timeTaken));
	}

	/**
	 * Find solutions using brute force algorithm
	 * 
	 * @param startRow
	 * @param startCol
	 */
	public void useBruteForceAlgorithm(int startRow, int startCol) {
		BruteForce2 algorithm = new BruteForce2(KnightTourConstant.BOARD_SIZE);
		algorithm.calculate(startRow, startCol);
		calculatedMovements = algorithm.getBoard();
	}

	/**
	 * find solutions using warnsdorff algorithm
	 * 
	 * @param knightRow
	 * @param knightCol
	 */
	public void useWarnsdorffAlgorithm(int startRow, int startCol) {
		Warnsdorff algorithm = new Warnsdorff(KnightTourConstant.BOARD_SIZE);
		algorithm.calculate(startRow, startCol);
		calculatedMovements = algorithm.getBoard();
	}

	// getters and setters for service
	public boolean[][] getVisited() {
		return visited;
	}

	public void setVisited(boolean[][] visited) {
		this.visited = visited;
	}

	public int getVisitedCount() {
		return visitedCount;
	}

	public void setVisitedCount(int visitedCount) {
		this.visitedCount = visitedCount;
	}

	public int[][] getCalculatedMovements() {
		return calculatedMovements;
	}

	public int[][] getUserManualMovement() {
		return userManualMovement;
	}

	public void setUserManualMovement(int[][] userManualMovement) {
		this.userManualMovement = userManualMovement;
	}
}

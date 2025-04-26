package services;

import utils.constants.KnightTourConstant;

public class KnightTourService {
	//data structures to store game data
	private boolean[][] visited;
	private int visitedCount;

	/**
	 * Initialize initial values
	 */
	public KnightTourService() {
		visited = new boolean[KnightTourConstant.BOARD_SIZE][KnightTourConstant.BOARD_SIZE];
		visitedCount = 0;
	}

	//getters and setters for service
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
}

package utils.dsa.bruteforce2;

import java.util.Arrays;

public class BruteForce2 {

	private int boardSize;
	int[] rowMove = { -2, -1, 1, 2, 2, 1, -1, -2 };
	int[] colMove = { 1, 2, 2, 1, -1, -2, -2, -1 };
	int[][] board;

	/**
	 * Initialize the initial values
	 * 
	 * @param boardSize
	 */
	public BruteForce2(int boardSize) {
		this.boardSize = boardSize;
		// set all board slots to -1 unvisited
		board = new int[this.boardSize][this.boardSize];
		for (int i = 0; i < this.boardSize; i++)
			Arrays.fill(this.board[i], -1);
	}

	/**
	 * drive method to calculate solutions
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean calculate(int row, int col) {
		// set initial move
		board[row][col] = 0;
		// calculate the solutions
		return this.calculate(row, col, 1);
	}

	/**
	 * recursive calculate method to find solutions
	 * 
	 * @param row
	 * @param col
	 * @param moveCount
	 * @return
	 */
	private boolean calculate(int row, int col, int moveCount) {
		// check move count reached to game board size
		if (moveCount == this.boardSize * this.boardSize) {
			return true;
		}

		// check with the next moves
		for (int i = 0; i < this.boardSize; i++) {
			int nextRow = row + rowMove[i];
			int nextCol = col + colMove[i];

			if (isSafe(nextRow, nextCol)) {
				// set the next movement
				board[nextRow][nextCol] = moveCount;
				// recursion
				if (this.calculate(nextRow, nextCol, moveCount + 1)) {
					return true;
				} else {
					// back track
					board[nextRow][nextCol] = -1;
				}
			}
		}

		return false;
	}

	/**
	 * check next movement is valid or unvisited
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	private boolean isSafe(int row, int col) {
		return (row >= 0 && row < this.boardSize && col >= 0 && col < this.boardSize && board[row][col] == -1);
	}

	// getter for board
	public int[][] getBoard() {
		return board;
	}
}

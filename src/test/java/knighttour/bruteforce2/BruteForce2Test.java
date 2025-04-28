package knighttour.bruteforce2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import utils.dsa.bruteforce2.BruteForce2;

public class BruteForce2Test {

	/**
	 * Test algorithm for board initialization
	 */
	@Test
	void testBoardInitialization() {
		int size = 5;
		BruteForce2 bf = new BruteForce2(size);
		int[][] board = bf.getBoard();

		assertEquals(size, board.length);
		for (int i = 0; i < size; i++) {
			assertEquals(size, board[i].length);
			for (int j = 0; j < size; j++) {
				assertEquals(-1, board[i][j], "Board should initialize with -1 at (" + i + "," + j + ")");
			}
		}
	}

	/**
	 * Test algorithm for calculations all done and board was filled
	 */
	@Test
	void testSuccessfulCalculationBoard() {
		BruteForce2 bf = new BruteForce2(8);
		boolean result = bf.calculate(1, 1);
		assertTrue(result, "All calculations are done using algorithm");

		int[][] board = bf.getBoard();
		for (int i = 0; i < board.length; i++) {
			assertNotNull(board[i], "Board position " + i + " should not be null");
			for (int j = 0; j < board[i].length; j++) {
				assertTrue(board[i][j] >= 0 && board[i][j] < 8 * 8, "Invalid cell value: " + board[i][j]);
			}
		}
	}

	/**
	 * Test algorithm with different board size
	 */
	@Test
	void testSuccessfulCalculationOn6x6Board() {
		BruteForce2 bf = new BruteForce2(6);
		boolean result = bf.calculate(1, 1);
		assertTrue(result, "All calculations done using algorithm");
		int[][] board = bf.getBoard();
		for (int i = 0; i < board.length; i++) {
			assertNotNull(board[i], "Board position " + i + " should not be null");
			for (int j = 0; j < board[i].length; j++) {
				assertTrue(board[i][j] >= 0 && board[i][j] < 8 * 8, "Invalid cell value: " + board[i][j]);
			}
		}
	}

	/**
	 * Test algorithm with the small board size
	 */
	@Test
	void testSmallBoardNoSolution() {
		BruteForce2 bf = new BruteForce2(3);
		boolean result = bf.calculate(0, 0);
		assertFalse(result, "No knight's tour possible on a 3x3 board");
	}
}

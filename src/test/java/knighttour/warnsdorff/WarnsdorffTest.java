package knighttour.warnsdorff;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import utils.dsa.warnsdorff.Warnsdorff;

public class WarnsdorffTest {
	/**
	 * Test algorithm board initialization
	 */
	@Test
	public void testBoardInitialization() {
		Warnsdorff warnsdorff = new Warnsdorff(8);
		int[][] board = warnsdorff.getBoard();

		assertEquals(8, board.length, "board should have 8 rows");
		for (int[] row : board) {
			assertEquals(8, row.length, "each row should have 8 columns");
			for (int cell : row) {
				assertEquals(-1, cell, "all cells should be initialized to -1");
			}
		}
	}
	
	/**
	 * Test algorithm is done and all calculations were done
	 */
    @Test
    public void testSuccessfulCalculationBoard() {
        Warnsdorff warnsdorff = new Warnsdorff(8);
        boolean success = warnsdorff.calculate(0, 0);
        assertTrue(success, "All calculations are done using algorithm");
        int[][] board = warnsdorff.getBoard();
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
    public void testSuccessfulCalculationOn5x5Board() {
        Warnsdorff warnsdorff = new Warnsdorff(5);
        boolean success = warnsdorff.calculate(0, 0);
        assertTrue(success, "Knight's tour should be possible on a 5x5 board starting at (0,0)");
        
        int[][] board = warnsdorff.getBoard();
		for (int i = 0; i < board.length; i++) {
			assertNotNull(board[i], "Board position " + i + " should not be null");
			for (int j = 0; j < board[i].length; j++) {
				assertTrue(board[i][j] >= 0 && board[i][j] < 8 * 8, "Invalid cell value: " + board[i][j]);
			}
		}
    }
    
    /**
     * Test algorithm with small board size
     */
    @Test
    public void testSmallBoardNoSolution() {
        Warnsdorff warnsdorff = new Warnsdorff(3);
        boolean success = warnsdorff.calculate(0, 0);
        assertFalse(success, "Knight's tour should not be possible on a 3x3 board");
    }
    
    
}

package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class TicTacToeAITest {
    private TicTacToeAI ai;
    private char[][] board;
    private static final int SIZE = 5;

    @BeforeEach
    void setUp() {
        ai = new TicTacToeAI();
        board = new char[SIZE][SIZE];
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard() {
        for(int i=0; i<SIZE; i++) {
            for(int j=0; j<SIZE; j++) {
                board[i][j] = ' ';
            }
        }
    }

    @Test
    void testGetBestMoveWithMinimax_WinningMove() {
        // Create near-win scenario for AI
        board[2][0] = 'O'; board[2][1] = 'O'; 
        board[2][2] = 'O'; board[2][3] = 'O';
        
        int[] move = ai.getBestMoveWithMinimax(board);
        assertArrayEquals(new int[]{2,4}, move);
    }

    @Test
    void testCheckWin_Horizontal() {
        // Create horizontal win
        for(int j=0; j<5; j++) board[3][j] = 'X';
        assertTrue(ai.checkWin(board, 'X'));
    }

    @Test
    void testIsBoardFull() {
        // Fill all cells except one
        for(int i=0; i<SIZE; i++) {
            Arrays.fill(board[i], 'X');
        }
        board[4][4] = ' ';
        assertFalse(ai.isBoardFull(board));
    }

    @Test
    void testAlphaBetaPruning_Efficiency() {
        // Compare move times between algorithms
        long startMinimax = System.currentTimeMillis();
        ai.getBestMoveWithMinimax(board);
        long timeMinimax = System.currentTimeMillis() - startMinimax;
        
        long startAlphaBeta = System.currentTimeMillis();
        ai.getBestMoveWithAlphaBeta(board);
        long timeAlphaBeta = System.currentTimeMillis() - startAlphaBeta;
        
        assertTrue(timeAlphaBeta <= timeMinimax);
    }

    @Test
    void testBlockHumanWin() {
        // Setup human near-win
        board[1][0] = 'X'; board[1][1] = 'X';
        board[1][2] = 'X'; board[1][3] = 'X';
        
        int[] move = ai.getBestMoveWithAlphaBeta(board);
        assertEquals(1, move[0]);
        assertEquals(4, move[1]);
    }
}

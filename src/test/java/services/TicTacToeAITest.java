package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class TicTacToeAITest {
    private TicTacToeAI ai;  // AI object for the Tic-Tac-Toe game
    private char[][] board;  // 5x5 board for the Tic-Tac-Toe game
    private static final int SIZE = 5;  // Size of the Tic-Tac-Toe board

    // Setup method to initialize the AI and empty board before each test
    @BeforeEach
    void setUp() {
        ai = new TicTacToeAI();  // Instantiate AI
        board = new char[SIZE][SIZE];  // Initialize board with the defined size
        initializeEmptyBoard();  // Method call to initialize the board to empty state
    }

    // Helper method to initialize the board with empty spaces
    private void initializeEmptyBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = ' ';  // Set each cell to empty space
            }
        }
    }

    // Test case for Minimax algorithm when AI has a winning move
    @Test
    void testGetBestMoveWithMinimax_WinningMove() {
        // Setup a near-win scenario for AI ('O')
        board[2][0] = 'O'; board[2][1] = 'O'; 
        board[2][2] = 'O'; board[2][3] = 'O';
        
        // Get the best move for AI using Minimax algorithm
        int[] move = ai.getBestMoveWithMinimax(board);
        
        // Assert that the best move is to place 'O' at [2, 4] to win the game
        assertArrayEquals(new int[]{2,4}, move);
    }

    // Test case to check if the AI correctly detects a horizontal win
    @Test
    void testCheckWin_Horizontal() {
        // Setup a horizontal win for 'X'
        for (int j = 0; j < 5; j++) {
            board[3][j] = 'X';  // Fill the entire row 3 with 'X'
        }
        
        // Assert that AI detects a win for 'X' in row 3
        assertTrue(ai.checkWin(board, 'X'));
    }

    // Test case to check if the AI correctly detects when the board is not full
    @Test
    void testIsBoardFull() {
        // Fill all cells except for one
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(board[i], 'X');  // Fill the entire board with 'X'
        }
        board[4][4] = ' ';  // Leave one cell empty

        // Assert that the board is not full as there is one empty cell
        assertFalse(ai.isBoardFull(board));
    }

    // Test case to compare the efficiency of Minimax vs Alpha-Beta pruning algorithms
    @Test
    void testAlphaBetaPruning_Efficiency() {
        // Record the time taken by Minimax algorithm
        long startMinimax = System.currentTimeMillis();
        ai.getBestMoveWithMinimax(board);  // Get the best move using Minimax
        long timeMinimax = System.currentTimeMillis() - startMinimax;  // Calculate time taken
        
        // Record the time taken by Alpha-Beta pruning algorithm
        long startAlphaBeta = System.currentTimeMillis();
        ai.getBestMoveWithAlphaBeta(board);  // Get the best move using Alpha-Beta
        long timeAlphaBeta = System.currentTimeMillis() - startAlphaBeta;  // Calculate time taken
        
        // Assert that Alpha-Beta pruning is faster or equal to Minimax
        assertTrue(timeAlphaBeta <= timeMinimax);
    }

    // Test case to check if the AI blocks a winning move by the human player
    @Test
    void testBlockHumanWin() {
        // Setup a near-win situation for human ('X')
        board[1][0] = 'X'; board[1][1] = 'X';
        board[1][2] = 'X'; board[1][3] = 'X';
        
        // Get the best move for AI using Alpha-Beta pruning to block human win
        int[] move = ai.getBestMoveWithAlphaBeta(board);
        
        // Assert that the AI blocks the human's win by placing 'O' at [1, 4]
        assertEquals(1, move[0]);
        assertEquals(4, move[1]);
    }
}

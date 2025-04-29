package services;

public class TicTacToeAI {
    private static final int BOARD_SIZE = 5; // Size of the Tic-Tac-Toe board (5x5)
    private static final char EMPTY = ' ';   // Character representing an empty space
    private static final char HUMAN = 'X';   // Character representing the human player
    private static final char AI = 'O';      // Character representing the AI player
    
    private static final int MAX_DEPTH = 4;   // Limit the depth of the search tree to reduce thinking time

    // Method to get the best move using Minimax algorithm
    public int[] getBestMoveWithMinimax(char[][] board) {
        long startTime = System.currentTimeMillis(); // Record the start time
        int[] bestMove = new int[]{-1, -1}; // Store the best move
        int bestScore = Integer.MIN_VALUE;  // Initialize best score to a very low value
        
        // Loop through all cells in the board
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {  // Only consider empty cells
                    board[i][j] = AI;  // Try the AI's move
                    int score = minimax(board, 0, false);  // Get the score from Minimax
                    board[i][j] = EMPTY;  // Undo the move

                    // Update best move if a better score is found
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        
        long timeTaken = System.currentTimeMillis() - startTime; // Calculate the time taken for the move
        System.out.println("Minimax time: " + timeTaken + "ms"); // Print the time taken
        return bestMove; // Return the best move
    }

    // Minimax algorithm to calculate the best possible move
    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        // Base cases
        if (checkWin(board, AI)) return 10 - depth; // AI wins, return positive score
        if (checkWin(board, HUMAN)) return depth - 10; // Human wins, return negative score
        if (isBoardFull(board) || depth >= MAX_DEPTH) return 0; // Draw or max depth reached, return 0
        
        if (isMaximizing) { // AI's turn (maximize score)
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {  // Only consider empty cells
                        board[i][j] = AI;  // Try the AI's move
                        int score = minimax(board, depth + 1, false);  // Recursively call minimax for the next turn
                        board[i][j] = EMPTY;  // Undo the move
                        bestScore = Math.max(score, bestScore);  // Update best score
                    }
                }
            }
            return bestScore;
        } else { // Human's turn (minimize score)
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {  // Only consider empty cells
                        board[i][j] = HUMAN;  // Try the human's move
                        int score = minimax(board, depth + 1, true);  // Recursively call minimax for the next turn
                        board[i][j] = EMPTY;  // Undo the move
                        bestScore = Math.min(score, bestScore);  // Update best score
                    }
                }
            }
            return bestScore;
        }
    }

    // Method to get the best move using Alpha-Beta Pruning
    public int[] getBestMoveWithAlphaBeta(char[][] board) {
        long startTime = System.currentTimeMillis(); // Record the start time
        int[] bestMove = new int[]{-1, -1}; // Store the best move
        int bestScore = Integer.MIN_VALUE;  // Initialize best score to a very low value
        
        // Loop through all cells in the board
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {  // Only consider empty cells
                    board[i][j] = AI;  // Try the AI's move
                    int score = alphaBeta(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);  // Get the score from Alpha-Beta Pruning
                    board[i][j] = EMPTY;  // Undo the move
                    
                    // Update best move if a better score is found
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        
        long timeTaken = System.currentTimeMillis() - startTime; // Calculate the time taken for the move
        System.out.println("Alpha-Beta time: " + timeTaken + "ms"); // Print the time taken
        return bestMove; // Return the best move
    }

    // Alpha-Beta Pruning algorithm to calculate the best possible move
    private int alphaBeta(char[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        // Base cases
        if (checkWin(board, AI)) return 10 - depth; // AI wins, return positive score
        if (checkWin(board, HUMAN)) return depth - 10; // Human wins, return negative score
        if (isBoardFull(board) || depth >= MAX_DEPTH) return 0; // Draw or max depth reached, return 0
        
        if (isMaximizing) { // AI's turn (maximize score)
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {  // Only consider empty cells
                        board[i][j] = AI;  // Try the AI's move
                        int score = alphaBeta(board, depth + 1, alpha, beta, false);  // Recursively call alphaBeta for the next turn
                        board[i][j] = EMPTY;  // Undo the move
                        bestScore = Math.max(score, bestScore);  // Update best score
                        alpha = Math.max(alpha, bestScore);  // Update alpha
                        if (beta <= alpha) break;  // Beta pruning: stop searching if the current branch is worse than the best already found
                    }
                }
                if (beta <= alpha) break;  // Beta pruning: stop searching if the current branch is worse than the best already found
            }
            return bestScore;
        } else { // Human's turn (minimize score)
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {  // Only consider empty cells
                        board[i][j] = HUMAN;  // Try the human's move
                        int score = alphaBeta(board, depth + 1, alpha, beta, true);  // Recursively call alphaBeta for the next turn
                        board[i][j] = EMPTY;  // Undo the move
                        bestScore = Math.min(score, bestScore);  // Update best score
                        beta = Math.min(beta, bestScore);  // Update beta
                        if (beta <= alpha) break;  // Alpha pruning: stop searching if the current branch is worse than the best already found
                    }
                }
                if (beta <= alpha) break;  // Alpha pruning: stop searching if the current branch is worse than the best already found
            }
            return bestScore;
        }
    }

    // Method to check if a player has won the game
    public boolean checkWin(char[][] board, char player) {
        // Check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            int count = 0;
            for (int j = 0; j < BOARD_SIZE; j++) {
                count = (board[i][j] == player) ? count + 1 : 0;
                if (count == 5) return true;  // Found a winning row
            }
        }
        
        // Check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            int count = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                count = (board[i][j] == player) ? count + 1 : 0;
                if (count == 5) return true;  // Found a winning column
            }
        }
        
        // Check diagonals
        for (int i = 0; i <= BOARD_SIZE - 5; i++) {
            for (int j = 0; j <= BOARD_SIZE - 5; j++) {
                if (checkDiagonal(board, i, j, player)) return true;
                if (checkAntiDiagonal(board, i, j + 4, player)) return true;
            }
        }
        return false;
    }

    // Method to check if a player has a diagonal win
    private boolean checkDiagonal(char[][] board, int row, int col, char player) {
        for (int k = 0; k < 5; k++) {
            if (board[row + k][col + k] != player) return false;
        }
        return true;
    }

    // Method to check if a player has an anti-diagonal win
    private boolean checkAntiDiagonal(char[][] board, int row, int col, char player) {
        for (int k = 0; k < 5; k++) {
            if (board[row + k][col - k] != player) return false;
        }
        return true;
    }

    // Method to check if the board is full (no empty spaces)
    public boolean isBoardFull(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) return false;  // If any cell is empty, return false
            }
        }
        return true;  // All cells are filled, return true
    }
}

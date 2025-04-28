package services;

public class TicTacToeAI {
    private static final int BOARD_SIZE = 5;
    private static final char EMPTY = ' ';
    private static final char HUMAN = 'X';
    private static final char AI = 'O';
    
    private static final int MAX_DEPTH = 4; // Limit depth to reduce thinking time!

    public int[] getBestMoveWithMinimax(char[][] board) {
        long startTime = System.currentTimeMillis();
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = AI;
                    int score = minimax(board, 0, false);
                    board[i][j] = EMPTY;
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        
        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.println("Minimax time: " + timeTaken + "ms");
        return bestMove;
    }

    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        if (checkWin(board, AI)) return 10 - depth;
        if (checkWin(board, HUMAN)) return depth - 10;
        if (isBoardFull(board) || depth >= MAX_DEPTH) return 0;
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = AI;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = HUMAN;
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }

    public int[] getBestMoveWithAlphaBeta(char[][] board) {
        long startTime = System.currentTimeMillis();
        int[] bestMove = new int[]{-1, -1};
        int bestScore = Integer.MIN_VALUE;
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = AI;
                    int score = alphaBeta(board, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                    board[i][j] = EMPTY;
                    
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        
        long timeTaken = System.currentTimeMillis() - startTime;
        System.out.println("Alpha-Beta time: " + timeTaken + "ms");
        return bestMove;
    }

    private int alphaBeta(char[][] board, int depth, int alpha, int beta, boolean isMaximizing) {
        if (checkWin(board, AI)) return 10 - depth;
        if (checkWin(board, HUMAN)) return depth - 10;
        if (isBoardFull(board) || depth >= MAX_DEPTH) return 0;
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = AI;
                        int score = alphaBeta(board, depth + 1, alpha, beta, false);
                        board[i][j] = EMPTY;
                        bestScore = Math.max(score, bestScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) break;
                    }
                }
                if (beta <= alpha) break;
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (board[i][j] == EMPTY) {
                        board[i][j] = HUMAN;
                        int score = alphaBeta(board, depth + 1, alpha, beta, true);
                        board[i][j] = EMPTY;
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, bestScore);
                        if (beta <= alpha) break;
                    }
                }
                if (beta <= alpha) break;
            }
            return bestScore;
        }
    }

    public boolean checkWin(char[][] board, char player) {
        // Same logic as you wrote - no change needed
        // check rows, columns, diagonals
        for (int i = 0; i < BOARD_SIZE; i++) {
            int count = 0;
            for (int j = 0; j < BOARD_SIZE; j++) {
                count = (board[i][j] == player) ? count + 1 : 0;
                if (count == 5) return true;
            }
        }
        for (int j = 0; j < BOARD_SIZE; j++) {
            int count = 0;
            for (int i = 0; i < BOARD_SIZE; i++) {
                count = (board[i][j] == player) ? count + 1 : 0;
                if (count == 5) return true;
            }
        }
        for (int i = 0; i <= BOARD_SIZE - 5; i++) {
            for (int j = 0; j <= BOARD_SIZE - 5; j++) {
                if (checkDiagonal(board, i, j, player)) return true;
                if (checkAntiDiagonal(board, i, j + 4, player)) return true;
            }
        }
        return false;
    }

    private boolean checkDiagonal(char[][] board, int row, int col, char player) {
        for (int k = 0; k < 5; k++) {
            if (board[row + k][col + k] != player) return false;
        }
        return true;
    }

    private boolean checkAntiDiagonal(char[][] board, int row, int col, char player) {
        for (int k = 0; k < 5; k++) {
            if (board[row + k][col - k] != player) return false;
        }
        return true;
    }

    public boolean isBoardFull(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY) return false;
            }
        }
        return true;
    }
}

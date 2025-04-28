package utils.dsa.warnsdorff;

import java.util.Arrays;

public class Warnsdorff {
	
	private int boardSize;
	
	//possible moves
    private int[] rowMove = { -2, -1, 1, 2, 2, 1, -1, -2 };
    private int[] colMove = { 1, 2, 2, 1, -1, -2, -2, -1 };
    
    //game board
    private int[][] board;
    
    /**
     * Initialize the initial values 
     * 
     * @param boardSize
     */
    public Warnsdorff(int boardSize) {
    	this.boardSize = boardSize;
        board = new int[this.boardSize][this.boardSize];
        for (int i = 0; i < this.boardSize; i++) {
            Arrays.fill(board[i], -1);
        }
    }
    
    /**
     * choose the move with the fewest onward moves
     * 
     * @param startRow
     * @param startCol
     * @return
     */
    public boolean calculate(int startRow, int startCol) {
    	// set starting position as visited
    	board[startRow][startCol] = 0;

        // start the recursive backtracking to find the knight's tour
        if (!findTourUtil(startRow, startCol, 1)) {
            return false;
        }
        return true;
    }
    
    /**
     * Recursive function to find the knight's tour using Warnsdorff's Rule
     * 
     * @param row
     * @param col
     * @param moveCount
     * @return
     */
    private boolean findTourUtil(int row, int col, int moveCount) {
        if (moveCount == this.boardSize * this.boardSize) {
            return true;
        }

        // try all possible moves based on Warnsdorff's Rule
        int[][] nextMoves = new int[8][2];
        for (int i = 0; i < 8; i++) {
        	// storing move coordinates
            nextMoves[i][0] = row + rowMove[i];
            nextMoves[i][1] = col + colMove[i];
        }

        // sort possible moves by the number of onward moves (fewest first)
        Arrays.sort(nextMoves, (a, b) -> this.countOnwardMoves(a[0], a[1]) - this.countOnwardMoves(b[0], b[1]));

        // find moves in order of fewest onward moves
        for (int i = 0; i < 8; i++) {
            int nextRow = nextMoves[i][0];
            int nextCol = nextMoves[i][1];
            if (this.isSafe(nextRow, nextCol)) {
                board[nextRow][nextCol] = moveCount;
                if (this.findTourUtil(nextRow, nextCol, moveCount + 1)) {
                    return true;
                } else {
                	// backtrack
                    board[nextRow][nextCol] = -1;
                }
            }
        }
        return false;
    }
    
    
    /**
     * count the number of onward moves for each possible next square
     * 
     * @param row
     * @param col
     * @return
     */
    private int countOnwardMoves(int row, int col) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int newRow = row + rowMove[i];
            int newCol = col + colMove[i];
            if (this.isSafe(newRow, newCol)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check next movement is valid or unvisited
     * 
     * @param row
     * @param col
     * @return
     */
    private boolean isSafe(int row, int col) {
        return (row >= 0 && row < this.boardSize && col >= 0 && col < this.boardSize && board[row][col] == -1);
    }

	
    //getters and setters
    public int[][] getBoard() {
		return board;
	}
	

	public void setBoard(int[][] board) {
		this.board = board;
	}    
}

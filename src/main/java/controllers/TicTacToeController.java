package controllers;

import services.TicTacToeAI;
import services.TicTacToeService;
import views.TicTacToeView;

import javax.swing.*;
import java.awt.*;

public class TicTacToeController {

    // Declare required variables
    private final TicTacToeView view; // View to display the game board and status
    private final TicTacToeService service; // Service to save the game results
    private final TicTacToeAI ai; // AI logic for the computer player
    private char[][] board; // 2D array to represent the game board
    private boolean gameOver; // Flag to check if the game is over
    private boolean useAlphaBeta; // Flag to toggle between Alpha-Beta pruning and Minimax algorithm
    private String playerName; // Player's name
    private static final int BOARD_SIZE = 5; // Board size (5x5)
    private static final char EMPTY = ' '; // Empty cell representation
    private static final char HUMAN = 'X'; // Human player (X)
    private static final char AI = 'O'; // AI player (O)
    private JFrame frame; // JFrame to hold the view

    // Constructor to initialize the controller with view and service
    public TicTacToeController(TicTacToeView view, TicTacToeService service) {
        this.view = view;
        this.service = service;
        this.ai = new TicTacToeAI(); // Initialize AI logic
        initListeners(); // Initialize listeners for user interactions
    }

    // Show the view of the TicTacToe game
    public void showView() {
        // If frame is not already created, initialize it
        if (frame == null) {
            frame = new JFrame("TicTacToe Game"); // Create a new frame for the game
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Set default close operation
            frame.add(view); // Add the view component to the frame
            frame.setPreferredSize(new Dimension(800, 600)); // Set preferred size of the frame
            frame.pack(); // Pack the components in the frame
            frame.setLocationRelativeTo(null); // Center the frame on the screen
        }
        frame.setVisible(true); // Make the frame visible
        initializeGame(); // Initialize the game board and settings
    }

    // Hide the view when the game is closed
    public void hideView() {
        if (frame != null) {
            frame.setVisible(false); // Hide the frame
            frame.dispose(); // Dispose of the frame to free resources
            frame = null; // Set frame to null for later re-creation
        }
    }

    // Initialize the game (reset board, game status, etc.)
    private void initializeGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE]; // Initialize a new 5x5 game board
        // Fill the board with empty cells
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        gameOver = false; // Set gameOver flag to false
        useAlphaBeta = false; // Default to Minimax algorithm
        // Ask the player to input their name
        playerName = JOptionPane.showInputDialog(
                view.getFrame(),
                "Enter your name:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE
        );
        // If player does not provide a name, use default name "Player"
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }
        // Update the game board and status label
        view.updateBoard(board);
        view.setStatusLabel("Your turn (" + playerName + ")");
        view.setAlgorithmLabel("AI Algorithm: " + (useAlphaBeta ? "Alpha-Beta" : "Minimax"));
    }

    // Initialize listeners for user interactions with the game
    private void initListeners() {
        // Add listener for board button clicks (player's move)
        view.addBoardButtonListener((row, col) -> {
            // Check if the game is over or if the cell is already filled
            if (!gameOver && board[row][col] == EMPTY) {
                makeMove(row, col, HUMAN); // Make the player's move
                view.setLastMove(row, col); // Track the last move made by the player

                // Check if the player wins after the move
                if (ai.checkWin(board, HUMAN)) {
                    gameOver = true;
                    view.setStatusLabel("You win!"); // Update status label for player win
                    highlightWinningCells(HUMAN); // Highlight winning cells
                    // Save the result of the game
                    service.saveResult(
                        playerName,
                        row,
                        col,
                        useAlphaBeta ? "AlphaBeta" : "Minimax",
                        0,
                        true
                    );
                    saveResultToDatabase(playerName); // Save result to database
                    disableBoard(); // Disable the board to prevent further moves
                    displayGameOver(playerName); // Display game over message
                } else if (ai.isBoardFull(board)) {
                    gameOver = true;
                    view.setStatusLabel("It's a draw!"); // Update status label for draw
                    saveResultToDatabase("Draw"); // Save draw result to database
                    disableBoard(); // Disable the board
                    displayGameOver("Draw"); // Display game over message
                } else if (!gameOver) {
                    view.setStatusLabel("Computer is thinking..."); // Notify player that AI is making a move
                    SwingUtilities.invokeLater(this::makeComputerMove); // Make computer move in the next event dispatch thread
                }
            }
        });

        // Add listener for toggling AI algorithm (Minimax / AlphaBeta)
        view.addAlgorithmToggleListener(e -> {
            useAlphaBeta = !useAlphaBeta; // Toggle algorithm
            view.setAlgorithmLabel("AI Algorithm: " + (useAlphaBeta ? "Alpha-Beta" : "Minimax")); // Update label
        });

        // Add listener for starting a new game
        view.addNewGameListener(e -> initializeGame());

        // Add listener for going back to the main menu
        view.addBackToMenuListener(e -> {
            hideView();
            JOptionPane.showMessageDialog(view.getFrame(), "Returning to Main Menu"); // Show message
        });
    }

    // Make a move on the board (either by human or AI)
    private void makeMove(int row, int col, char player) {
        board[row][col] = player; // Set the cell to the player's symbol
        view.updateBoard(board); // Update the view with the new board state
    }

    // Make the computer's move (AI's turn)
    private void makeComputerMove() {
        if (gameOver) return; // If game is over, do nothing

        int[] move; // Array to store AI's move (row, col)
        long startTime = System.currentTimeMillis(); // Track the time taken for AI move
        String algorithm = useAlphaBeta ? "AlphaBeta" : "Minimax"; // Get the algorithm being used
        // Determine the best move based on the selected algorithm
        if (useAlphaBeta) {
            move = ai.getBestMoveWithAlphaBeta(board);
        } else {
            move = ai.getBestMoveWithMinimax(board);
        }
        long timeTaken = System.currentTimeMillis() - startTime; // Calculate the time taken for the move

        // If the move is valid, make the move on the board
        if (move[0] != -1 && move[1] != -1) {
            makeMove(move[0], move[1], AI); // Make AI's move
            view.setLastMove(move[0], move[1]); // Track AI's last move

            // Check if AI wins after the move
            if (ai.checkWin(board, AI)) {
                gameOver = true;
                view.setStatusLabel("Computer wins!"); // Update status label for AI win
                highlightWinningCells(AI); // Highlight winning cells
                // Save the result of the game
                service.saveResult(
                    "Computer",
                    move[0],
                    move[1],
                    algorithm,
                    timeTaken,
                    true
                );
                saveResultToDatabase("Computer"); // Save result to database
                disableBoard(); // Disable the board
                displayGameOver("Computer"); // Display game over message
            } else if (ai.isBoardFull(board)) {
                gameOver = true;
                view.setStatusLabel("It's a draw!"); // Update status label for draw
                saveResultToDatabase("Draw"); // Save draw result to database
                disableBoard(); // Disable the board
                displayGameOver("Draw"); // Display game over message
            } else {
                view.setStatusLabel("Your turn (" + playerName + ")"); // Notify player that it's their turn
            }
        }
    }

    // Display the game over message depending on the winner
    private void displayGameOver(String winner) {
        if ("Computer".equals(winner)) {
            System.out.println("Computer wins!"); // Display computer win
        } else if ("Draw".equals(winner)) {
            System.out.println("It's a draw!"); // Display draw
        } else {
            System.out.println(winner + " wins!"); // Display player's win
        }
    }

    // Highlight the cells that contributed to the winning move
    private void highlightWinningCells(char player) {
        view.highlightWinningCells(player, board); // Call the view method to highlight the winning cells
    }

    // Save the result of the game to the database
    private void saveResultToDatabase(String winner) {
        service.saveFinalResult(playerName, winner); // Call service to save the final result
    }

    // Disable the board and prevent further moves after the game is over
    private void disableBoard() {
        gameOver = true; // Set gameOver flag to true
        view.disableBoard(); // Call the view method to disable the board
    }
}

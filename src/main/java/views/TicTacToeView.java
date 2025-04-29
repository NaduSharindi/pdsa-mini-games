package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

public class TicTacToeView extends JPanel {
    private static final int BOARD_SIZE = 5;  // Size of the Tic-Tac-Toe board (5x5)

    private final JButton[][] boardButtons;  // 2D array to hold the buttons on the board
    private final JLabel statusLabel;  // Label to show game status (e.g., "Game Over")
    private final JLabel algorithmLabel;  // Label to display the current AI algorithm
    private final JButton toggleAlgorithmButton;  // Button to toggle between algorithms
    private final JButton newGameButton;  // Button to start a new game
    private final JButton backToMenuButton;  // Button to return to the main menu
    private static final char HUMAN = 'X';  // Player's symbol (X)
    private static final char COMPUTER = 'O';  // AI's symbol (O)
    private int lastMoveRow = -1;  // The row of the last move made
    private int lastMoveCol = -1;  // The column of the last move made


    public TicTacToeView() {
        setLayout(new BorderLayout());  // Set layout manager for the panel

        // Board panel (5x5 grid)
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));  // Grid layout for the board
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];  // Initialize the board buttons
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardButtons[i][j] = new JButton("");  // Create an empty button
                boardButtons[i][j].setFont(new Font("Arial", Font.BOLD, 28));  // Set button font
                boardButtons[i][j].setFocusPainted(false);  // Disable focus paint for buttons
                boardButtons[i][j].setPreferredSize(new Dimension(70, 70));  // Set button size
                boardPanel.add(boardButtons[i][j]);  // Add button to the panel
            }
        }

        // Status and controls panel
        JPanel statusPanel = new JPanel();  // Panel to hold the status labels and buttons
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));  // Set vertical layout

        // Status label
        statusLabel = new JLabel("Welcome to Tic-Tac-Toe!");  // Initial status text
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));  // Set font for status label
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align the label

        // Algorithm label
        algorithmLabel = new JLabel("AI Algorithm: Minimax");  // Initial algorithm text
        algorithmLabel.setFont(new Font("Arial", Font.PLAIN, 14));  // Set font for algorithm label
        algorithmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center align the label

        // Panel for action buttons
        JPanel buttonsPanel = new JPanel();  
        toggleAlgorithmButton = new JButton("Toggle Algorithm");  // Button to toggle algorithm
        toggleAlgorithmButton.setFont(new Font("Arial", Font.PLAIN, 14));  // Set font for the button
        newGameButton = new JButton("New Game");  // Button to start a new game
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 14));  // Set font for the button
        backToMenuButton = new JButton("Back to Main Menu");  // Button to return to main menu
        backToMenuButton.setFont(new Font("Arial", Font.PLAIN, 14));  // Set font for the button
        buttonsPanel.add(toggleAlgorithmButton);  // Add toggle button to panel
        buttonsPanel.add(newGameButton);  // Add new game button to panel
        buttonsPanel.add(backToMenuButton);  // Add back to menu button to panel

        // Add components to the status panel
        statusPanel.add(Box.createVerticalStrut(10));  // Add vertical space
        statusPanel.add(statusLabel);  // Add status label to panel
        statusPanel.add(Box.createVerticalStrut(5));  // Add vertical space
        statusPanel.add(algorithmLabel);  // Add algorithm label to panel
        statusPanel.add(Box.createVerticalStrut(10));  // Add vertical space
        statusPanel.add(buttonsPanel);  // Add buttons panel to status panel
        statusPanel.add(Box.createVerticalStrut(10));  // Add vertical space

        // Add the board and status panels to the main panel
        add(boardPanel, BorderLayout.CENTER);  // Add board to the center of the panel
        add(statusPanel, BorderLayout.SOUTH);  // Add status panel to the bottom of the panel
    }

    /**
     * Updates the board display with the current state.
     */
    public void updateBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // Update button text based on the board state
                boardButtons[i][j].setText(board[i][j] == ' ' ? "" : String.valueOf(board[i][j]));
                boardButtons[i][j].setBackground(null);  // Reset background color
                boardButtons[i][j].setEnabled(true);  // Enable buttons for new game
            }
        }
    }

    /**
     * Sets the status label text.
     */
    public void setStatusLabel(String text) {
        statusLabel.setText(text);  // Update status label text
    }

    /**
     * Sets the algorithm label text.
     */
    public void setAlgorithmLabel(String text) {
        algorithmLabel.setText(text);  // Update algorithm label text
    }

    /**
     * Adds a listener for board button clicks.
     */
    public void addBoardButtonListener(BiConsumer<Integer, Integer> listener) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                final int row = i;
                final int col = j;
                // Remove previous listeners to avoid duplicate events
                for (ActionListener al : boardButtons[i][j].getActionListeners()) {
                    boardButtons[i][j].removeActionListener(al);
                }
                boardButtons[i][j].addActionListener(e -> listener.accept(row, col));  // Add new listener
            }
        }
    }

    /**
     * Adds a listener for the algorithm toggle button.
     */
    public void addAlgorithmToggleListener(ActionListener listener) {
        toggleAlgorithmButton.addActionListener(listener);  // Add listener to toggle algorithm button
    }

    /**
     * Adds a listener for the new game button.
     */
    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);  // Add listener to new game button
    }
    
    /**
     * Adds a listener for the back to main menu button.
     */
    public void addBackToMenuListener(ActionListener listener) {
        backToMenuButton.addActionListener(listener);  // Add listener to back to menu button
    }    
    
    /**
     * Helper method to check if a line (row, column, or diagonal) is a winning line.
     */
    private boolean isWinningLine(char[] line, char player) {
        for (char cell : line) {
            if (cell != player) {  // If any cell doesn't match the player, it's not a win
                return false;
            }
        }
        return true;  // All cells match the player, it's a win
    }

    /**
     * Highlights the winning cells for the given player symbol.
     */
    public void highlightWinningCells(char player, char[][] board) {
        // Iterate through the board to find the winning row, column, or diagonal
        for (int i = 0; i < BOARD_SIZE; i++) {
            // Check rows for a win
            if (isWinningLine(board[i], player)) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    boardButtons[i][j].setBackground(player == HUMAN ? Color.BLUE : Color.RED);  // Color winning cells
                }
            }

            // Check columns for a win
            char[] column = new char[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                column[j] = board[j][i];
            }
            if (isWinningLine(column, player)) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    boardButtons[j][i].setBackground(player == HUMAN ? Color.BLUE : Color.RED);  // Color winning cells
                }
            }
        }

        // Check diagonals for a win
        char[] mainDiagonal = new char[BOARD_SIZE];
        char[] antiDiagonal = new char[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            mainDiagonal[i] = board[i][i];
            antiDiagonal[i] = board[i][BOARD_SIZE - i - 1];
        }
        if (isWinningLine(mainDiagonal, player)) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                boardButtons[i][i].setBackground(player == HUMAN ? Color.BLUE : Color.RED);  // Color winning cells
            }
        }
        if (isWinningLine(antiDiagonal, player)) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                boardButtons[i][BOARD_SIZE - i - 1].setBackground(player == HUMAN ? Color.BLUE : Color.RED);  // Color winning cells
            }
        }
    }


    /**
     * Disables the entire board (no more clicks allowed).
     */
    public void disableBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardButtons[i][j].setEnabled(false);  // Disable all board buttons
            }
        }
    }

    /**
     * Returns the top-level frame containing this view, if any.
     */
    public JFrame getFrame() {
        // Traverse up the component hierarchy to find the JFrame
        Container c = this;
        while (c != null && !(c instanceof JFrame)) {
            c = c.getParent();
        }
        return (JFrame) c;  // Return the JFrame
    }
    
    public int getLastMoveRow() {
        return lastMoveRow;  // Return the row of the last move
    }

    public int getLastMoveCol() {
        return lastMoveCol;  // Return the column of the last move
    }

    public void setLastMove(int row, int col) {
        this.lastMoveRow = row;  // Set the row of the last move
        this.lastMoveCol = col;  // Set the column of the last move
    }
}

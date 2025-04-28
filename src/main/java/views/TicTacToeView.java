package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

public class TicTacToeView extends JPanel {
    private static final int BOARD_SIZE = 5;

    private final JButton[][] boardButtons;
    private final JLabel statusLabel;
    private final JLabel algorithmLabel;
    private final JButton toggleAlgorithmButton;
    private final JButton newGameButton;
    private final JButton backToMenuButton; 
    private static final char HUMAN = 'X';  // Player's symbol
    private static final char COMPUTER = 'O';  // AI's symbol
    private int lastMoveRow = -1;
    private int lastMoveCol = -1;


    public TicTacToeView() {
        setLayout(new BorderLayout());

        // Board panel (5x5 grid)
        JPanel boardPanel = new JPanel(new GridLayout(BOARD_SIZE, BOARD_SIZE));
        boardButtons = new JButton[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardButtons[i][j] = new JButton("");
                boardButtons[i][j].setFont(new Font("Arial", Font.BOLD, 28));
                boardButtons[i][j].setFocusPainted(false);
                boardButtons[i][j].setPreferredSize(new Dimension(70, 70));
                boardPanel.add(boardButtons[i][j]);
            }
        }

        // Status and controls panel
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

        statusLabel = new JLabel("Welcome to Tic-Tac-Toe!");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        algorithmLabel = new JLabel("AI Algorithm: Minimax");
        algorithmLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        algorithmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonsPanel = new JPanel();
        toggleAlgorithmButton = new JButton("Toggle Algorithm");
        toggleAlgorithmButton.setFont(new Font("Arial", Font.PLAIN, 14));
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backToMenuButton = new JButton("Back to Main Menu");
        backToMenuButton.setFont(new Font("Arial", Font.PLAIN, 14));
        buttonsPanel.add(toggleAlgorithmButton);
        buttonsPanel.add(newGameButton);
        buttonsPanel.add(backToMenuButton);

        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(statusLabel);
        statusPanel.add(Box.createVerticalStrut(5));
        statusPanel.add(algorithmLabel);
        statusPanel.add(Box.createVerticalStrut(10));
        statusPanel.add(buttonsPanel);
        statusPanel.add(Box.createVerticalStrut(10));

        add(boardPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }

    /**
     * Updates the board display with the current state.
     */
    public void updateBoard(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardButtons[i][j].setText(board[i][j] == ' ' ? "" : String.valueOf(board[i][j]));
                boardButtons[i][j].setBackground(null); // Reset background color
                boardButtons[i][j].setEnabled(true); // Enable buttons for new game
            }
        }
    }

    /**
     * Sets the status label text.
     */
    public void setStatusLabel(String text) {
        statusLabel.setText(text);
    }

    /**
     * Sets the algorithm label text.
     */
    public void setAlgorithmLabel(String text) {
        algorithmLabel.setText(text);
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
                boardButtons[i][j].addActionListener(e -> listener.accept(row, col));
            }
        }
    }

    /**
     * Adds a listener for the algorithm toggle button.
     */
    public void addAlgorithmToggleListener(ActionListener listener) {
        toggleAlgorithmButton.addActionListener(listener);
    }

    /**
     * Adds a listener for the new game button.
     */
    public void addNewGameListener(ActionListener listener) {
        newGameButton.addActionListener(listener);
    }
    
    /**
     * Adds a listener for the back to main menu button.
     */
    public void addBackToMenuListener(ActionListener listener) {
        backToMenuButton.addActionListener(listener);
    }    
    
    /**
     * Helper method to check if a line (row, column, or diagonal) is a winning line.
     */
    private boolean isWinningLine(char[] line, char player) {
        for (char cell : line) {
            if (cell != player) {
                return false;
            }
        }
        return true;
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
                    boardButtons[i][j].setBackground(player == HUMAN ? Color.BLUE : Color.RED); // Yellow for player, Red for AI
                }
            }

            // Check columns for a win
            char[] column = new char[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                column[j] = board[j][i];
            }
            if (isWinningLine(column, player)) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    boardButtons[j][i].setBackground(player == HUMAN ? Color.BLUE : Color.RED);
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
                boardButtons[i][i].setBackground(player == HUMAN ? Color.BLUE : Color.RED);
            }
        }
        if (isWinningLine(antiDiagonal, player)) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                boardButtons[i][BOARD_SIZE - i - 1].setBackground(player == HUMAN ? Color.BLUE : Color.RED);
            }
        }
    }


    /**
     * Disables the entire board (no more clicks allowed).
     */
    public void disableBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boardButtons[i][j].setEnabled(false);
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
        return (JFrame) c;
    }
    
    public int getLastMoveRow() {
        return lastMoveRow;
    }

    public int getLastMoveCol() {
        return lastMoveCol;
    }

    public void setLastMove(int row, int col) {
        this.lastMoveRow = row;
        this.lastMoveCol = col;
    }
}

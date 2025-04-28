package controllers;

import services.TicTacToeAI;
import services.TicTacToeService;
import views.TicTacToeView;

import javax.swing.*;
import java.awt.*;

public class TicTacToeController {

    private final TicTacToeView view;
    private final TicTacToeService service;
    private final TicTacToeAI ai;
    private char[][] board;
    private boolean gameOver;
    private boolean useAlphaBeta;
    private String playerName;
    private static final int BOARD_SIZE = 5;
    private static final char EMPTY = ' ';
    private static final char HUMAN = 'X';
    private static final char AI = 'O';
    private JFrame frame;

    public TicTacToeController(TicTacToeView view, TicTacToeService service) {
        this.view = view;
        this.service = service;
        this.ai = new TicTacToeAI();
        initListeners();
    }

    public void showView() {
        if (frame == null) {
            frame = new JFrame("TicTacToe Game");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.add(view);
            frame.setPreferredSize(new Dimension(800, 600));
            frame.pack();
            frame.setLocationRelativeTo(null);
        }
        frame.setVisible(true);
        initializeGame();
    }

    public void hideView() {
        if (frame != null) {
            frame.setVisible(false);
            frame.dispose();
            frame = null;
        }
    }

    private void initializeGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        gameOver = false;
        useAlphaBeta = false;
        playerName = JOptionPane.showInputDialog(
                view.getFrame(),
                "Enter your name:",
                "Player Name",
                JOptionPane.QUESTION_MESSAGE
        );
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player";
        }
        view.updateBoard(board);
        view.setStatusLabel("Your turn (" + playerName + ")");
        view.setAlgorithmLabel("AI Algorithm: " + (useAlphaBeta ? "Alpha-Beta" : "Minimax"));
    }

    private void initListeners() {
        view.addBoardButtonListener((row, col) -> {
            if (!gameOver && board[row][col] == EMPTY) {
                makeMove(row, col, HUMAN);
                view.setLastMove(row, col); // Track the last move

                // Only check and save when player wins
                if (ai.checkWin(board, HUMAN)) {
                    gameOver = true;
                    view.setStatusLabel("You win!");
                    highlightWinningCells(HUMAN);
                    service.saveResult(
                        playerName,
                        row,
                        col,
                        useAlphaBeta ? "AlphaBeta" : "Minimax",
                        0,
                        true
                    );
                    saveResultToDatabase(playerName);
                    disableBoard();
                    displayGameOver(playerName);
                } else if (ai.isBoardFull(board)) {
                    gameOver = true;
                    view.setStatusLabel("It's a draw!");
                    // No saveResult for draw (only wins are stored)
                    saveResultToDatabase("Draw");
                    disableBoard();
                    displayGameOver("Draw");
                } else if (!gameOver) {
                    view.setStatusLabel("Computer is thinking...");
                    SwingUtilities.invokeLater(this::makeComputerMove);
                }
            }
        });

        view.addAlgorithmToggleListener(e -> {
            useAlphaBeta = !useAlphaBeta;
            view.setAlgorithmLabel("AI Algorithm: " + (useAlphaBeta ? "Alpha-Beta" : "Minimax"));
        });

        view.addNewGameListener(e -> initializeGame());

        view.addBackToMenuListener(e -> {
            hideView();
            JOptionPane.showMessageDialog(view.getFrame(), "Returning to Main Menu");
        });
    }

    private void makeMove(int row, int col, char player) {
        board[row][col] = player;
        view.updateBoard(board);
    }

    private void makeComputerMove() {
        if (gameOver) return;

        int[] move;
        long startTime = System.currentTimeMillis();
        String algorithm = useAlphaBeta ? "AlphaBeta" : "Minimax";
        if (useAlphaBeta) {
            move = ai.getBestMoveWithAlphaBeta(board);
        } else {
            move = ai.getBestMoveWithMinimax(board);
        }
        long timeTaken = System.currentTimeMillis() - startTime;

        if (move[0] != -1 && move[1] != -1) {
            makeMove(move[0], move[1], AI);
            view.setLastMove(move[0], move[1]); // Track computer's move

            if (ai.checkWin(board, AI)) {
                gameOver = true;
                view.setStatusLabel("Computer wins!");
                highlightWinningCells(AI);
                // Only saveResult for computer win
                service.saveResult(
                    "Computer",
                    move[0],
                    move[1],
                    algorithm,
                    timeTaken,
                    true
                );
                saveResultToDatabase("Computer");
                disableBoard();
                displayGameOver("Computer");
            } else if (ai.isBoardFull(board)) {
                gameOver = true;
                view.setStatusLabel("It's a draw!");
                // No saveResult for draw (only wins are stored)
                saveResultToDatabase("Draw");
                disableBoard();
                displayGameOver("Draw");
            } else {
                view.setStatusLabel("Your turn (" + playerName + ")");
            }
        }
    }

    private void displayGameOver(String winner) {
        if ("Computer".equals(winner)) {
            System.out.println("Computer wins!");
        } else if ("Draw".equals(winner)) {
            System.out.println("It's a draw!");
        } else {
            System.out.println(winner + " wins!");
        }
    }

    private void highlightWinningCells(char player) {
        view.highlightWinningCells(player, board);
    }

    private void saveResultToDatabase(String winner) {
        service.saveFinalResult(playerName, winner);
    }

    private void disableBoard() {
        gameOver = true;
        view.disableBoard();
    }
}

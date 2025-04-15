package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import models.entities.TowerOfHanoiResult;
import models.exceptions.DatabaseException;
import services.TowerOfHanoiService;
import services.TowerOfHanoiService.Move;
import utils.DatabaseConnection;
import utils.dsa.LinkedList;
import views.TowerOfHanoiView;

/**
 * Controller for the Tower of Hanoi game.
 * Connects the view (UI) with the service (logic) and handles user interactions.
 */
public class TowerOfHanoiController {
    private TowerOfHanoiService service;
    private TowerOfHanoiView view;
    private DatabaseConnection dbConnection;
    private LinkedList<GameSession> gameHistory;
    private int currentDisks;

    /**
     * Constructor initializes the game, database, view, and event listeners.
     */
    public TowerOfHanoiController(TowerOfHanoiService service, TowerOfHanoiView view) {
        this.service = service;
        this.view = view;
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (DatabaseException e) {
            JOptionPane.showMessageDialog(null, "Database connection error: " + e.getMessage());
        }
        this.gameHistory = new LinkedList<>();
        initGame(); // Generate initial disk count
        setupEventListeners(); // Hook up buttons to actions
    }

    /**
     * Inner class to represent a single game session's result.
     */
    private static class GameSession {
        private String playerName;
        private int disks;
        private int moves;
        private Date timestamp;

        public GameSession(String playerName, int disks, int moves) {
            this.playerName = playerName;
            this.disks = disks;
            this.moves = moves;
            this.timestamp = new Date();
        }

        @Override
        public String toString() {
            return "Player: " + playerName + ", Disks: " + disks + ", Moves: " + moves;
        }
    }

    /**
     * Initializes a new game with random disk count.
     */
    private void initGame() {
        currentDisks = service.generateRandomDisks(); // Get random number of disks
        view.updateDiskCount(currentDisks);           // Show it on the UI
        view.resetInputs();                           // Clear form fields
    }

    /**
     * Attaches listeners to view buttons.
     */
    private void setupEventListeners() {
        view.addSubmitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmit(); // Logic for "Submit" button
            }
        });

        view.addSolveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSolve(); // Logic for "Solve" button
            }
        });

        view.addResetListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                initGame(); // Reset to a new game
            }
        });
    }

    /**
     * Calculates minimum number of moves using Frame-Stewart algorithm (for 4 pegs).
     */
    private int calculateMinMovesFrameStewart(int n) {
        if (n <= 0) return 0;
        if (n == 1) return 1;
        if (n == 2) return 3;
        if (n == 3) return 5;
        if (n == 4) return 9;
        if (n == 5) return 13;
        if (n == 6) return 17;
        if (n == 7) return 25;
        if (n == 8) return 33;
        if (n == 9) return 41;
        if (n == 10) return 49;

        int result = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            int moves = 2 * calculateMinMovesFrameStewart(n - i) + (1 << i) - 1;
            result = Math.min(result, moves);
        }
        return result;
    }

    /**
     * Handles logic when player submits a move sequence.
     */
    private void handleSubmit() {
        try {
            // Get inputs from the UI
            String playerName = view.getPlayerName();
            String moveCountText = view.getMoveCountText();
            String moveSequenceText = view.getMoveSequenceText();

            // Validate inputs
            if (playerName == null || playerName.trim().isEmpty())
                throw new IllegalArgumentException("Player name cannot be empty");

            if (moveCountText == null || moveCountText.trim().isEmpty())
                throw new IllegalArgumentException("Number of moves cannot be empty");

            if (moveSequenceText == null || moveSequenceText.trim().isEmpty())
                throw new IllegalArgumentException("Move sequence cannot be empty");

            int moveCount = Integer.parseInt(moveCountText);
            List<Move> moves = parseMoveSequence(moveSequenceText);

            // Choose algorithm for move validation
            int minMoves = (view.getSelectedAlgorithmIndex() == 2)
                    ? calculateMinMovesFrameStewart(currentDisks)
                    : service.calculateMinMoves(currentDisks);

            // Match declared and calculated moves
            if (moveCount != minMoves) {
                view.showResult(false, "Incorrect! The minimum number of moves required is " + minMoves);
                return;
            }

            if (moves.size() != moveCount) {
                view.showResult(false, "Move count doesn't match the sequence length!");
                return;
            }

            // If moves are valid
            if (service.validateMoveSequence(moves, currentDisks)) {
                // Measure performance of solving algorithms
                long startTime, endTime;

                startTime = System.nanoTime();
                List<Move> recursiveSolution = service.solveRecursive(currentDisks, 'A', 'B', 'C');
                endTime = System.nanoTime();
                long recursiveTime = (endTime - startTime) / 1000000;

                startTime = System.nanoTime();
                List<Move> iterativeSolution = service.solveIterative(currentDisks, 'A', 'B', 'C');
                endTime = System.nanoTime();
                long iterativeTime = (endTime - startTime) / 1000000;

                startTime = System.nanoTime();
                List<Move> frameStewartSolution = service.solveFrameStewart(currentDisks, 'A', 'B', 'C', 'D');
                endTime = System.nanoTime();
                long frameStewartTime = (endTime - startTime) / 1000000;

                // Record game session
                gameHistory.add(new GameSession(playerName, currentDisks, moveCount));

                // Create result object for DB
                TowerOfHanoiResult result = new TowerOfHanoiResult();
                result.setPlayerName(playerName);
                result.setNumberOfDisks(currentDisks);
                result.setNumberOfMoves(moveCount);
                result.setMoveSequence(moves.stream().map(Move::toString).collect(Collectors.toList()));
                result.setRecursiveTime(recursiveTime);
                result.setIterativeTime(iterativeTime);
                result.setFrameStewartTime(frameStewartTime);
                result.setTimestamp(new Date());

                try {
                    dbConnection.save(result); // Save to DB

                    // Show result and animation
                    String message = "Correct! Solution saved. Algorithm times:\n" +
                            "Recursive: " + recursiveTime + "ms\n" +
                            "Iterative: " + iterativeTime + "ms\n" +
                            "Frame-Stewart: " + frameStewartTime + "ms";
                    view.showResult(true, message);
                    view.getPegPanel().reset();
                    view.getPegPanel().animateMoves(moves.stream().map(Move::toString).collect(Collectors.toList()));


                } catch (Exception ex) {
                    view.showResult(false, "Database error: " + ex.getMessage());
                }
            } else {
                view.showResult(false, "Incorrect! Your move sequence doesn't solve the puzzle.");
            }

        } catch (NumberFormatException ex) {
            view.showResult(false, "Please enter a valid number of moves");
        } catch (IllegalArgumentException ex) {
            view.showResult(false, ex.getMessage());
        } catch (Exception ex) {
            view.showResult(false, "An error occurred: " + ex.getMessage());
        }
    }

    /**
     * Auto-generates and animates a valid solution.
     */
    private void handleSolve() {
        try {
            int algorithmIndex = view.getSelectedAlgorithmIndex();
            List<Move> moves;

            if (algorithmIndex == 0) {
                moves = service.solveRecursive(currentDisks, 'A', 'B', 'C');
            } else if (algorithmIndex == 1) {
                moves = service.solveIterative(currentDisks, 'A', 'B', 'C');
            } else {
                moves = service.solveFrameStewart(currentDisks, 'A', 'B', 'C', 'D');
            }

            view.displayMoveSequence(moves);
            view.getMoveCountField().setText(String.valueOf(moves.size()));
            view.getPegPanel().reset();
            view.getPegPanel().animateMoves(moves.stream().map(Move::toString).collect(Collectors.toList()));
        } catch (Exception ex) {
            view.showResult(false, "Error generating solution: " + ex.getMessage());
        }
    }

    /**
     * Parses the move sequence string entered by user into a list of Move objects.
     */
    private List<Move> parseMoveSequence(String moveSequenceText) {
        List<Move> moves = new ArrayList<>();
        int pegCount = view.getPegPanel().getNumberOfPegs();
        char maxPeg = (char) ('A' + pegCount - 1);

        String[] lines = moveSequenceText.split("\\r?\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            String[] parts = line.contains("->") ? line.split("->") :
                             line.contains("-") ? line.split("-") : line.split("\\s+");

            if (parts.length != 2)
                throw new IllegalArgumentException("Invalid move format: " + line);

            char source = parts[0].trim().toUpperCase().charAt(0);
            char dest = parts[1].trim().toUpperCase().charAt(0);

            if (source < 'A' || source > maxPeg || dest < 'A' || dest > maxPeg)
                throw new IllegalArgumentException("Invalid peg: " + line);

            if (source == dest)
                throw new IllegalArgumentException("Source and destination cannot be the same: " + line);

            moves.add(new Move(source, dest));
        }
        return moves;
    }

    /**
     * Displays the game history in a dialog.
     */
    public void showGameHistory() {
        StringBuilder sb = new StringBuilder("Game History:\n");
        for (int i = 0; i < gameHistory.size(); i++) {
            sb.append(i + 1).append(". ").append(gameHistory.get(i).toString()).append("\n");
        }
        JOptionPane.showMessageDialog(view, sb.toString(), "Game History", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Makes the game view visible.
     */
    public void showView() {
        view.setVisible(true);
    }
}

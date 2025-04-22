package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dev.morphia.Datastore;
import dev.morphia.query.Query;
import models.entities.TowerOfHanoiResult;
import models.exceptions.DatabaseException;
import utils.DatabaseConnection;
import utils.dsa.hanoi.FrameStewartSolver;
import utils.dsa.hanoi.HanoiSolver;
import utils.dsa.hanoi.IterativeSolver;
import utils.dsa.hanoi.RecursiveSolver;
import utils.dsa.LinkedList;

public class TowerOfHanoiService {
    private DatabaseConnection dbConnection;
    private final HanoiSolver recursiveSolver;
    private final HanoiSolver iterativeSolver;
    private final HanoiSolver frameStewartSolver;
    private int currentDiskCount;
    private int currentPegCount = 3; // Default to 3 pegs
    private List<String> optimalMoves3Peg;
    private List<String> optimalMoves4Peg;
    private TowerOfHanoiResult lastResult;
    private boolean useRecursiveFor3Peg = true;
    private boolean current3PegIsRecursive;

    public TowerOfHanoiService() {
        // Initialize final fields exactly once
        this.recursiveSolver = new RecursiveSolver();
        this.iterativeSolver = new IterativeSolver();
        this.frameStewartSolver = new FrameStewartSolver();
        
        // Only database connection in try-catch
        try {
            this.dbConnection = DatabaseConnection.getInstance();
        } catch (DatabaseException e) {
            System.err.println("Database connection error: " + e.getMessage());
            this.dbConnection = null; 
        }
        
        // Continue with other initialization
        generateNewPuzzle();
    }

    /**
     * Generate a new Tower of Hanoi puzzle with random disk count (5-10)
     * Calculates solutions for both 3-peg and 4-peg versions
     */
    public void generateNewPuzzle() {
        Random random = new Random();
        currentDiskCount = random.nextInt(6) + 5; // Random between 5-10
        
        // Generate 3-peg solutions with both algorithms
        optimalMoves3Peg = new ArrayList<>();
        current3PegIsRecursive = useRecursiveFor3Peg;

        if (current3PegIsRecursive) {
            recursiveSolver.solve(currentDiskCount, 'A', 'C', 'B', optimalMoves3Peg);
        } else {
            iterativeSolver.solve(currentDiskCount, 'A', 'C', 'B', optimalMoves3Peg);
        }
        useRecursiveFor3Peg = !useRecursiveFor3Peg;
        
        // Generate 4-peg solution with Frame-Stewart algorithm
        optimalMoves4Peg = new ArrayList<>();
        frameStewartSolver.solve(currentDiskCount, 'A', 'D', 'B', 'C', optimalMoves4Peg);
        
    }
    
    public String getCurrent3PegAlgorithm() {
        return current3PegIsRecursive ? "Recursive" : "Iterative";
    }

    /**
     * Get the current disk count for this puzzle
     */
    public int getCurrentDiskCount() {
        return currentDiskCount;
    }

    /**
     * Get the optimal move count for a given peg count
     */
    public int getOptimalMoveCount(int pegCount) {
        if (pegCount == 4) {
            return optimalMoves4Peg.size();
        } else {
            return (int) Math.pow(2, currentDiskCount) - 1; // Standard formula for 3 pegs
        }
    }

    /**
     * Get the optimal move count using current peg count
     */
    public int getOptimalMoveCount() {
        return getOptimalMoveCount(currentPegCount);
    }

    /**
     * Get optimal moves list for a specific peg count
     */
    public List<String> getOptimalMoves(int pegCount) {
        return pegCount == 4 ? optimalMoves4Peg : optimalMoves3Peg;
    }

    /**
     * Get optimal moves list using current peg count
     */
    public List<String> getOptimalMoves() {
        return getOptimalMoves(currentPegCount);
    }

    /**
     * Set the current peg count (3 or 4)
     */
    public void setPegCount(int pegCount) {
        this.currentPegCount = (pegCount == 4) ? 4 : 3;
    }

    /**
     * Get the current peg count
     */
    public int getPegCount() {
        return currentPegCount;
    }

    /**
     * Validate user moves against correct solution for a specific peg count
     */
    public boolean validateMoves(List<String> userMoves, int pegCount) {
 

        // Simulation of Tower of Hanoi with user moves
        char[][] pegs = new char[pegCount][currentDiskCount];
        int[] topIndex = new int[pegCount]; // Top disk index for each peg
        
     // Initialize all pegs as empty except first peg
        topIndex[0] = currentDiskCount - 1;
        for (int i = 1; i < pegCount; i++) {
            topIndex[i] = -1;
        }
       

        // Initialize first peg with all disks
        for (int i = 0; i < currentDiskCount; i++) {
            pegs[0][i] = (char)('A' + (currentDiskCount - 1 - i));
        }
        

        // Process each move
        for (String move : userMoves) {
            if (move.length() != 3 || move.charAt(1) != '-' || 
                move.charAt(0) < 'A' || move.charAt(0) >= ('A' + pegCount) || 
                move.charAt(2) < 'A' || move.charAt(2) >= ('A' + pegCount)) {
                return false; // Invalid format or peg reference
            }

            int fromPeg = move.charAt(0) - 'A';
            int toPeg = move.charAt(2) - 'A';

            if (fromPeg == toPeg || topIndex[fromPeg] == -1) {
                return false; // Invalid move
            }

            char disk = pegs[fromPeg][topIndex[fromPeg]];
            // Check disk size rule
            if (topIndex[toPeg] != -1 && pegs[toPeg][topIndex[toPeg]] < disk) {
                return false;
            }

            // Move the disk
            pegs[fromPeg][topIndex[fromPeg]] = 0;
            topIndex[fromPeg]--;
            topIndex[toPeg]++;
            pegs[toPeg][topIndex[toPeg]] = disk;
        }

        // Check final state (all disks on destination peg)
        int destinationPeg = (pegCount == 4) ? 3 : 2; // D for 4 pegs, C for 3
        return topIndex[destinationPeg] == currentDiskCount - 1;
    }

    /**
     * Original validateMoves method for backward compatibility
     */
    public boolean validateMoves(List<String> userMoves) {
        return validateMoves(userMoves, currentPegCount);
    }

    /**
     * Check a user's answer for correctness with specified peg count
     */
    public boolean checkAnswer(String playerName, int moveCount, String moveSequence, int pegCount) {
        // Parse move sequence
        String[] moves = moveSequence.split(",");
        List<String> userMoves = new ArrayList<>();
        for (String move : moves) {
            userMoves.add(move.replaceAll("\\s+", "").trim());
        }

        int expectedMoveCount = getOptimalMoveCount(pegCount);
        boolean isCorrect = validateMoves(userMoves, pegCount) && moveCount == expectedMoveCount;
        boolean isValid = validateMoves(userMoves, pegCount);
        boolean correctMoveCount = moveCount == userMoves.size();
        boolean isOptimal = isValid && (moveCount == getOptimalMoveCount(pegCount));

        // Time algorithms
        long recursiveTime = 0, iterativeTime = 0, frameStewartTime = 0;
        long startTime;
        
        // For 3-peg version, time both recursive and iterative algorithms
        if (pegCount == 3) {
            List<String> recursiveMoves = new ArrayList<>();
            startTime = System.nanoTime();
            recursiveSolver.solve(currentDiskCount, 'A', 'C', 'B', recursiveMoves);
            recursiveTime = System.nanoTime() - startTime;
            
            List<String> iterativeMoves = new ArrayList<>();
            startTime = System.nanoTime();
            iterativeSolver.solve(currentDiskCount, 'A', 'C', 'B', iterativeMoves);
            iterativeTime = System.nanoTime() - startTime;
        }
        
        // Always time Frame-Stewart for comparison
        List<String> frameStewartMoves = new ArrayList<>();
        startTime = System.nanoTime();
        frameStewartSolver.solve(currentDiskCount, 'A', 'D', 'B', 'C', frameStewartMoves);
        frameStewartTime = System.nanoTime() - startTime;

        // Save to database if correct
        if (isValid && dbConnection != null) {
            TowerOfHanoiResult result = new TowerOfHanoiResult(
                playerName, currentDiskCount, moveSequence, moveCount,
                recursiveTime, iterativeTime, frameStewartTime, pegCount, true, isOptimal
            );
            
            try {
            	this.lastResult = result;
                dbConnection.save(result);
            } catch (DatabaseException e) {
                System.err.println("Failed to save result: " + e.getMessage());
            }
        }

        return isValid && moveCount == userMoves.size();
    }

    /**
     * Original checkAnswer method for backward compatibility
     */
    public boolean checkAnswer(String playerName, int moveCount, String moveSequence) {
        return checkAnswer(playerName, moveCount, moveSequence, currentPegCount);
    }
    
    /*
     * Get time taken for each algorithm
     */

    public TowerOfHanoiResult getLastResult() {
        return lastResult;
    }
    
    public void setLastResult(TowerOfHanoiResult result) {
        this.lastResult = result;
    }


    /**
     * Get all saved results from the database
     */
    public List<TowerOfHanoiResult> getAllResults() {
        if (dbConnection == null) {
            return new ArrayList<>();
        }
        
        try {
            Datastore datastore = dbConnection.getDatastore();
            Query<TowerOfHanoiResult> query = datastore.find(TowerOfHanoiResult.class);
            return query.iterator().toList();
        } catch (Exception e) {
            System.err.println("Error retrieving results: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * Get algorithm timing data for analysis and reporting
     * @param diskCount Specific disk count to filter results (0 for all)
     * @param pegCount Specific peg count to filter results (0 for all)
     * @return List of timing results
     */
    public List<TowerOfHanoiResult> getTimingResults(int diskCount, int pegCount) {
        if (dbConnection == null) {
            return new ArrayList<>();
        }
        
        try {
            Datastore datastore = dbConnection.getDatastore();
            Query<TowerOfHanoiResult> query = datastore.find(TowerOfHanoiResult.class);
            
            if (diskCount > 0) {
                query = query.field("diskCount").equal(diskCount);
            }
            
            if (pegCount > 0) {
                query = query.field("pegCount").equal(pegCount);
            }
            
            return query.iterator().toList();
        } catch (Exception e) {
            System.err.println("Error retrieving timing results: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

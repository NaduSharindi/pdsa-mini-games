package services;

import java.util.List;
import java.util.Random;

import utils.dsa.Stack;
import utils.dsa.hanoi.RecursiveSolver;
import utils.dsa.hanoi.IterativeSolver;
import utils.dsa.hanoi.FrameStewartSolver;

public class TowerOfHanoiService {
    // Constants defining the min and max number of disks
    private static final int MIN_DISKS = 5;
    private static final int MAX_DISKS = 10;
    
    private RecursiveSolver recursiveSolver;
    private IterativeSolver iterativeSolver;
    private FrameStewartSolver frameStewartSolver;
    
    public TowerOfHanoiService() {
        recursiveSolver = new RecursiveSolver();
        iterativeSolver = new IterativeSolver();
        frameStewartSolver = new FrameStewartSolver();
    }

    /**
     * Generates a random number of disks between MIN_DISKS and MAX_DISKS (inclusive)
     */
    public int generateRandomDisks() {
        Random random = new Random();
        return random.nextInt(MAX_DISKS - MIN_DISKS + 1) + MIN_DISKS;
    }

    /**
     * Calculates the minimum number of moves required to solve the Tower of Hanoi with `n` disks.
     * Formula: 2^n - 1
     */
    public int calculateMinMoves(int numberOfDisks) {
        return recursiveSolver.calculateMinMoves(numberOfDisks);
    }

    /**
     * Recursive solution for the classic 3-peg Tower of Hanoi.
     */
    public List<Move> solveRecursive(int n, char source, char auxiliary, char destination) {
        return recursiveSolver.solve(n, source, auxiliary, destination);
    }

    /**
     * Iterative solution using custom Stack data structure.
     */
    public List<Move> solveIterative(int n, char source, char auxiliary, char destination) {
        return iterativeSolver.solve(n, source, auxiliary, destination);
    }

    /**
     * Frame-Stewart algorithm to solve Tower of Hanoi with 4 pegs (advanced).
     */
    public List<Move> solveFrameStewart(int n, char source, char aux1, char aux2, char destination) {
        return frameStewartSolver.solve(n, source, aux1, aux2, destination);
    }

    /**
     * Validates a sequence of moves.
     * Ensures no illegal moves and that all disks end up on destination peg.
     */
    public boolean validateMoveSequence(List<Move> moves, int diskCount, int algorithmIndex) {
        // Initialize pegs
        Stack<Integer>[] pegs = new Stack[4];
        for (int i = 0; i < 4; i++) {
            pegs[i] = new Stack<>();
        }
        
        // Fill source peg
        for (int i = diskCount; i > 0; i--) {
            pegs[0].push(i);
        }
        
        // Execute moves and validate each one
        for (Move move : moves) {
            int srcIndex = move.getSource() - 'A';
            int dstIndex = move.getDestination() - 'A';
            
            if (pegs[srcIndex].isEmpty()) return false;
            
            int disk = pegs[srcIndex].peek();
            if (!pegs[dstIndex].isEmpty() && pegs[srcIndex].peek() > pegs[dstIndex].peek()) {
                return false;
            }
            
            pegs[dstIndex].push(pegs[srcIndex].pop());
        }
        
        // Determine the destination peg based on algorithm
        int destPegIndex = (algorithmIndex == 2) ? 3 : 2; // Use peg D for Frame-Stewart, peg C otherwise
        
        // Valid if all disks end up on the correct destination peg
        return pegs[0].isEmpty() && pegs[1].isEmpty() && 
               (algorithmIndex != 2 ? pegs[2].size() == diskCount && pegs[3].isEmpty() : 
                                      pegs[2].isEmpty() && pegs[3].size() == diskCount);
    }
    
    
    
    // Getter methods for solvers
    public RecursiveSolver getRecursiveSolver() {
        return recursiveSolver;
    }
    
    public IterativeSolver getIterativeSolver() {
        return iterativeSolver;
    }
    
    public FrameStewartSolver getFrameStewartSolver() {
        return frameStewartSolver;
    }

    /**
     * Inner class to represent a single move from one peg to another.
     */
    public static class Move {
        private char source;
        private char destination;
        
        public Move(char source, char destination) {
            this.source = source;
            this.destination = destination;
        }
        
        public char getSource() {
            return source;
        }
        
        public char getDestination() {
            return destination;
        }
        
        @Override
        public String toString() {
            return source + "->" + destination;
        }
    }
}

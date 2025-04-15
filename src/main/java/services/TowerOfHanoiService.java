package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import utils.dsa.Stack;

public class TowerOfHanoiService {
    // Constants
    private static final int MIN_DISKS = 5;
    private static final int MAX_DISKS = 10;
    
    /**
     * Generate random number of disks between 5 and 10
     */
    public int generateRandomDisks() {
        Random random = new Random();
        return random.nextInt(MAX_DISKS - MIN_DISKS + 1) + MIN_DISKS;
    }
    
    /**
     * Calculate minimum moves required: 2^n - 1
     */
    public int calculateMinMoves(int numberOfDisks) {
        return (int)Math.pow(2, numberOfDisks) - 1;
    }
    
    /**
     * Recursive solution for Tower of Hanoi
     * @return List of moves in sequence
     */
    public List<Move> solveRecursive(int n, char source, char auxiliary, char destination) {
        List<Move> moves = new ArrayList<>();
        solveRecursiveHelper(n, source, auxiliary, destination, moves);
        return moves;
    }
    
    private void solveRecursiveHelper(int n, char source, char auxiliary, char destination, List<Move> moves) {
        if (n == 1) {
            moves.add(new Move(source, destination));
            return;
        }
        
        solveRecursiveHelper(n-1, source, destination, auxiliary, moves);
        moves.add(new Move(source, destination));
        solveRecursiveHelper(n-1, auxiliary, source, destination, moves);
    }
    
    /**
     * Iterative solution using custom Stack data structure
     * @return List of moves in sequence
     */
    public List<Move> solveIterative(int n, char source, char auxiliary, char destination) {
        List<Move> moves = new ArrayList<>();
        
        // For even number of disks, swap auxiliary and destination
        if (n % 2 == 0) {
            char temp = destination;
            destination = auxiliary;
            auxiliary = temp;
        }
        
        // Initialize three stacks for the pegs
        Stack<Integer>[] pegs = new Stack[3];
        for (int i = 0; i < 3; i++) {
            pegs[i] = new Stack<>();
        }
        
        // Initialize source peg with disks
        for (int i = n; i > 0; i--) {
            pegs[0].push(i);
        }
        
        int totalMoves = calculateMinMoves(n);
        
        for (int move = 1; move <= totalMoves; move++) {
            int src, dst;
            
            if (n % 2 == 1) {
                // For odd number of disks
                if (move % 3 == 1) {
                    src = 0; dst = 2; // source to destination
                } else if (move % 3 == 2) {
                    src = 0; dst = 1; // source to auxiliary
                } else {
                    src = 1; dst = 2; // auxiliary to destination
                }
            } else {
                // For even number of disks
                if (move % 3 == 1) {
                    src = 0; dst = 1; // source to auxiliary
                } else if (move % 3 == 2) {
                    src = 0; dst = 2; // source to destination
                } else {
                    src = 1; dst = 2; // auxiliary to destination
                }
            }
            
            // Legal move: either peg src is empty, or top disk of peg src is smaller than that of peg dst
            if (pegs[src].isEmpty()) {
                // Move disk from dst to src
                int disk = pegs[dst].pop();
                pegs[src].push(disk);
                moves.add(new Move(getCharFromIndex(dst), getCharFromIndex(src)));
            } else if (pegs[dst].isEmpty() || pegs[src].peek() < pegs[dst].peek()) {
                // Move disk from src to dst
                int disk = pegs[src].pop();
                pegs[dst].push(disk);
                moves.add(new Move(getCharFromIndex(src), getCharFromIndex(dst)));
            } else {
                // Move disk from dst to src
                int disk = pegs[dst].pop();
                pegs[src].push(disk);
                moves.add(new Move(getCharFromIndex(dst), getCharFromIndex(src)));
            }
        }
        
        return moves;
    }
    
    private char getCharFromIndex(int index) {
        return (char)('A' + index);
    }
    
    /**
     * Frame-Stewart algorithm for 4 pegs
     */
    public List<Move> solveFrameStewart(int n, char source, char aux1, char aux2, char destination) {
        List<Move> moves = new ArrayList<>();
        
        // Calculate optimal k value (approximate solution)
        int k = (int)Math.sqrt(2 * n);
        
        if (n == 0) return moves;
        if (n == 1) {
            moves.add(new Move(source, destination));
            return moves;
        }
        
        // Move top k disks from source to aux1 using all 4 pegs
        moves.addAll(solveFrameStewart(n-k, source, aux2, destination, aux1));
        
        // Move remaining disks from source to destination using 3 pegs
        moves.addAll(solveRecursive(k, source, aux2, destination));
        
        // Move the (n-k) disks from aux1 to destination using all 4 pegs
        moves.addAll(solveFrameStewart(n-k, aux1, source, aux2, destination));
        
        return moves;
    }
    
    /**
     * Validate a sequence of moves using custom Stack implementation
     * @return true if the sequence correctly solves the puzzle
     */
    public boolean validateMoveSequence(List<Move> moves, int diskCount) {
        // Initialize the three pegs using custom Stack
        Stack<Integer>[] pegs = new Stack[3];
        for (int i = 0; i < 3; i++) {
            pegs[i] = new Stack<>();
        }
        
        // Initialize source peg with disks
        for (int i = diskCount; i > 0; i--) {
            pegs[0].push(i);
        }
        
        // Apply each move and validate
        for (Move move : moves) {
            int srcIndex = move.getSource() - 'A';
            int dstIndex = move.getDestination() - 'A';
            
            // Validate source peg has disks
            if (pegs[srcIndex].isEmpty()) {
                return false;
            }
            
            // Get the top disk from source
            int disk = pegs[srcIndex].peek();
            
         // Inside validateMoveSequence method
         // Validate destination can accept this disk
         if (!pegs[dstIndex].isEmpty() && pegs[srcIndex].peek() > pegs[dstIndex].peek()) {
             return false; // Cannot place larger disk on smaller disk
         }
            
            // Move the disk
            pegs[dstIndex].push(pegs[srcIndex].pop());
        }
        
        // Check if all disks are on the destination peg
        return pegs[0].isEmpty() && pegs[1].isEmpty() && pegs[2].size() == diskCount;
    }
    
    /**
     * Move class to represent a single move
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

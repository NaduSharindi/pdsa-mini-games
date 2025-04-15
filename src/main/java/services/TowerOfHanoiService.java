package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import utils.dsa.Stack; 

public class TowerOfHanoiService {

    // Constants defining the min and max number of disks
    private static final int MIN_DISKS = 5;
    private static final int MAX_DISKS = 10;

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
        return (int) Math.pow(2, numberOfDisks) - 1;
    }

    /**
     * Recursive solution for the classic 3-peg Tower of Hanoi.
     * @param n Number of disks
     * @param source Source peg label (e.g., 'A')
     * @param auxiliary Auxiliary peg label (e.g., 'B')
     * @param destination Destination peg label (e.g., 'C')
     * @return List of moves to solve the puzzle
     */
    public List<Move> solveRecursive(int n, char source, char auxiliary, char destination) {
        List<Move> moves = new ArrayList<>();
        solveRecursiveHelper(n, source, auxiliary, destination, moves);
        return moves;
    }

    // Helper function for recursive solution
    private void solveRecursiveHelper(int n, char source, char auxiliary, char destination, List<Move> moves) {
        if (n == 1) {
            moves.add(new Move(source, destination));
            return;
        }
        solveRecursiveHelper(n - 1, source, destination, auxiliary, moves);
        moves.add(new Move(source, destination));
        solveRecursiveHelper(n - 1, auxiliary, source, destination, moves);
    }

    /**
     * Iterative solution using custom Stack data structure.
     * @param n Number of disks
     * @param source Source peg label
     * @param auxiliary Auxiliary peg label
     * @param destination Destination peg label
     * @return List of moves to solve the puzzle
     */
    public List<Move> solveIterative(int n, char source, char auxiliary, char destination) {
        List<Move> moves = new ArrayList<>();

        // Swap auxiliary and destination for even number of disks
        if (n % 2 == 0) {
            char temp = destination;
            destination = auxiliary;
            auxiliary = temp;
        }

        // Initialize pegs
        Stack<Integer>[] pegs = new Stack[3];
        for (int i = 0; i < 3; i++) {
            pegs[i] = new Stack<>();
        }

        // Fill source peg with disks (largest disk at bottom)
        for (int i = n; i > 0; i--) {
            pegs[0].push(i);
        }

        int totalMoves = calculateMinMoves(n);

        // Perform the moves
        for (int move = 1; move <= totalMoves; move++) {
            int src, dst;

            // Determine which pegs to move between
            if (n % 2 == 1) { // Odd number of disks
                if (move % 3 == 1) {
                    src = 0; dst = 2;
                } else if (move % 3 == 2) {
                    src = 0; dst = 1;
                } else {
                    src = 1; dst = 2;
                }
            } else { // Even number of disks
                if (move % 3 == 1) {
                    src = 0; dst = 1;
                } else if (move % 3 == 2) {
                    src = 0; dst = 2;
                } else {
                    src = 1; dst = 2;
                }
            }

            // Execute the legal move
            if (pegs[src].isEmpty()) {
                int disk = pegs[dst].pop();
                pegs[src].push(disk);
                moves.add(new Move(getCharFromIndex(dst), getCharFromIndex(src)));
            } else if (pegs[dst].isEmpty() || pegs[src].peek() < pegs[dst].peek()) {
                int disk = pegs[src].pop();
                pegs[dst].push(disk);
                moves.add(new Move(getCharFromIndex(src), getCharFromIndex(dst)));
            } else {
                int disk = pegs[dst].pop();
                pegs[src].push(disk);
                moves.add(new Move(getCharFromIndex(dst), getCharFromIndex(src)));
            }
        }

        return moves;
    }

    /**
     * Helper to convert index to peg label (0 -> 'A', 1 -> 'B', 2 -> 'C')
     */
    private char getCharFromIndex(int index) {
        return (char) ('A' + index);
    }

    /**
     * Frame-Stewart algorithm to solve Tower of Hanoi with 4 pegs (advanced).
     * Uses approximation to calculate optimal sub-problem size.
     */
    public List<Move> solveFrameStewart(int n, char source, char aux1, char aux2, char destination) {
        List<Move> moves = new ArrayList<>();

        int k = (int) Math.sqrt(2 * n); // Approximate best k

        if (n == 0) return moves;
        if (n == 1) {
            moves.add(new Move(source, destination));
            return moves;
        }

        // Step 1: Move n-k disks to aux1 using all 4 pegs
        moves.addAll(solveFrameStewart(n - k, source, aux2, destination, aux1));

        // Step 2: Move k disks to destination using 3 pegs
        moves.addAll(solveRecursive(k, source, aux2, destination));

        // Step 3: Move n-k disks from aux1 to destination using all 4 pegs
        moves.addAll(solveFrameStewart(n - k, aux1, source, aux2, destination));

        return moves;
    }

    /**
     * Validates a sequence of moves.
     * Ensures no illegal moves and that all disks end up on destination peg.
     */
    public boolean validateMoveSequence(List<Move> moves, int diskCount) {
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

        // Valid if all disks end up on one peg (typically destination peg)
        return pegs[0].isEmpty() && pegs[1].isEmpty() && pegs[2].size() == diskCount;
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

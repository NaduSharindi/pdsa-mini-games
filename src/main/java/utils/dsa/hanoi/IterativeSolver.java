package utils.dsa.hanoi;

import java.util.ArrayList;
import java.util.List;
import services.TowerOfHanoiService.Move;
import utils.dsa.Stack;

/**
 * Iterative solution for the classic 3-peg Tower of Hanoi.
 */
public class IterativeSolver implements HanoiSolver {
    @Override
    public String getName() {
        return "Iterative";
    }
    
    @Override
    public int calculateMinMoves(int numberOfDisks) {
        return (int) Math.pow(2, numberOfDisks) - 1;
    }
    
    /**
     * Solve the Tower of Hanoi problem iteratively
     */
    public List<Move> solve(int n, char source, char auxiliary, char destination) {
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
    
    private char getCharFromIndex(int index) {
        return (char) ('A' + index);
    }
}

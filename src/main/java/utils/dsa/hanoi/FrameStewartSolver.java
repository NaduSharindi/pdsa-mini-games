package utils.dsa.hanoi;

import java.util.ArrayList;
import java.util.List;
import services.TowerOfHanoiService.Move;

/**
 * Frame-Stewart algorithm for the 4-peg Tower of Hanoi.
 */
public class FrameStewartSolver implements HanoiSolver {
    private RecursiveSolver recursiveSolver;
    
    public FrameStewartSolver() {
        this.recursiveSolver = new RecursiveSolver();
    }
    
    @Override
    public String getName() {
        return "Frame-Stewart";
    }
    
    @Override
    public int calculateMinMoves(int n) {
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
            int moves = 2 * calculateMinMoves(n - i) + (1 << i) - 1;
            result = Math.min(result, moves);
        }
        return result;
    }
    
    /**
     * Solve the Tower of Hanoi problem using Frame-Stewart algorithm
     */
    public List<Move> solve(int n, char source, char aux1, char aux2, char destination) {
        List<Move> moves = new ArrayList<>();
        
        if (n == 0) return moves;
        if (n == 1) {
            moves.add(new Move(source, destination));
            return moves;
        }
        
        // Approximate best k value for Frame-Stewart
        int k = (int) Math.sqrt(2 * n);
        
        // Step 1: Move n-k disks to aux1 using all 4 pegs
        moves.addAll(solve(n - k, source, aux2, destination, aux1));
        
        // Step 2: Move k disks to destination using 3 pegs (classic TOH)
        moves.addAll(recursiveSolver.solve(k, source, aux2, destination));
        
        // Step 3: Move n-k disks from aux1 to destination using all 4 pegs
        moves.addAll(solve(n - k, aux1, source, aux2, destination));
        
        return moves;
    }
}

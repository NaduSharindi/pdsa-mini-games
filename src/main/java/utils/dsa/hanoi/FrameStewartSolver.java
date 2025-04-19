package utils.dsa.hanoi;

import java.util.List;

public class FrameStewartSolver implements HanoiSolver {
    @Override
    public void solve(int disks, char source, char destination, char auxiliary, List<String> moves) {
        // Default to 3-peg solution when called with only 3 pegs
        solveTowerOfHanoi(disks, source, destination, auxiliary, moves);
    }

    @Override
    public void solve(int disks, char source, char destination, char auxiliary1, char auxiliary2, List<String> moves) {
        // Base case - if no disks or just one disk, handle directly
        if (disks <= 0) {
            return;
        }
        
        if (disks == 1) {
            moves.add(source + "-" + destination);
            return;
        }
        
        // Calculate the optimal k value for Frame-Stewart algorithm
        int k = calculateOptimalK(disks);
        
        // CRITICAL FIX 1: Ensure k is always smaller than disks to prevent infinite recursion
        if (k >= disks) {
            k = disks - 1;
        }
        
        // CRITICAL FIX 1: Ensure k is always smaller than disks to prevent infinite recursion
        if (k >= disks) {
            k = disks - 1;
        }
        
        // Move top k disks from source to auxiliary1 using 4 pegs
        solve(k, source, auxiliary1, auxiliary2, destination, moves);
        
        // Move remaining (disks-k) disks from source to destination using 3 pegs
        solveTowerOfHanoi(disks - k, source, destination, auxiliary2, moves);
        
        // Move k disks from auxiliary1 to destination using 4 pegs
        solve(k, auxiliary1, destination, auxiliary2, source, moves);
    }
        
    
    
    /**
     * Calculate optimal k for Frame-Stewart algorithm
     * @param n Number of disks
     * @return Optimal k value
     */
    private int calculateOptimalK(int n) {
        // Optimal k for Frame-Stewart is approximately sqrt(2*n)
        // This is a heuristic, not exact
        return (int) Math.round(Math.sqrt(2 * n));
    }
    
    /**
     * Standard 3-peg Tower of Hanoi recursive solution
     * This is a standalone method, not using the solve method
     */
    private void solveTowerOfHanoi(int disks, char source, char destination, char auxiliary, List<String> moves) {
        if (disks <= 0) {
            return;
        }
        
        if (disks == 1) {
            moves.add(source + "-" + destination);
            return;
        }
        
        solveTowerOfHanoi(disks - 1, source, auxiliary, destination, moves);
        moves.add(source + "-" + destination);
        solveTowerOfHanoi(disks - 1, auxiliary, destination, source, moves);
    }
}

package utils.dsa.hanoi;

import java.util.List;
import services.TowerOfHanoiService.Move;

/**
 * Interface for Tower of Hanoi solvers
 */
public interface HanoiSolver {
    /**
     * Get the name of the solver
     */
    String getName();
    
    /**
     * Calculate minimum number of moves needed
     */
    int calculateMinMoves(int numberOfDisks);
}

package utils.dsa.hanoi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the IterativeSolver algorithm.
 */
public class IterativeSolverTest {

    @Test
    void testSolveMatchesRecursive() {
        // Test that the iterative solver produces the same moves as the recursive solver for 3 disks
        IterativeSolver iterative = new IterativeSolver();
        RecursiveSolver recursive = new RecursiveSolver();
        List<String> movesIter = new ArrayList<>();
        List<String> movesRec = new ArrayList<>();
        iterative.solve(3, 'A', 'C', 'B', movesIter);
        recursive.solve(3, 'A', 'C', 'B', movesRec);
        assertEquals(movesRec, movesIter, "Iterative and recursive solvers should produce same moves");
    }
}



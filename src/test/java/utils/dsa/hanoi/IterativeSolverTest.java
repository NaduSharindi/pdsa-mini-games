package utils.dsa.hanoi;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class IterativeSolverTest {
    private final IterativeSolver iterativeSolver = new IterativeSolver();
    private final RecursiveSolver recursiveSolver = new RecursiveSolver();

    @Test
    void testSolveMatchesRecursive() {
        List<String> iterativeMoves = new ArrayList<>();
        List<String> recursiveMoves = new ArrayList<>();
        
        iterativeSolver.solve(3, 'A', 'C', 'B', iterativeMoves);
        recursiveSolver.solve(3, 'A', 'C', 'B', recursiveMoves);
        
        assertEquals(recursiveMoves, iterativeMoves);
    }

    @Test
    void testSolve4Disks() {
        List<String> moves = new ArrayList<>();
        iterativeSolver.solve(4, 'A', 'C', 'B', moves);
        assertEquals(15, moves.size());
        assertEquals("A-B", moves.get(0));
        assertEquals("B-C", moves.get(14));
    }

    @Test
    void test4PegSignatureCalls3PegSolution() {
        List<String> moves = new ArrayList<>();
        iterativeSolver.solve(3, 'A', 'D', 'B', 'C', moves);
        assertEquals(7, moves.size()); // Same as 3-peg solution
    }


}

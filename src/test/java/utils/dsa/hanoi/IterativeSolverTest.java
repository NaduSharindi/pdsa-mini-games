package utils.dsa.hanoi;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.dsa.hanoi.IterativeSolver;
import utils.dsa.hanoi.RecursiveSolver;
import java.util.ArrayList;
import java.util.List;

public class IterativeSolverTest {
    @Test
    void testSolveMatchesRecursive() {
        IterativeSolver iterative = new IterativeSolver();
        RecursiveSolver recursive = new RecursiveSolver();
        List<String> movesIter = new ArrayList<>();
        List<String> movesRec = new ArrayList<>();
        iterative.solve(3, 'A', 'C', 'B', movesIter);
        recursive.solve(3, 'A', 'C', 'B', movesRec);
        assertEquals(movesRec, movesIter);
    }
}


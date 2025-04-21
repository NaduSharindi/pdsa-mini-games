package utils.dsa.hanoi;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the RecursiveSolver algorithm.
 */
public class RecursiveSolverTest {

    @Test
    void testSolve3Disks() {
        RecursiveSolver solver = new RecursiveSolver();
        List<String> moves = new ArrayList<>();
        solver.solve(3, 'A', 'C', 'B', moves);
        String[] expected = {"A-C", "A-B", "C-B", "A-C", "B-A", "B-C", "A-C"};
        assertEquals(expected.length, moves.size(), "Number of moves should match expected");
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], moves.get(i), "Move at index " + i + " should match expected");
        }
    }
}


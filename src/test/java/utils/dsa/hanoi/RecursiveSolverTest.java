package utils.dsa.hanoi;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class RecursiveSolverTest {
    @Test
    void testSolve3Disks() {
        RecursiveSolver solver = new RecursiveSolver();
        List<String> moves = new ArrayList<>();
        solver.solve(3, 'A', 'C', 'B', moves);
        String[] expected = {"A-B", "A-C", "B-C", "A-B", "C-A", "C-B", "A-B"};
        assertEquals(expected.length, moves.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], moves.get(i));
        }
    }
}

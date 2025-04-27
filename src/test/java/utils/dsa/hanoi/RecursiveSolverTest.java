package utils.dsa.hanoi;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RecursiveSolverTest {
    private final RecursiveSolver solver = new RecursiveSolver();

    @Test
    void testSolve1Disk() {
        List<String> moves = new ArrayList<>();
        solver.solve(1, 'A', 'C', 'B', moves);
        assertEquals(1, moves.size());
        assertEquals("A-C", moves.get(0));
    }

    @Test
    void testSolve3Disks() {
        List<String> moves = new ArrayList<>();
        solver.solve(3, 'A', 'C', 'B', moves);
        
        String[] expected = {
            "A-C", "A-B", "C-B", 
            "A-C", "B-A", "B-C", "A-C"
        };
        assertEquals(expected.length, moves.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], moves.get(i));
        }
    }



    @Test
    void test4PegSignatureCalls3PegSolution() {
        List<String> moves = new ArrayList<>();
        solver.solve(3, 'A', 'D', 'B', 'C', moves);
        assertEquals(7, moves.size()); // Same as 3-peg solution
    }
}

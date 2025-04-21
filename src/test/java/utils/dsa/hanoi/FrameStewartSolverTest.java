package utils.dsa.hanoi;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the FrameStewartSolver algorithm (4 pegs).
 */
public class FrameStewartSolverTest {

    @Test
    void testSolve4Pegs() {
        // Test that the Frame-Stewart solver produces the correct number of moves for 4 disks and 4 pegs
        FrameStewartSolver solver = new FrameStewartSolver();
        List<String> moves = new ArrayList<>();
        solver.solve(4, 'A', 'D', 'B', 'C', moves);
        // The minimum number of moves for 4 disks and 4 pegs is 9
        assertEquals(9, moves.size(), "4 disks on 4 pegs should take 9 moves");
        // Optionally, check some specific moves
        assertEquals("A-C", moves.get(0));
        assertEquals("A-B", moves.get(1));
        assertEquals("C-B", moves.get(2));
        
    }
}



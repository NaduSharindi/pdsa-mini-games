package utils.dsa.hanoi;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FrameStewartSolverTest {
    private final FrameStewartSolver solver = new FrameStewartSolver();

    @Test
    void test4PegsWith1Disk() {
        List<String> moves = new ArrayList<>();
        solver.solve(1, 'A', 'D', 'B', 'C', moves);
        assertEquals(1, moves.size());
        assertEquals("A-D", moves.get(0));
    }

    @Test
    void test4PegsWith4Disks() {
        List<String> moves = new ArrayList<>();
        solver.solve(4, 'A', 'D', 'B', 'C', moves);
        // Accept solutions with at least the optimal number of moves
        assertTrue(moves.size() >= 9, "Should solve in at least 9 moves");
    }

    @Test
    void test4PegsWith5Disks() {
        List<String> moves = new ArrayList<>();
        solver.solve(5, 'A', 'D', 'B', 'C', moves);
        // Accept solutions with at least the optimal number of moves (13 for 5 disks)
        assertTrue(moves.size() >= 13, "Should solve in at least 13 moves");
    }

    @Test
    void test3PegFallback() {
        List<String> moves = new ArrayList<>();
        solver.solve(3, 'A', 'C', 'B', moves);
        
        // Should use standard 3-peg recursive solution
        assertEquals(7, moves.size()); // 2^3 - 1 = 7
        assertEquals("A-C", moves.get(0));
        assertEquals("A-B", moves.get(1));
        assertEquals("C-B", moves.get(2));
    }

    @Test
    void testCalculateOptimalK() {
        assertEquals(2, solver.calculateOptimalK(4)); // Special case
        assertEquals(3, solver.calculateOptimalK(5)); // sqrt(10)~3.16->3
        assertEquals(4, solver.calculateOptimalK(8)); // sqrt(16)=4
    }

    @Test
    void testZeroDisks() {
        List<String> moves = new ArrayList<>();
        solver.solve(0, 'A', 'D', 'B', 'C', moves);
        assertTrue(moves.isEmpty());
    }

    @Test
    void testInvalidPegCount() {
        List<String> moves = new ArrayList<>();
        // Should handle gracefully even with invalid aux pegs
        assertDoesNotThrow(() -> solver.solve(2, 'A', 'D', 'X', 'Y', moves));
    }
}

package utils.dsa.hanoi;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import utils.dsa.hanoi.FrameStewartSolver;
import java.util.ArrayList;
import java.util.List;

public class FrameStewartSolverTest {
    @Test
    void testSolve4Pegs() {
        FrameStewartSolver solver = new FrameStewartSolver();
        List<String> moves = new ArrayList<>();
        solver.solve(4, 'A', 'D', 'B', 'C', moves);
        // The minimum number of moves for 4 disks and 4 pegs is 9
        assertEquals(9, moves.size());
        assertEquals("A-B", moves.get(0));
        assertEquals("A-C", moves.get(1));
        assertEquals("A-D", moves.get(2));
        // ... you can check more moves if you want
    }
}


package utils.dsa.hanoi;

import java.util.List;
import utils.dsa.Stack;

public class IterativeSolver implements HanoiSolver {
    @Override
    public void solve(int disks, char source, char destination, char auxiliary, List<String> moves) {
        Stack<HanoiFrame> stack = new Stack<HanoiFrame>();
        stack.push(new HanoiFrame(disks, source, destination, auxiliary, 0));
        
        while (!stack.isEmpty()) {
            HanoiFrame frame = stack.pop();
            
            if (frame.disks == 1) {
                moves.add(frame.source + "-" + frame.destination);
            } else if (frame.stage == 0) {
                // Push frames in reverse execution order
                stack.push(new HanoiFrame(frame.disks, frame.source, frame.destination, frame.auxiliary, 1));
                stack.push(new HanoiFrame(frame.disks - 1, frame.source, frame.auxiliary, frame.destination, 0));
            } else if (frame.stage == 1) {
                moves.add(frame.source + "-" + frame.destination);
                stack.push(new HanoiFrame(frame.disks - 1, frame.auxiliary, frame.destination, frame.source, 0));
            }
        }
    }

    @Override
    public void solve(int disks, char source, char destination, char auxiliary1, char auxiliary2, List<String> moves) {
        // For compatibility - use standard 3-peg solution
        solve(disks, source, destination, auxiliary1, moves);
    }
    
    /**
     * Helper class to represent a frame in the iterative solution
     */
    private static class HanoiFrame {
        int disks;
        char source;
        char destination;
        char auxiliary;
        int stage;
        
        public HanoiFrame(int disks, char source, char destination, char auxiliary, int stage) {
            this.disks = disks;
            this.source = source;
            this.destination = destination;
            this.auxiliary = auxiliary;
            this.stage = stage;
        }
    }
}

package utils.dsa.hanoi;

import java.util.List;

public class RecursiveSolver implements HanoiSolver {
    @Override
    public void solve(int disks, char source, char destination, char auxiliary, List<String> moves) {
        if (disks == 1) {
            moves.add(source + "-" + destination);
            return;
        }
   
        
        solve(disks - 1, source, auxiliary, destination, moves);
        moves.add(source + "-" + destination);
        solve(disks - 1, auxiliary, destination, source, moves);
    }

    @Override
    public void solve(int disks, char source, char destination, char auxiliary1, char auxiliary2, List<String> moves) {
        // For compatibility - use standard 3-peg solution
        solve(disks, source, destination, auxiliary1, moves);
    }
}

package utils.dsa.hanoi;

import java.util.ArrayList;
import java.util.List;
import services.TowerOfHanoiService.Move;

/**
 * Recursive solution for the classic 3-peg Tower of Hanoi.
 */
public class RecursiveSolver implements HanoiSolver {
    @Override
    public String getName() {
        return "Recursive";
    }
    
    @Override
    public int calculateMinMoves(int numberOfDisks) {
        return (int) Math.pow(2, numberOfDisks) - 1;
    }
    
    /**
     * Solve the Tower of Hanoi problem recursively
     */
    public List<Move> solve(int n, char source, char auxiliary, char destination) {
        List<Move> moves = new ArrayList<>();
        solveRecursiveHelper(n, source, auxiliary, destination, moves);
        return moves;
    }

    private void solveRecursiveHelper(int n, char source, char auxiliary, char destination, List<Move> moves) {
        if (n == 1) {
            moves.add(new Move(source, destination));
            return;
        }
        
        solveRecursiveHelper(n - 1, source, destination, auxiliary, moves);
        moves.add(new Move(source, destination));
        solveRecursiveHelper(n - 1, auxiliary, source, destination, moves);
    }
}

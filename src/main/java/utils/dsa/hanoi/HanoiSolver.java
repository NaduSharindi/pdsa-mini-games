package utils.dsa.hanoi;

import java.util.List;

public interface HanoiSolver {
    /**
     * Solves the Tower of Hanoi puzzle for 3 pegs
     * 
     * @param disks Number of disks
     * @param source Source peg
     * @param destination Destination peg
     * @param auxiliary Auxiliary peg
     * @param moves List to store the sequence of moves
     */
    void solve(int disks, char source, char destination, char auxiliary, List<String> moves);
    
    /**
     * Solves the Tower of Hanoi puzzle for 4 pegs
     * 
     * @param disks Number of disks
     * @param source Source peg
     * @param destination Destination peg
     * @param auxiliary1 First auxiliary peg
     * @param auxiliary2 Second auxiliary peg
     * @param moves List to store the sequence of moves
     */
    void solve(int disks, char source, char destination, char auxiliary1, char auxiliary2, List<String> moves);
}

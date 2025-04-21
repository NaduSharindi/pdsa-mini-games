package services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

/**
 * Unit tests for the TowerOfHanoiService logic.
 */
public class TowerOfHanoiServiceTest {

    @Test
    void testGenerateNewPuzzle() {
        // Test that a new puzzle generates valid disk count and move lists
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.generateNewPuzzle();
        int count = service.getCurrentDiskCount();
        assertTrue(count >= 5 && count <= 10, "Disk count should be between 5 and 10");
        assertFalse(service.getOptimalMoves(3).isEmpty(), "3-peg moves should not be empty");
        assertFalse(service.getOptimalMoves(4).isEmpty(), "4-peg moves should not be empty");
    }

    @Test
    void testValidateMovesCorrect3Pegs() {
        // Test that the service validates the correct solution for 3 pegs
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.setPegCount(3);
        service.generateNewPuzzle();
        var correctMoves = service.getOptimalMoves(3);
        assertTrue(service.validateMoves(correctMoves, 3), "Correct moves for 3 pegs should be valid");
    }

    @Test
    void testValidateMovesCorrect4Pegs() {
        // Test that the service validates the correct solution for 4 pegs
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.setPegCount(4);
        service.generateNewPuzzle();
        var correctMoves = service.getOptimalMoves(4);
        assertTrue(service.validateMoves(correctMoves, 4), "Correct moves for 4 pegs should be valid");
    }

    @Test
    void testValidateMovesIncorrect() {
        // Test that an obviously wrong move sequence is invalid
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.setPegCount(3);
        service.generateNewPuzzle();
        var wrongMoves = Arrays.asList("A-B", "B-C"); // Too short, obviously wrong
        assertFalse(service.validateMoves(wrongMoves, 3), "Incorrect moves should be invalid");
    }
    
    @Test
    void testAlgorithmAlternation() {
    	//Test that selection of algorithms alternatively
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.setPegCount(3);
        service.generateNewPuzzle();
        String algo1 = service.getCurrent3PegAlgorithm();
        service.generateNewPuzzle();
        String algo2 = service.getCurrent3PegAlgorithm();
        assertNotEquals(algo1, algo2, "Algorithm should alternate between recursive and iterative");
    }

}


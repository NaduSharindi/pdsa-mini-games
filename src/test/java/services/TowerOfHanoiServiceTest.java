package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TowerOfHanoiServiceTest {
    private TowerOfHanoiService service;
    
    @BeforeEach
    void setUp() {
        service = new TowerOfHanoiService();
    }

    @Test
    void testGenerateNewPuzzle() {
        service.generateNewPuzzle();
        int diskCount = service.getCurrentDiskCount();
        assertTrue(diskCount >= 5 && diskCount <= 10);
        
        // Test 3-peg solutions
        List<String> moves3 = service.getOptimalMoves(3);
        assertFalse(moves3.isEmpty());
        assertEquals((int) Math.pow(2, diskCount) - 1, moves3.size());
        
        // Test 4-peg solutions
        List<String> moves4 = service.getOptimalMoves(4);
        assertFalse(moves4.isEmpty());
    }

    @Test
    void testGetOptimalMoveCount() {
        service.generateNewPuzzle();
        int diskCount = service.getCurrentDiskCount();
        
        // Test 3-peg optimal moves
        assertEquals((int) Math.pow(2, diskCount) - 1, 
                    service.getOptimalMoveCount(3));
        
        // Test 4-peg optimal moves
        int fourPegMoves = service.getOptimalMoves(4).size();
        assertEquals(fourPegMoves, service.getOptimalMoveCount(4));
    }

    @Test
    void testValidateMoves() {
        service.generateNewPuzzle();
        int diskCount = service.getCurrentDiskCount();
        
        // Test valid solution
        List<String> validMoves = service.getOptimalMoves(3);
        assertTrue(service.validateMoves(validMoves, 3));
        
        // Test invalid move sequence
        List<String> invalidMoves = List.of("A-B", "B-A");
        assertFalse(service.validateMoves(invalidMoves, 3));
        
        // Test incomplete solution
        List<String> incompleteMoves = validMoves.subList(0, validMoves.size()-1);
        assertFalse(service.validateMoves(incompleteMoves, 3));
    }

    @Test
    void testCheckAnswer() {
        service.generateNewPuzzle();
        int diskCount = service.getCurrentDiskCount();
        List<String> validMoves = service.getOptimalMoves(3);
        String validSequence = String.join(",", validMoves);
        
        // Test correct answer
        assertTrue(service.checkAnswer("test", validMoves.size(), validSequence, 3));
        
        // Test incorrect move count
        assertFalse(service.checkAnswer("test", validMoves.size() - 1, validSequence, 3));
        
        // Test invalid move sequence
        assertFalse(service.checkAnswer("test", 31, "A-B,invalid", 3));
    }

    @Test
    void testAlgorithmAlternation() {
        service.setPegCount(3);
        service.generateNewPuzzle();
        String firstAlgo = service.getCurrent3PegAlgorithm();
        
        service.generateNewPuzzle();
        String secondAlgo = service.getCurrent3PegAlgorithm();
        
        assertNotEquals(firstAlgo, secondAlgo);
    }

    @Test
    void testPegCountHandling() {
        // Test 3-peg configuration
        service.setPegCount(3);
        assertEquals(3, service.getPegCount());
        
        // Test 4-peg configuration
        service.setPegCount(4);
        assertEquals(4, service.getPegCount());
    }
    
    @Test
    void testNonOptimalSolution() {
        service.generateNewPuzzle();
        // Get the optimal solution
        List<String> optimalMoves = service.getOptimalMoves(3);
        
        List<String> nonOptimalMoves = new ArrayList<>();
        nonOptimalMoves.add("A-B"); // Move disk 1 from A to B
        nonOptimalMoves.add("B-A"); // Move disk 1 back to A
        nonOptimalMoves.addAll(optimalMoves);
        assertTrue(service.validateMoves(nonOptimalMoves, 3));
        assertTrue(service.checkAnswer("test", nonOptimalMoves.size(), String.join(",", nonOptimalMoves), 3));
        assertNotNull(service.getLastResult());
        assertFalse(service.getLastResult().isOptimal());

    }
}

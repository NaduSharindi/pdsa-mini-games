package services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import services.TowerOfHanoiService;
import java.util.Arrays;

public class TowerOfHanoiServiceTest {
    @Test
    void testGenerateNewPuzzle() {
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.generateNewPuzzle();
        int count = service.getCurrentDiskCount();
        assertTrue(count >= 5 && count <= 10);
        assertFalse(service.getOptimalMoves(3).isEmpty());
        assertFalse(service.getOptimalMoves(4).isEmpty());
    }

    @Test
    void testValidateMovesCorrect3Pegs() {
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.setPegCount(3);
        service.generateNewPuzzle();
        int n = service.getCurrentDiskCount();
        // Get the correct moves
        var correctMoves = service.getOptimalMoves(3);
        assertTrue(service.validateMoves(correctMoves, 3));
    }

    @Test
    void testValidateMovesCorrect4Pegs() {
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.setPegCount(4);
        service.generateNewPuzzle();
        int n = service.getCurrentDiskCount();
        var correctMoves = service.getOptimalMoves(4);
        assertTrue(service.validateMoves(correctMoves, 4));
    }

    @Test
    void testValidateMovesIncorrect() {
        TowerOfHanoiService service = new TowerOfHanoiService();
        service.setPegCount(3);
        service.generateNewPuzzle();
        var wrongMoves = Arrays.asList("A-B", "B-C"); // Too short, obviously wrong
        assertFalse(service.validateMoves(wrongMoves, 3));
    }
}

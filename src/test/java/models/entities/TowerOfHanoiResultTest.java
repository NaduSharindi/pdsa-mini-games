package models.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for the TowerOfHanoiResult entity.
 */
public class TowerOfHanoiResultTest {

    @Test
    void testGettersSetters() {
        // Test that setters and getters work as expected
        TowerOfHanoiResult result = new TowerOfHanoiResult("Menaya", 3, "A-B,A-C,B-C", 7, 100, 100, 100, 3, true);
        assertEquals("Menaya", result.getPlayerName());
        assertEquals(3, result.getDiskCount());
        assertEquals("A-B,A-C,B-C", result.getMoveSequence());
        assertEquals(7, result.getMoveCount());
        assertEquals(100, result.getRecursiveTime());
        assertEquals(100, result.getIterativeTime());
        assertEquals(100, result.getFrameStewartTime());
        assertEquals(3, result.getPegCount());
        assertTrue(result.isCorrect());
    }
}



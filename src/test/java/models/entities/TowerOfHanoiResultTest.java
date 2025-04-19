package models.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import models.entities.TowerOfHanoiResult;

public class TowerOfHanoiResultTest {
    @Test
    void testGettersSetters() {
        TowerOfHanoiResult result = new TowerOfHanoiResult("Alice", 3, "A-B,A-C,B-C", 7, 100, 100, 100, 3, true);
        assertEquals("Alice", result.getPlayerName());
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


package models.entities;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TowerOfHanoiResultTest {
    private TowerOfHanoiResult result;
    private final ObjectId testId = new ObjectId();
    private final String testPlayer = "testUser";
    private final String testMoves = "A-B,B-C";
    
    @BeforeEach
    void setUp() {
        result = new TowerOfHanoiResult(
            testPlayer, 
            5, 
            testMoves,
            10,
            100L,
            150L,
            200L,
            3,
            true,
            false
        );
    }

    @Test
    void testGettersAndSetters() {
        // Test ID
        result.setId(testId);
        assertEquals(testId, result.getId());

        // Test Player Name
        result.setPlayerName("newPlayer");
        assertEquals("newPlayer", result.getPlayerName());

        // Test Disk Count
        result.setDiskCount(7);
        assertEquals(7, result.getDiskCount());

        // Test Move Sequence
        result.setMoveSequence("A-C");
        assertEquals("A-C", result.getMoveSequence());

        // Test Move Count
        result.setMoveCount(15);
        assertEquals(15, result.getMoveCount());

        // Test Timings
        result.setRecursiveTime(200L);
        assertEquals(200L, result.getRecursiveTime());

        result.setIterativeTime(300L);
        assertEquals(300L, result.getIterativeTime());

        result.setFrameStewartTime(400L);
        assertEquals(400L, result.getFrameStewartTime());

        // Test Peg Count
        result.setPegCount(4);
        assertEquals(4, result.getPegCount());

        // Test Correctness
        result.setCorrect(false);
        assertFalse(result.isCorrect());

        // Test Optimality
        result.setOptimal(true);
        assertTrue(result.isOptimal());
    }

    @Test
    void testConstructor() {
        assertEquals(testPlayer, result.getPlayerName());
        assertEquals(5, result.getDiskCount());
        assertEquals(testMoves, result.getMoveSequence());
        assertEquals(10, result.getMoveCount());
        assertEquals(100L, result.getRecursiveTime());
        assertEquals(150L, result.getIterativeTime());
        assertEquals(200L, result.getFrameStewartTime());
        assertEquals(3, result.getPegCount());
        assertTrue(result.isCorrect());
        assertFalse(result.isOptimal());
    }

    @Test
    void testDefaultConstructor() {
        TowerOfHanoiResult empty = new TowerOfHanoiResult();
        assertNull(empty.getPlayerName());
        assertEquals(0, empty.getDiskCount());
    }

    @Test
    void testOptimalFlag() {
        TowerOfHanoiResult optimalResult = new TowerOfHanoiResult(
            "optimalUser",
            5,
            testMoves,
            31, // Optimal move count for 5 disks
            50L,
            75L,
            100L,
            3,
            true,
            true
        );
        assertTrue(optimalResult.isOptimal());
    }
}

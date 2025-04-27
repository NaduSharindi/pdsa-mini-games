package services;

import models.entities.EightQueensResult;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import dev.morphia.Datastore;
import java.lang.reflect.Field;

/**
 * Essential unit tests for EightQueensService.
 */
public class EightQueensServiceTest {

    private EightQueensService service;
    private Datastore datastore;

    // Helper to access private datastore via reflection
    private Datastore getPrivateDatastore() throws Exception {
        Field field = EightQueensService.class.getDeclaredField("datastore");
        field.setAccessible(true);
        return (Datastore) field.get(service);
    }

    @BeforeEach
    public void setUp() throws Exception {
        service = new EightQueensService();
        datastore = getPrivateDatastore();
     
    }

    /**
     * Test sequential solution generation and storage.
     */
    @Test
    public void testSequentialSolutionGeneration() {
        long time = service.findAllSolutionsSequential();
        // Should find and store exactly 92 sequential solutions
        long dbCount = datastore.find(EightQueensResult.class)
            .filter("algorithmType", "Sequential").count();
        assertEquals(92, dbCount, "Should store 92 Sequential solutions in DB");
        assertTrue(time > 0, "Time taken should be positive");
    }

    /**
     * Test threaded solution generation and storage.
     */
    @Test
    public void testThreadedSolutionGeneration() {
        long time = service.findAllSolutionsThreaded();
        // Should find and store exactly 92 threaded solutions
        long dbCount = datastore.find(EightQueensResult.class)
            .filter("algorithmType", "Threaded").count();
        assertEquals(92, dbCount, "Should store 92 Threaded solutions in DB");
        assertTrue(time > 0, "Time taken should be positive");
    }

    /**
     * Test that a valid solution is recognized.
     */
    @Test
    public void testValidatePlayerSolution() {
        service.findAllSolutionsSequential();
        int[] valid = {0, 4, 7, 5, 2, 6, 1, 3};
        assertTrue(service.validatePlayerSolution(valid), "Valid solution should be recognized");
    }

    /**
     * Test saving and recognizing a player's solution.
     */
    @Test
    public void testSaveAndRecognizePlayerSolution() {
        service.findAllSolutionsSequential();
        int[] valid = {0, 4, 7, 5, 2, 6, 1, 3};
        String player = "Alice";
        service.savePlayerSolution(valid, player);
        assertTrue(service.isSolutionRecognized(valid), "Solution should be marked as recognized");
        EightQueensResult result = datastore.find(EightQueensResult.class)
            .filter("positions", valid).first();
        assertNotNull(result, "Result should exist in DB");
        assertEquals(player, result.getPlayerName(), "Player name should be saved");
    }
}

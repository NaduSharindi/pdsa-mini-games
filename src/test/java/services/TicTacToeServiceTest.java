package services;

import dev.morphia.Datastore;  // Import Morphia Datastore for MongoDB interaction
import dev.morphia.DeleteOptions;  // Import Morphia's DeleteOptions for deleting data from MongoDB
import dev.morphia.Morphia;  // Import Morphia to create a Datastore
import com.mongodb.client.MongoClients;  // Import MongoDB client to connect to MongoDB
import models.entities.TicTacToeResult;  // Import the TicTacToeResult model
import org.junit.jupiter.api.*;  // Import JUnit 5 annotations and assertions
import static org.junit.jupiter.api.Assertions.*;  // Import assertions

class TicTacToeServiceTest { // Integration test class for TicTacToeService
    private static Datastore datastore;  // Declare the Datastore instance to interact with MongoDB
    private TicTacToeService service;  // Declare the service instance which will be tested

    // Setup method to initialize the database connection before all tests
    @BeforeAll
    static void setupDB() {
        // Create a connection to the MongoDB instance at localhost:27017 with the database 'test_db'
        datastore = Morphia.createDatastore(
            MongoClients.create("mongodb://localhost:27017"), 
            "test_db"
        );
        // Map the models to the datastore, specifically the classes in the 'models.entities' package
        datastore.getMapper().mapPackage("models.entities");
        // Ensure indexes are created on the database (useful for queries)
        datastore.ensureIndexes();
    }

    // Method to clear the database before each test to ensure tests are isolated and data does not persist
    @BeforeEach
    void clearDB() {
        // Delete all TicTacToeResult documents in the database before each test
        datastore.find(TicTacToeResult.class).delete(new DeleteOptions().multi(true));
        // Initialize the service instance that will be tested
        service = new TicTacToeService(datastore);
    }

    // Test case to verify that the saveResult method persists the result to the database
    @Test
    void saveResult_ShouldPersistToDatabase() {
        // Call the saveResult method of the service to store a result in the database
        service.saveResult("Ranu", 2, 3, "Minimax", 150L, true);
        
        // Query the database to retrieve the TicTacToeResult for player "Ranu"
        TicTacToeResult result = datastore.find(TicTacToeResult.class)
                                        .filter("playerName", "Ranu")
                                        .first();
        
        // Assert that the result is not null, indicating the result was saved successfully
        assertNotNull(result);
        // Assert that the move row and column match the values passed to saveResult
        assertEquals(2, result.getMoveRow());
        assertEquals(3, result.getMoveCol());
    }
}

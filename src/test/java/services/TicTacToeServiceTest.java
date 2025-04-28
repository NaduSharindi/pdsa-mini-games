package services;

import dev.morphia.Datastore;
import dev.morphia.DeleteOptions;
import dev.morphia.Morphia;
import com.mongodb.client.MongoClients;
import models.entities.TicTacToeResult;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class TicTacToeServiceTest { // Integration test
    private static Datastore datastore;
    private TicTacToeService service;

    @BeforeAll
    static void setupDB() {
        datastore = Morphia.createDatastore(
            MongoClients.create("mongodb://localhost:27017"), 
            "test_db"
        );
        datastore.getMapper().mapPackage("models.entities");
        datastore.ensureIndexes();
    }

    @BeforeEach
    void clearDB() {
        datastore.find(TicTacToeResult.class).delete(new DeleteOptions().multi(true));
        service = new TicTacToeService(datastore);
    }

    @Test
    void saveResult_ShouldPersistToDatabase() {
        service.saveResult("Ranu", 2, 3, "Minimax", 150L, true);
        
        TicTacToeResult result = datastore.find(TicTacToeResult.class)
                                        .filter("playerName", "Ranu")
                                        .first();
        
        assertNotNull(result);
        assertEquals(2, result.getMoveRow());
        assertEquals(3, result.getMoveCol());
    }
}

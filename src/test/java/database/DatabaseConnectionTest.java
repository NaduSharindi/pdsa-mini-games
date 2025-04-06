package database;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.mongodb.client.model.ReturnDocument;

import dev.morphia.ModifyOptions;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.UpdateOperators;
import models.entities.TestCollection;
import models.exceptions.DatabaseException;
import utils.DatabaseConnection;

public class DatabaseConnectionTest {

	/**
	 * test the database connection throw any exception
	 */
	@Test
	void testDbConnectionInitialization() {
		assertDoesNotThrow(() -> {
			DatabaseConnection instance = DatabaseConnection.getInstance();
		}, "database connection should not throw an exception");
	}

	/**
	 * test database connection can not be null
	 */
	@Test
	void testDbConnectionNotNull() throws DatabaseException {
		DatabaseConnection instance = DatabaseConnection.getInstance();
		assertNotNull(instance, "database connection can not be null");
	}

	/**
	 * test the database connection should be implemented with singleton pattern
	 */
	@Test
	void testDbConnectionSingletonPattern() throws DatabaseException {
		DatabaseConnection instance1 = DatabaseConnection.getInstance();
		DatabaseConnection instance2 = DatabaseConnection.getInstance();
		assertSame(instance1, instance2,
				"database connection should be same because database connection implemented with singleton");
	}

	/**
	 * test database CRUD operations
	 */
	@Test
	void testDbCrudOperations() throws DatabaseException {
		try {
			DatabaseConnection dbInstance = DatabaseConnection.getInstance();
			// create test data
			TestCollection testData = new TestCollection("Ashen", 10);

			// save test data in database
			TestCollection savedData = dbInstance.getDatastore().save(testData);
			assertNotNull(savedData, "saved test data should not null");

			// fetch data from database
			TestCollection fetchedData = dbInstance.getDatastore().find(TestCollection.class)
					.filter(Filters.eq("name", testData.getName())).first();
			assertNotNull(fetchedData, "fetched data should not be null");
			assertEquals(testData.getName(), fetchedData.getName(), "fetched data conflicts with orginal data");
			assertEquals(testData.getScore(), fetchedData.getScore(), "fetched data conflicts with orginal data");

			// update test data
			TestCollection updatedData = dbInstance.getDatastore().find(TestCollection.class)
					.filter(Filters.eq("name", testData.getName()))
					.modify(UpdateOperators.set("name", "Sudaraka"), UpdateOperators.set("score", 8000))
					.execute(new ModifyOptions().returnDocument(ReturnDocument.AFTER));
			assertNotNull(updatedData, "updated data should not be null");

			TestCollection fetchUpdatedData = dbInstance.getDatastore().find(TestCollection.class)
					.filter(Filters.eq("name", "Sudaraka")).first();
			assertNotNull(fetchUpdatedData, "fetched updated data should not be null");
			assertEquals("Sudaraka", fetchUpdatedData.getName(), "fetched updated data conflict with orginal data");
			assertEquals(8000, fetchUpdatedData.getScore(), "fetched updated data conflict with orginal data");

			// delete test data
			TestCollection deletedData = dbInstance.getDatastore().find(TestCollection.class)
					.filter(Filters.eq("name", "Sudaraka")).findAndDelete();
			assertNotNull(deletedData);
			
			TestCollection fetchDeletedData = dbInstance.getDatastore().find(TestCollection.class)
					.filter(Filters.eq("name", "Sudaraka")).first();
			assertNull(fetchDeletedData, "fetched deleted data should be null");
		}catch (Exception exception) {
			fail(exception.getMessage());
		}
		
	}
}

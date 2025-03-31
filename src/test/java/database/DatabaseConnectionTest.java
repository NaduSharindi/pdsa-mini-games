package database;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import utils.DatabaseConnection;

public class DatabaseConnectionTest {

	/**
	 * Test the database connection is opened
	 */
	@Test
	void testDatabaseConnection() {
		DatabaseConnection con = DatabaseConnection.getInstance();
		assertNotNull(con, "Database connection should not be null");
	}

}

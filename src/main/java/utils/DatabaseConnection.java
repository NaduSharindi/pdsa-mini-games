package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import dev.morphia.Datastore;
import dev.morphia.Morphia;

public class DatabaseConnection {
	// database configurations
	private static final String DATABASE_URI = "mongodb://localhost:27017/";
	private static final String DATABASE_NAME = "minigamesdb";

	private static DatabaseConnection instance = null;
	private Datastore datastore;

	/**
	 * Private constructor for ensure singleton for database
	 */
	private DatabaseConnection() {
		try {
			MongoClient mongoClient = MongoClients.create(DATABASE_URI);
			this.datastore = Morphia.createDatastore(mongoClient, DATABASE_NAME);
		} catch (Exception e) {

		}

	}

	/**
	 * Get the database connection instance as singleton
	 * 
	 * @return
	 */
	public static DatabaseConnection getInstance() {
		if (instance == null) {
			synchronized (DatabaseConnection.class) {
				if (instance == null) {
					instance = new DatabaseConnection();
				}
			}
		}

		return instance;
	}

	/**
	 * Get the datastore from the database connection class
	 * 
	 * @return Datastore
	 */
	public Datastore getDatastore() {
		return datastore;
	}

}

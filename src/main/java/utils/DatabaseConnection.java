package utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import models.entities.TestCollection;
import models.exceptions.DatabaseException;

import models.entities.TowerOfHanoiResult;

public class DatabaseConnection {
	// database configurations
	private static final String DATABASE_URI = "mongodb://localhost:27017/";
	private static final String DATABASE_NAME = "minigamesdb";

	private static DatabaseConnection instance = null;
	private Datastore datastore;

	/**
	 * Private constructor for ensure the singleton design pattern for database
	 * connection
	 * 
	 * @throws DatabaseException
	 */
	private DatabaseConnection() throws DatabaseException {
		try {
			MongoClient mongoClient = MongoClients.create(DATABASE_URI);
			this.datastore = Morphia.createDatastore(mongoClient, DATABASE_NAME);
			
			//map database collection entity classes to data store
			this.mapDatabaseColletionEntities(this.datastore);
		} catch (Exception e) {
			throw new DatabaseException(e.getMessage());
		}

	}

	/**
	 * Get the database connection instance as singleton
	 * 
	 * @return
	 */
	public static DatabaseConnection getInstance() throws DatabaseException {
		if (instance == null) {
			synchronized (DatabaseConnection.class) {
				if (instance == null) {
					try {
						instance = new DatabaseConnection();
					} catch (DatabaseException e) {
						throw e;
					}
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
	
	
	/**
	 * map database collection entities to data store
	 * 
	 * @param datastore
	 */
	private void mapDatabaseColletionEntities(Datastore datastore) {
		datastore.getMapper().map(TestCollection.class);
		datastore.getMapper().map(TowerOfHanoiResult.class);
		//other data store configuration
		datastore.ensureIndexes();
	}
	
    public void save(TowerOfHanoiResult result) throws DatabaseException {
        try {
            datastore.save(result);
        } catch (Exception e) {
            throw new DatabaseException("Failed to save Tower of Hanoi result: " + e.getMessage());
        }
    }
	


}

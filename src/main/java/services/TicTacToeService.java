package services;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import com.mongodb.client.MongoClients;
import models.entities.TicTacToeResult;

public class TicTacToeService {

    private final Datastore datastore;

    // No-argument constructor - creates and initializes its own Datastore
    public TicTacToeService() {
        // Initialize the MongoDB connection and Datastore for the database "minigamesdb"
        this.datastore = Morphia.createDatastore(
            MongoClients.create("mongodb://localhost:27017"), "minigamesdb"
        );
        // Map the "models.entities" package for entity scanning and ensure indexes are created
        this.datastore.getMapper().mapPackage("models.entities");
        this.datastore.ensureIndexes();
    }

    // Constructor that accepts an external Datastore
    public TicTacToeService(Datastore datastore) {
        // Use the provided Datastore for database operations
        this.datastore = datastore;
    }

    // Saves the result of a Tic-Tac-Toe game to the database
    public void saveResult(String winner, int row, int col,
                           String algorithmType, long timeTaken, boolean isWin) {
        // Create a new result object with the provided details
        TicTacToeResult result = new TicTacToeResult(
            winner, row, col, algorithmType, timeTaken, isWin
        );
        // Save the result object into the database
        datastore.save(result);
    }

    // Retrieves average algorithm time from database
    public long getAverageTimeForAlgorithm(String algorithmType) {
        try {
            // Calculate and return the average time taken for a given algorithm type
            return (long) datastore.find(TicTacToeResult.class)
                .filter("algorithmType", algorithmType)  // Filter results by algorithm type
                .iterator().toList()  // Convert the results to a list
                .stream()
                .mapToLong(TicTacToeResult::getTimeTakenMs)  // Extract the time taken from each result
                .average()  // Calculate the average of the times
                .orElse(0);  // If no results, return 0
        } catch (Exception e) {
            // If an error occurs, print the error and return 0
            System.err.println("Error getting average time: " + e.getMessage());
            return 0;
        }
    }

    // Gets the win rate for a player
    public double getPlayerWinRate(String playerName) {
        try {
            // Calculate the total number of games played by the player
            long totalGames = datastore.find(TicTacToeResult.class)
                .filter("playerName", playerName)  // Filter results by player name
                .count();
            if (totalGames == 0) return 0;  // If no games played, return 0 win rate
            // Calculate the number of wins for the player
            long wins = datastore.find(TicTacToeResult.class)
                .filter("playerName", playerName)  // Filter results by player name
                .filter("isWin", true)  // Filter results where the player won
                .count();
            // Calculate and return the win rate
            return (double) wins / totalGames;
        } catch (Exception e) {
            // If an error occurs, print the error and return 0 win rate
            System.err.println("Error getting win rate: " + e.getMessage());
            return 0;
        }
    }

    // Save final result (for legacy or extra logging)
    public void saveFinalResult(String playerName, String winner) {
        // This method logs the final result, providing player and winner details
        System.out.println("Saving result: " + playerName + " - Winner: " + winner);
    }
}

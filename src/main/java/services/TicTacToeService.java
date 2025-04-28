package services;

import dev.morphia.Datastore;
import dev.morphia.Morphia;
import com.mongodb.client.MongoClients;
import models.entities.TicTacToeResult;

public class TicTacToeService {

    private final Datastore datastore;

    // No-argument constructor - creates and initializes its own Datastore
    public TicTacToeService() {
        this.datastore = Morphia.createDatastore(
            MongoClients.create("mongodb://localhost:27017"), "minigamesdb"
        );
        this.datastore.getMapper().mapPackage("models.entities");
        this.datastore.ensureIndexes();
    }

    // Constructor that accepts an external Datastore
    public TicTacToeService(Datastore datastore) {
        this.datastore = datastore;
    }

    // Saves the result of a Tic-Tac-Toe game to the database
    public void saveResult(String winner, int row, int col,
                           String algorithmType, long timeTaken, boolean isWin) {
        TicTacToeResult result = new TicTacToeResult(
            winner, row, col, algorithmType, timeTaken, isWin
        );
        datastore.save(result);
    }

    // Retrieves average algorithm time from database
    public long getAverageTimeForAlgorithm(String algorithmType) {
        try {
            return (long) datastore.find(TicTacToeResult.class)
                .filter("algorithmType", algorithmType)
                .iterator().toList()
                .stream()
                .mapToLong(TicTacToeResult::getTimeTakenMs)
                .average()
                .orElse(0);
        } catch (Exception e) {
            System.err.println("Error getting average time: " + e.getMessage());
            return 0;
        }
    }

    // Gets the win rate for a player
    public double getPlayerWinRate(String playerName) {
        try {
            long totalGames = datastore.find(TicTacToeResult.class)
                .filter("playerName", playerName)
                .count();
            if (totalGames == 0) return 0;
            long wins = datastore.find(TicTacToeResult.class)
                .filter("playerName", playerName)
                .filter("isWin", true)
                .count();
            return (double) wins / totalGames;
        } catch (Exception e) {
            System.err.println("Error getting win rate: " + e.getMessage());
            return 0;
        }
    }

    // Save final result (for legacy or extra logging)
    public void saveFinalResult(String playerName, String winner) {
        System.out.println("Saving result: " + playerName + " - Winner: " + winner);
    }
}

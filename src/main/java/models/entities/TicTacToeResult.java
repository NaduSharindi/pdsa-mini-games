package models.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Date;

// The @Entity annotation marks this class as a Morphia entity
// "tictactoe_results" is the name of the MongoDB collection where instances of this class will be stored
@Entity("tictactoe_results")
public class TicTacToeResult {
    
    // The @Id annotation marks this field as the primary key (unique identifier) in the MongoDB collection
    @Id
    private ObjectId id; 
    
    // Player's name who played the game
    private String playerName;
    
    // Row and column where the player made their move
    private int moveRow;
    private int moveCol;
    
    // Algorithm type used by the AI: "Minimax" or "AlphaBeta"
    private String algorithmType; 
    
    // Time taken for the AI to make its move, in milliseconds
    private long timeTakenMs;
    
    // Timestamp of when the result was recorded
    private Date timestamp;
    
    // Flag indicating whether the player won the game
    private boolean isWin;

    // Default constructor required by Morphia (the ODM framework for MongoDB)
    public TicTacToeResult() {
        // Required by Morphia for entity creation
    }

    // Constructor to initialize the TicTacToeResult object with specific details
    public TicTacToeResult(String playerName, int moveRow, int moveCol, 
                          String algorithmType, long timeTakenMs, boolean isWin) {
        this.playerName = playerName;  // Set the player's name
        this.moveRow = moveRow;        // Set the row where the move was made
        this.moveCol = moveCol;        // Set the column where the move was made
        this.algorithmType = algorithmType;  // Set the AI algorithm used (Minimax or AlphaBeta)
        this.timeTakenMs = timeTakenMs;  // Set the time taken for the AI to make the move
        this.timestamp = new Date();   // Set the current timestamp when the result is recorded
        this.isWin = isWin;            // Set whether the player won the game
    }

    // Getter and setter methods for each field
    public ObjectId getId() { 
        return id; 
    }

    public void setId(ObjectId id) { 
        this.id = id; 
    }
    
    public String getPlayerName() { 
        return playerName; 
    }
    
    public void setPlayerName(String playerName) { 
        this.playerName = playerName; 
    }
    
    public int getMoveRow() { 
        return moveRow; 
    }
    
    public void setMoveRow(int moveRow) { 
        this.moveRow = moveRow; 
    }
    
    public int getMoveCol() { 
        return moveCol; 
    }
    
    public void setMoveCol(int moveCol) { 
        this.moveCol = moveCol; 
    }
    
    public String getAlgorithmType() { 
        return algorithmType; 
    }
    
    public void setAlgorithmType(String algorithmType) { 
        this.algorithmType = algorithmType; 
    }
    
    public long getTimeTakenMs() { 
        return timeTakenMs; 
    }
    
    public void setTimeTakenMs(long timeTakenMs) { 
        this.timeTakenMs = timeTakenMs; 
    }
    
    public Date getTimestamp() { 
        return timestamp; 
    }
    
    public void setTimestamp(Date timestamp) { 
        this.timestamp = timestamp; 
    }
    
    public boolean isWin() { 
        return isWin; 
    }
    
    public void setWin(boolean win) { 
        isWin = win; 
    }
}

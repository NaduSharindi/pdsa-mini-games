package models.entities;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

import java.util.Date;

@Entity("tictactoe_results")
public class TicTacToeResult {
    @Id
    private ObjectId id;
    private String playerName;
    private int moveRow;
    private int moveCol;
    private String algorithmType; // "Minimax" or "AlphaBeta"
    private long timeTakenMs;
    private Date timestamp;
    private boolean isWin;

    public TicTacToeResult() {
        // Required by Morphia
    }

    public TicTacToeResult(String playerName, int moveRow, int moveCol, 
                          String algorithmType, long timeTakenMs, boolean isWin) {
        this.playerName = playerName;
        this.moveRow = moveRow;
        this.moveCol = moveCol;
        this.algorithmType = algorithmType;
        this.timeTakenMs = timeTakenMs;
        this.timestamp = new Date();
        this.isWin = isWin;
    }

    // Getters and setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }
    
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    
    public int getMoveRow() { return moveRow; }
    public void setMoveRow(int moveRow) { this.moveRow = moveRow; }
    
    public int getMoveCol() { return moveCol; }
    public void setMoveCol(int moveCol) { this.moveCol = moveCol; }
    
    public String getAlgorithmType() { return algorithmType; }
    public void setAlgorithmType(String algorithmType) { this.algorithmType = algorithmType; }
    
    public long getTimeTakenMs() { return timeTakenMs; }
    public void setTimeTakenMs(long timeTakenMs) { this.timeTakenMs = timeTakenMs; }
    
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    
    public boolean isWin() { return isWin; }
    public void setWin(boolean win) { isWin = win; }
}
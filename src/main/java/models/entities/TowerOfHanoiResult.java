package models.entities;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("towerOfHanoiResults")
public class TowerOfHanoiResult {
    @Id
    private ObjectId id;
    private String playerName;
    private int numberOfDisks;
    private int numberOfMoves;
    private List<String> moveSequence;
    private long recursiveTime;
    private long iterativeTime;
    private long frameStewartTime;
    private Date timestamp;
    
    // Default constructor required by Morphia
    public TowerOfHanoiResult() {
    }
    
    // Getters and setters
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
    
    public int getNumberOfDisks() {
        return numberOfDisks;
    }
    
    public void setNumberOfDisks(int numberOfDisks) {
        this.numberOfDisks = numberOfDisks;
    }
    
    public int getNumberOfMoves() {
        return numberOfMoves;
    }
    
    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }
    
    public List<String> getMoveSequence() {
        return moveSequence;
    }
    
    public void setMoveSequence(List<String> moveSequence) {
        this.moveSequence = moveSequence;
    }
    
    public long getRecursiveTime() {
        return recursiveTime;
    }
    
    public void setRecursiveTime(long recursiveTime) {
        this.recursiveTime = recursiveTime;
    }
    
    public long getIterativeTime() {
        return iterativeTime;
    }
    
    public void setIterativeTime(long iterativeTime) {
        this.iterativeTime = iterativeTime;
    }
    
    public long getFrameStewartTime() {
        return frameStewartTime;
    }
    
    public void setFrameStewartTime(long frameStewartTime) {
        this.frameStewartTime = frameStewartTime;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

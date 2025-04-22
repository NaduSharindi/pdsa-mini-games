package models.entities;

import org.bson.types.ObjectId;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("towerOfHanoiResults")
public class TowerOfHanoiResult {
    @Id
    private ObjectId id;
    private String playerName;
    private int diskCount;
    private String moveSequence;
    private int moveCount;
    private long recursiveTime;
    private long iterativeTime;
    private long frameStewartTime;
    private int pegCount;
    private boolean isCorrect;
    private boolean isOptimal;

    // Empty constructor for Morphia
    public TowerOfHanoiResult() {}


    

    public TowerOfHanoiResult(String playerName, int diskCount, String moveSequence, 
                             int moveCount, long recursiveTime, long iterativeTime, 
                             long frameStewartTime, int pegCount, boolean isCorrect, boolean isOptimal) {
        this.playerName = playerName;
        this.diskCount = diskCount;
        this.moveSequence = moveSequence;
        this.moveCount = moveCount;
        this.recursiveTime = recursiveTime;
        this.iterativeTime = iterativeTime;
        this.frameStewartTime = frameStewartTime;
        this.pegCount = pegCount;
        this.isCorrect = isCorrect;
        this.isOptimal = isOptimal;
    }

    // Getters and setters
    public ObjectId getId() {
    	return id; }
    public void setId(ObjectId id) {
    	this.id = id; }
    public String getPlayerName() { 
    	return playerName; }
    public void setPlayerName(String playerName) { 
    	this.playerName = playerName; }
    public int getDiskCount() {
    	return diskCount; }
    public void setDiskCount(int diskCount) {
    	this.diskCount = diskCount; }
    public String getMoveSequence() { 
    	return moveSequence; }
    public void setMoveSequence(String moveSequence) { 
    	this.moveSequence = moveSequence; }
    public int getMoveCount() { 
    	return moveCount; }
    public void setMoveCount(int moveCount) {
    	this.moveCount = moveCount; }
    public long getRecursiveTime() { 
    	return recursiveTime; }
    public void setRecursiveTime(long recursiveTime) {
    	this.recursiveTime = recursiveTime; }
    public long getIterativeTime() {
    	return iterativeTime; }
    public void setIterativeTime(long iterativeTime) {
    	this.iterativeTime = iterativeTime; }
    public long getFrameStewartTime() {
    	return frameStewartTime; }
    public void setFrameStewartTime(long frameStewartTime) { 
    	this.frameStewartTime = frameStewartTime; }
    public int getPegCount() { 
    	return pegCount; }
    public void setPegCount(int pegCount) {
    	this.pegCount = pegCount; }
    public boolean isCorrect() { 
    	return isCorrect; }
    public void setCorrect(boolean correct) {
    	isCorrect = correct; }
    public boolean isOptimal() {
        return isOptimal; }
    public void setOptimal(boolean optimal) {
    	isOptimal = optimal; }
}

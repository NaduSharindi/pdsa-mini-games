/**
 * 
 */
package models.entities;

import org.bson.types.ObjectId;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import java.util.Date;
import java.util.Arrays;

/**
 * 
 */
@Entity("eightqueensresults")
public class EightQueensResult {
	@Id
	private ObjectId id;
	private int[] positions;
	private String playerName;
	private long timeTaken;
	private String algorithmType;
	private boolean isRecognized;
	private Date timestamp;
	
	//default constructor required by Morphia
	public EightQueensResult() {
		
	}
	
	//parameterized constructor
	public EightQueensResult(int[] positions, String playerName, long timeTaken,
			String algorithmType, boolean isRecognized, Date timestamp) {
        this.positions = positions;
        this.playerName = playerName;
        this.timeTaken = timeTaken;
        this.algorithmType = algorithmType;
        this.isRecognized = isRecognized;
        this.timestamp = timestamp;
	}
	
	//Getters and Setters
	public ObjectId getId() {
		return id;
	}
	
	public void setId(ObjectId id) {
		this.id= id;	
	}
	
	 public int[] getPositions() {
	        return positions;
	 }

	 public void setPositions(int[] positions) {
	        this.positions = positions;
	 }

	 public String getPlayerName() {
	        return playerName;
	 }

	 public void setPlayerName(String playerName) {
	        this.playerName = playerName;
	 }

	 public long getTimeTaken() {
	        return timeTaken;
	 }

	 public void setTimeTaken(long timeTaken) {
	        this.timeTaken = timeTaken;
	 }

	 public String getAlgorithmType() {
	        return algorithmType;
	 }

	 public void setAlgorithmType(String algorithmType) {
	        this.algorithmType = algorithmType;
	 }

	 public boolean isRecognized() {
	        return isRecognized;
	 }

	 public void setRecognized(boolean recognized) {
	        isRecognized = recognized;
	 }

	 public Date getTimestamp() {
	        return timestamp;
	 }

	 public void setTimestamp(Date timestamp) {
	        this.timestamp = timestamp;
	 }
	 
	 // equals and hashCode for comparing solutions based on positions
	 @Override
	 public boolean equals(Object obj) {
		 if(this == obj) 
			 return true;
		 if(!(obj instanceof EightQueensResult))
			 return false;
		 EightQueensResult other = (EightQueensResult) obj;
		 return Arrays.equals(this.positions,other.positions);
	 }
	 
	 @Override
	 public int hashCode() {
		 return Arrays.hashCode(positions);
	 }
	 
	 @Override
	    public String toString() {
	        return "EightQueensResult{" +
	                "positions=" + Arrays.toString(positions) +
	                ", playerName='" + playerName + '\'' +
	                ", timeTaken=" + timeTaken +
	                ", algorithmType='" + algorithmType + '\'' +
	                ", isRecognized=" + isRecognized +
	                ", timestamp=" + timestamp +
	                '}';
	    }

}

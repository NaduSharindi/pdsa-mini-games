package models.entities;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;

@Entity("travelingsalesmanresult")
public class TravelingSalesManResult {
	@Id
	private ObjectId id;
	private String name;
	private String algorithm;
	private String sourceCity;
	private List<String> destinationCities;
	private long timeTaken;
	private LocalDateTime lastCreated;

	public TravelingSalesManResult() {
	}

	public TravelingSalesManResult(String name, String algorithm, String sourceCity,
			List<String> destinationCities, long timeTaken) {
		this.name = name;
		this.algorithm = algorithm;
		this.sourceCity = sourceCity;
		this.destinationCities = destinationCities;
		this.timeTaken = timeTaken;
	}
	
    @PrePersist
    void prePersist() {
        if (lastCreated == null) {
        	lastCreated = LocalDateTime.now();
        }
    }

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getSourceCity() {
		return sourceCity;
	}

	public void setSourceCity(String sourceCity) {
		this.sourceCity = sourceCity;
	}

	public List<String> getDestinationCities() {
		return destinationCities;
	}

	public void setDestinationCities(List<String> destinationCities) {
		this.destinationCities = destinationCities;
	}

	public long getTimeTaken() {
		return timeTaken;
	}

	public void setTimeTaken(long timeTaken) {
		this.timeTaken = timeTaken;
	}

	public LocalDateTime getLastCreated() {
		return lastCreated;
	}

	public void setLastCreated(LocalDateTime lastCreated) {
		this.lastCreated = lastCreated;
	}
}

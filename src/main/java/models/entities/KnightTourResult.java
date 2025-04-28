package models.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.PrePersist;

@Entity("knighttourresult")
public class KnightTourResult {
	@Id
	private ObjectId id;
	private String name;
	private String algorithm;
	private List<Position> path;
	private long timeTaken;
	private LocalDateTime lastCreated;

	public KnightTourResult() {
	}

	public KnightTourResult(String name, String algorithm, List<Position> path, long timeTaken) {
		this.name = name;
		this.algorithm = algorithm;
		this.path = path;
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

	public List<Position> getPath() {
		return path;
	}

	public void setPath(List<Position> path) {
		this.path = path;
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

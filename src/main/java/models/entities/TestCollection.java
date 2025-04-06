package models.entities;

import org.bson.types.ObjectId;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("testcollection")
public class TestCollection {
	// properties
	@Id
	private ObjectId id;

	private String name;
	private Integer score;

	// constructors
	public TestCollection() {
	}

	public TestCollection(String name, Integer score) {
		this.name = name;
		this.score = score;
	}

	// getters and setters
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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

}

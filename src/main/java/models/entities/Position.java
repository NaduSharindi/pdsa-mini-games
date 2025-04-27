package models.entities;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;

@Entity
public class Position {
	private int row;
	private int col;
	private int sequence;

	public Position() {
	}

	public Position(int row, int col, int sequence) {
		this.row = row;
		this.col = col;
		this.sequence = sequence;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}

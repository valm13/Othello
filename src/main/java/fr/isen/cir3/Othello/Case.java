package fr.isen.cir3.Othello;

import java.io.Serializable;

public class Case implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int row,col,value;
	
	public Case(int row, int col, int value) {
		// TODO Auto-generated constructor stub
		this.row = row;
		this.col = col;
		this.value = value;
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

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Case [row=" + row + ", col=" + col + ", value=" + value + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Case other = (Case) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		if (value != other.value)
			return false;
		return true;
	}
}

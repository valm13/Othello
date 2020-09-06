package fr.isen.cir3.Othello;

public class Point {
    public final int row;
    public final int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
package fr.isen.cir3.Othello;

public class Score {
	private int noirs,blancs;
	
	public Score(int noirs, int blancs) {
		this.noirs = noirs;
		this.blancs = blancs;
	}

	public int getNoirs() {
		return noirs;
	}

	public void setNoirs(int noirs) {
		this.noirs = noirs;
	}

	public int getBlancs() {
		return blancs;
	}

	public void setBlancs(int blancs) {
		this.blancs = blancs;
	}

}

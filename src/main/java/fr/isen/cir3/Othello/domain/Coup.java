package fr.isen.cir3.Othello.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "coup")
public class Coup {
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "joueur_id", foreignKey = @ForeignKey(name = "JOUEUR_ID_FK"))
	private User joueur;
	
	@Column(nullable = false)
	private Long col;
	
	@Column(nullable = false)
	private Long row;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getJoueur() {
		return joueur;
	}

	public void setJoueur(User joueur) {
		this.joueur = joueur;
	}

	public Long getCol() {
		return col;
	}

	public void setCol(Long col) {
		this.col = col;
	}

	public Long getRow() {
		return row;
	}

	public void setRow(Long row) {
		this.row = row;
	}
}

package fr.isen.cir3.Othello.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import fr.isen.cir3.Othello.Case;

@Entity
@Table(name = "party")
public class Party {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "joueur_blanc_id", foreignKey = @ForeignKey(name = "JOUEUR_BLANC_ID_FK"))
	private User joueur_blanc;
	
	@ManyToOne
	@JoinColumn(name = "joueur_noir_id", foreignKey = @ForeignKey(name = "JOUEUR_NOIR_ID_FK"))
	private User joueur_noir;
	
	@Column(nullable = false)
	private Long taille;

	@Column(nullable = false)
	private String etat;
	
	@Column(nullable = false)
	private String name;
	
//	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	private Grid grid;
	
	@Column(nullable = false)
	private ArrayList<ArrayList<Case>> grille;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Coup> coups = new ArrayList<>();
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getJoueur_blanc() {
		return joueur_blanc;
	}

	public void setJoueur_blanc(User joueur_blanc) {
		this.joueur_blanc = joueur_blanc;
	}

	public User getJoueur_noir() {
		return joueur_noir;
	}

	public void setJoueur_noir(User joueur_noir) {
		this.joueur_noir = joueur_noir;
	}

	public Long getTaille() {
		return taille;
	}

	public void setTaille(Long taille) {
		this.taille = taille;
	}

	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Coup> getCoups() {
		return coups;
	}

	public void setCoups(List<Coup> coups) {
		this.coups = coups;
	}

	public ArrayList<ArrayList<Case>> getGrille() {
		return grille;
	}

	public void setGrille(ArrayList<ArrayList<Case>> grille) {
		this.grille = grille;
	}
}

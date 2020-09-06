package fr.isen.cir3.Othello.form;

import org.hibernate.validator.constraints.Length;

public class PartyForm {
	
	private Long id;
	
	private Long joueur_blanc,joueur_noir,taille;
	
	@Length(min=2,max=20)
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJoueur_blanc() {
		return joueur_blanc;
	}

	public void setJoueur_blanc(Long joueur_blanc) {
		this.joueur_blanc = joueur_blanc;
	}

	public Long getJoueur_noir() {
		return joueur_noir;
	}

	public void setJoueur_noir(Long joueur_noir) {
		this.joueur_noir = joueur_noir;
	}

	public Long getTaille() {
		return taille;
	}

	public void setTaille(Long taille) {
		this.taille = taille;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}

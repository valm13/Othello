package fr.isen.cir3.Othello.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "\"user\"")
public class User {
	@Id
	@Column
	@GeneratedValue
	private Long id;

	@Column(length = 40, nullable = false)
	private String firstname;

	@Column(length = 40, nullable = false)
	private String name;
	
	@Column(length = 40, nullable = false, unique = true)
	private String pseudo;

	@Column(name = "mail", length = 80, nullable = false, unique = true)
	private String email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
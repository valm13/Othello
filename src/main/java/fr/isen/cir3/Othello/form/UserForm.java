package fr.isen.cir3.Othello.form;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;

public class UserForm {
	
	private Long id;
	
	@Length(min=2,max=20)
	private String firstname,name,pseudo;
	
	@Email
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

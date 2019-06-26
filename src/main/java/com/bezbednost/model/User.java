package com.bezbednost.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
	private int id;
    
    @Column(name = "email", unique = true, nullable = false)
	private String email;
    
    @Column(name = "password", unique = false, nullable = false)
	private String password;
    
    @Column(name = "certificate", unique = false, nullable = false)
	private String certificate;
	
    @Column(name = "active", unique = false, nullable = false)
	private boolean active;
    
    @OneToOne
    @JoinColumn(name = "authority", referencedColumnName = "authority_id")
	private Authority authority;
	
	public User() {}

	public User(int id, String email, String password, String certificate, boolean active, Authority authority) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.certificate = certificate;
		this.active = active;
		this.authority = authority;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Authority getAuthority() {
		return authority;
	}

	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
	
	

}

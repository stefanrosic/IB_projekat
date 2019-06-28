package com.bezbednost.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
@Entity
@Table(name = "users")
public class User implements UserDetails{
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false)
	private int id;
    
    @Column(name = "email", nullable = false)
	private String email;
    
    @Column(name = "password", unique = false, nullable = false)
	private String password;
    
    @Column(name = "certificate", unique = false, nullable = true)
	private String certificate;
	
    @Column(name = "active", unique = false, nullable = false)
	private boolean active;
    
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "authority_id"))
	private Set<Authority> authority;
	
	@ManyToMany(cascade=CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name="user_authority",
			joinColumns=@JoinColumn(name="user_id",referencedColumnName="user_id"),
			inverseJoinColumns = @JoinColumn(name="authority_id",referencedColumnName="authority_id"))
	private Set<Authority> user_authorities = new HashSet<>();

	
	public User() {}

	public User(int id, String email, String password, String certificate, boolean active, Set<Authority> authority) {
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

	public Set<Authority> getAuthority() {
		return authority;
	}

	public void setAuthority(Set<Authority> authority) {
		this.authority = authority;
	}

	public Set<Authority> getUser_authorities() {
		return user_authorities;
	}
	
	@Override
	 public Collection<? extends GrantedAuthority> getAuthorities() {
		 return this.user_authorities;
	 }

	public void setUser_authorities(Set<Authority> user_authorities) {
		this.user_authorities = user_authorities;
	}
	
	@Override
	public String getUsername() {
		return this.email;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
        return this.active;
	}

}

package com.bezbednost.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class TokenAuthentification extends AbstractAuthenticationToken{

	private String token;
	private UserDetails principle;

	public TokenAuthentification(UserDetails principle) {
		super(principle.getAuthorities());
		this.principle = principle;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    @Override
    public boolean isAuthenticated() {
        return true;
    }

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return principle;
	}

	@Override
	public String toString() {
		return "TokenAuthentification [token=" + token + ", principle=" + principle + "]";
	}
	
}

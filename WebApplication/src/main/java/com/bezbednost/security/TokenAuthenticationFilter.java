package com.bezbednost.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.bezbednost.service.UserService;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
	
    private TokenHelper tokenHelper;
    private com.bezbednost.service.UserService userService;

    public TokenAuthenticationFilter(TokenHelper tokenHelper, UserService userService) {
    	super();
        this.tokenHelper = tokenHelper;
        this.userService = userService;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter_chain)
			throws ServletException, IOException {

		String user_name;
		String authToken = tokenHelper.getToken(request);
		if(authToken != null) {
	
			//if authToken is not null get user_name from token;
			user_name = tokenHelper.getUsernameFromToken(authToken);
			
			if(user_name != null) {
				//get user by user_name
				UserDetails user_details = userService.loadUserByUsername(user_name);
				//check that is token valid
				if(tokenHelper.validateToken(authToken, user_details)) {
					//if it is crate instance of TokenAuthentification and pass user_details instance to this
					//then set token
					TokenAuthentification authentification = new TokenAuthentification(user_details);
					authentification.setToken(authToken);
					SecurityContextHolder.getContext().setAuthentication(authentification);
				}
			}
		}
		//proceeding to the next element in the chain
		filter_chain.doFilter(request, response);
	}
}

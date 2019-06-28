package com.bezbednost.controller;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezbednost.model.User;
import com.bezbednost.security.AuthenticationRequest;
import com.bezbednost.security.TokenHelper;
import com.bezbednost.security.UserAuthenticationToken;

@RestController
@RequestMapping(value = "/authentication")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private TokenHelper tokenHelper;
	
	@PostMapping
	public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest tokenRequest) throws AuthenticationException, IOException {
		UsernamePasswordAuthenticationToken authenticationRequest = new UsernamePasswordAuthenticationToken(tokenRequest.getUsername(), tokenRequest.getPassword());
		
		final Authentication authentication;
		
		try {
			authentication = authenticationManager.authenticate(authenticationRequest);
		} catch (org.springframework.security.core.AuthenticationException ex) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		User user = (User) authentication.getPrincipal();
		
		String token = tokenHelper.generateToken(user.getUsername());
		long expiresIn = tokenHelper.getExiprationDate(token);
				
		UserAuthenticationToken token_response = new UserAuthenticationToken(token, expiresIn);
				
		return ResponseEntity.ok(token_response);
	}

}

package com.bezbednost.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bezbednost.model.User;

public interface UserServiceInterface extends UserDetailsService{
	
	List<User> findAll();

	User findByEmail(String email);

	User findById(int user_id);

	User save(User user);

	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

}

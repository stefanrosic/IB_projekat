package com.bezbednost.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezbednost.model.Authority;
import com.bezbednost.model.User;

@Scope("session")
@RestController
public class AuthController {
	
	@Autowired
	public static ArrayList<User> users = new ArrayList<>();
	
	static {
		User stefi = new User(1, "stefiKysac@gmail.com", "Danijela", "", true, new Authority());
		User sobi = new User(2, "sobi78000@bl.com", "Sandra", "",true,new Authority());
		
		users.add(stefi);
		users.add(sobi);
	}
	
	@RequestMapping(value = "/api/auth", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public User doLogin(HttpSession session, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password) {
		User temp = users.stream().filter(a -> a.getEmail().equals(email) && a.getPassword().equals(password))
				.findFirst().orElse(null);
		session.setAttribute("user", temp);
		return temp;
	}

}

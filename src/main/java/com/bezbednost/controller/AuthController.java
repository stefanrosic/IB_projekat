package com.bezbednost.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezbednost.model.User;
import com.bezbednost.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public User doLogin(HttpSession session, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password) {
		User temp = userService.findAll().stream().filter(a -> a.getEmail().equals(email) && a.getPassword().equals(password))
				.findFirst().orElse(null);
		session.setAttribute("user", temp);
		String demo = System.getProperty("user.home");
		System.out.println(demo + " =");
		System.out.println(temp.getEmail());
		return temp;
	}
	
	@GetMapping("/test")
	public String login() {
		return "ok";
	}

}

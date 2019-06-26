package com.example.InformacionaBezbednost;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezbednost.model.Authority;
import com.bezbednost.model.User;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	public static ArrayList<User> users = new ArrayList<>();
	
	static {
		User stefi = new User(1, "stefiKysac@gmail.com", "Danijela", "", true, new Authority());
		User sobi = new User(2, "sobi78000@bl.com", "Sandra", "",true,new Authority());
		
		users.add(stefi);
		users.add(sobi);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public User doLogin(HttpSession session, @RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password) {
		User temp = users.stream().filter(a -> a.getEmail().equals(email) && a.getPassword().equals(password))
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

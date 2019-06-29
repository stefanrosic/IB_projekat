package com.bezbednost.controller;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezbednost.model.User;
import com.bezbednost.service.UserService;

import certificateUtil.KeyStoreWriter;


@RestController
@RequestMapping("/keystore")
public class KeystoreController {
	
	@Autowired
    private UserService userService;
	
	@PostMapping("/generate")
	public String generateJKS(@RequestParam("id") int id) {
		User user = userService.findById(id);
		
		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		String path = keyStoreWriter.testIt(user);
		user.setCertificate(path);
		userService.save(user);
		
		return "ok";
	
	}

}

package com.bezbednost.controller;

import com.bezbednost.model.Authority;
import com.bezbednost.model.User;
import com.bezbednost.service.AuthorityServiceInterface;
import com.bezbednost.service.UserService;
import com.google.gson.GsonBuilder;

import certificateUtil.KeyStoreWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.bezbednost.dto.userDTO;
import com.google.gson.Gson;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityServiceInterface authorityService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(){
        Gson gson = new GsonBuilder().create();
        return new ResponseEntity<>(gson.toJson(userService.findAll()) ,HttpStatus.OK);
    }

    @PostMapping("/createAcc")
    public ResponseEntity<userDTO> createAccount(@RequestBody User user){

        Authority authority = authorityService.findByName("Regular");

        User u = userService.findByEmail(user.getEmail());
        if(u != null){
            return new ResponseEntity<userDTO>(HttpStatus.OK);
        }
        u = new User();
        u.setEmail(user.getEmail());
        u.setPassword(passwordEncoder.encode(user.getPassword()));

        u.setActive(false);
		u.getUser_authorities().add(authority);
        u.setCertificate("");

        u = userService.save(u);
        
        User us = userService.findByEmail(u.getEmail());

		KeyStoreWriter keyStoreWriter = new KeyStoreWriter();
		String path = keyStoreWriter.testIt(us);
		us.setCertificate(path);
		userService.save(us);

        return new ResponseEntity<userDTO>(new userDTO(u),HttpStatus.OK);
    }

    @PostMapping("/activate/{id}")
	@PreAuthorize("hasRole('ADMIN')")
    public void activateAccount(@PathVariable("id") int id){
        User user = userService.findById(id);
        if(user != null){
            user.setActive(!user.isActive());
            userService.save(user);
        }

    }

}




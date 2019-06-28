package com.bezbednost.controller;

import com.bezbednost.model.Authority;
import com.bezbednost.model.User;
import com.bezbednost.service.AuthorityServiceInterface;
import com.bezbednost.service.UserService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.bezbednost.dto.userDTO;
import com.google.gson.Gson;

import java.util.ArrayList;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityServiceInterface authorityService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/getAll")
    public String getAll(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(userService.findAll());
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

        u.setActive(true);
		u.getUser_authorities().add(authority);
        u.setCertificate("aa");

        u = userService.save(u);

        return new ResponseEntity<userDTO>(new userDTO(u),HttpStatus.OK);
    }

    @PostMapping("/activate")
    public void activateAccount(@RequestParam("id") int id){
        System.out.println(id);
        User user = userService.findById(id);
        if(user != null){
            user.setActive(!user.isActive());
            userService.save(user);
            System.out.println(user.isActive());
        }

    }




}




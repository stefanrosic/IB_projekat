package com.bezbednost.controller;

import com.bezbednost.model.Authority;
import com.bezbednost.model.User;
import com.bezbednost.service.AuthorityServiceInterface;
import com.bezbednost.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bezbednost.dto.userDTO;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityServiceInterface authorityService;

    @PostMapping("/createAcc")
    public ResponseEntity<userDTO> createAccount(@RequestBody User user){

        Authority authority = authorityService.findByName("Regular");

        User u = userService.findByEmail(user.getEmail());
        if(u != null){
            return new ResponseEntity<userDTO>(HttpStatus.FORBIDDEN);
        }
        u = new User();
        u.setEmail(user.getEmail());
        u.setPassword(user.getPassword());
        u.setActive(true);
        u.setAuthority(authority);
        u.setCertificate("aa");

        u = userService.save(u);

        return new ResponseEntity<userDTO>(new userDTO(u),HttpStatus.OK);
    }
}

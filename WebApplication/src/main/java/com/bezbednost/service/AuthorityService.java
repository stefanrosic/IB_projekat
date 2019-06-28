package com.bezbednost.service;

import com.bezbednost.model.Authority;
import com.bezbednost.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityService implements AuthorityServiceInterface{

    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public Authority findByName(String name) {
        return authorityRepository.findByName(name);
    }

}
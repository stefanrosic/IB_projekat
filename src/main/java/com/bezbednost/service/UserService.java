package com.bezbednost.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bezbednost.model.User;
import com.bezbednost.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface{

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

}

package com.bezbednost.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bezbednost.model.User;
import com.bezbednost.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface{

    private UserRepository userRepository;
	private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
		User user = userRepository.findByEmail(username);
		
		if (user == null) {
			log.error("Bad credentials for username");
		} else if (user.getAuthorities().size() == 0) {
			log.error("Unathorized user");
		}
		
		return user;
	}

	@Override
	public User findById(int user_id) {
		return userRepository.findById(user_id);
	}
}

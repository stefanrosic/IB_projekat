package com.bezbednost.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bezbednost.model.User;
import com.bezbednost.repository.UserRepository;

@Service
public class UserService implements UserServiceInterface{

	protected final Log LOGGER = LogFactory.getLog(getClass());
	
    @Autowired
    private UserRepository userRepository;

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
			LOGGER.error("Bad credentials for username");
		} else if (user.getAuthorities().size() == 0) {
			LOGGER.error("Unathorized user");
		}
		
		return user;
	}

	@Override
	public User findById(int user_id) {
		return userRepository.findById(user_id);
	}
}

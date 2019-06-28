package com.bezbednost.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezbednost.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    User findByEmail(String email);
}

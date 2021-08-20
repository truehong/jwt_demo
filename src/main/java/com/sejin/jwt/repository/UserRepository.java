package com.sejin.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sejin.jwt.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}

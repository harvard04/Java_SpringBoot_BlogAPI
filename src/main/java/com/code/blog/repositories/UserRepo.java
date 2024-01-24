package com.code.blog.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.blog.entities.User;

public interface UserRepo extends JpaRepository<User, Integer> {

	// in case of spring security we are treating email as username so we find the User object by using the email from DB
	Optional<User> findByEmail(String email);
}

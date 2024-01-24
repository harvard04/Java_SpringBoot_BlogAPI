package com.code.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.code.blog.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Integer>{

}

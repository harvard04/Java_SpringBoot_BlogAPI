package com.code.blog.payloads;

import java.util.HashSet;
import java.util.Set;

import com.code.blog.entities.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
	
	private int id;
	
	@NotEmpty
	@Size(min = 4, message = "Username must be of minimum 4 characters")
	private String name;
	
	@Email(message = "Email address is not valid")
	private String email;
	
	@NotEmpty
	@Size(min = 3, max = 10, message = "Password must be of minimum 3 chars and maximum 10 chars")
	//@Pattern(regexp = "")
	private String password;
	
	@NotEmpty
	private String about;
	
	private Set<RoleDto> roles = new HashSet<>();
}

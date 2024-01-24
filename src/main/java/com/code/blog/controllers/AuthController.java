package com.code.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.code.blog.exceptions.ApiException;
import com.code.blog.payloads.JwtAuthRequest;
import com.code.blog.payloads.JwtAuthResponse;
import com.code.blog.payloads.UserDto;
import com.code.blog.security.JwtTokenHelper;
import com.code.blog.services.UserService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	// POST - create Token
	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest jwtAuthRequest) throws Exception{
		
		// first authenticate the username by using the helper function authenticate(username, password)
		this.authenticate(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());
		
		// once the user is authenticated then get the UserDetails of user using UserDetailsService
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtAuthRequest.getUsername());
		
		// generate token for the user using the userDetails via JwtTokenHelper
		String token = this.jwtTokenHelper.generateToken(userDetails);
		
		// set the token in response format that this function will return
		JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
		jwtAuthResponse.setToken(token);
		return new ResponseEntity<JwtAuthResponse>(jwtAuthResponse, HttpStatus.OK);
	}

	private void authenticate(String username, String password) throws Exception {
		// construct a UsernamePasswordAuthenticationToken object from the username and password
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
		
		try {
			// authenticate it via authenticationManager
			this.authenticationManager.authenticate(authenticationToken);
		} 
		catch (BadCredentialsException e) {
			System.out.println("invalid details");
			throw new ApiException("invalid username or password");
		}
	}
	
	// POST - register new User
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) throws Exception{
		UserDto registeredUserDto = this.userService.registerNewUser(userDto);
		return new ResponseEntity<UserDto>(registeredUserDto, HttpStatus.CREATED);
	}
	
}

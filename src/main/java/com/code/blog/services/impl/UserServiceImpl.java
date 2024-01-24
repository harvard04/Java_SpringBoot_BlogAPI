package com.code.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.code.blog.config.AppConstants;
import com.code.blog.entities.Role;
import com.code.blog.entities.User;
import com.code.blog.exceptions.ResourceNotFoundException;
import com.code.blog.payloads.UserDto;
import com.code.blog.repositories.RoleRepo;
import com.code.blog.repositories.UserRepo;
import com.code.blog.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RoleRepo roleRepo;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepo.save(user);
		return this.userToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		user.setName(userDto.getName());
		user.setEmail(userDto.getEmail());
		user.setAbout(userDto.getAbout());
		user.setPassword(userDto.getPassword());
		
		User updatedUser = this.userRepo.save(user);
		UserDto tempUserDto = this.userToDto(updatedUser);
		
		return tempUserDto;
	}

	@Override
	public UserDto getUserById(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		return this.userToDto(user);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> usersList = this.userRepo.findAll();
		List<UserDto> userDtoList = usersList.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
		return userDtoList;
	}

	@Override
	public void deleteUser(Integer userId) {
		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
		this.userRepo.delete(user);
	}
	
	private User dtoToUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		return user;
//		User user = new User();
//		user.setId(userDto.getId());
//		user.setName(userDto.getName());
//		user.setEmail(userDto.getEmail());
//		user.setAbout(userDto.getAbout());
//		user.setPassword(userDto.getPassword());
//		return user;
	}

	private UserDto userToDto(User user) {
		UserDto userDto = this.modelMapper.map(user, UserDto.class);
		return userDto;
//		UserDto userDto = new UserDto();
//		userDto.setId(user.getId());
//		userDto.setName(user.getName());
//		userDto.setEmail(user.getEmail());
//		userDto.setAbout(user.getAbout());
//		userDto.setPassword(user.getPassword());
//		return userDto;
	}

	@Override
	public UserDto registerNewUser(UserDto userDto) {
		User user = this.modelMapper.map(userDto, User.class);
		// converting the normal password to encoded or encrypted password using password encoder
		user.setPassword(this.passwordEncoder.encode(user.getPassword()));
		// get role object for normal user(i.e non ADMIN user), here when we register a new user we give it normal Role
		Role role = this.roleRepo.findById(AppConstants.NORMAL_USER).get();
		// set that role in this user
		user.getRoles().add(role);
		// save the user in DB
		User updatedUser = this.userRepo.save(user);
		return this.modelMapper.map(updatedUser, UserDto.class);
	}
}

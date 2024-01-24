/*package com.code.blog;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BlogAppApisApplicationWithBasicSecurity implements CommandLineRunner{

	// To be used when using BasicSecurityConfig.java
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public static void main(String[] args) {
		SpringApplication.run(BlogAppApisApplication.class, args);
	}
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	// To be used when using BasicSecurityConfig.java
	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder.encode("afd"));
	}
	
}*/
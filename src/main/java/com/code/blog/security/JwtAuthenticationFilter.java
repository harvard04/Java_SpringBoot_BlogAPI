package com.code.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// 1. get token from request header
		String requestToken = request.getHeader("Authorization");
		// ex: Bearer 233537sdfjn
		System.out.println(requestToken);
		
		String username = null;
		String token = null;
		
		if(requestToken != null && requestToken.startsWith("Bearer")) {
			// remover string "Bearer" from the token
			token = requestToken.substring(7);
			try {
				// get username from the Token
				username = this.jwtTokenHelper.getUsernameFromToken(token);
			}
			catch(IllegalArgumentException e) {
				System.out.println("unable to get jwt token");
			}
			catch(ExpiredJwtException e) {
				System.out.println("jwt token has expired");
			}
			catch(MalformedJwtException e) {
				System.out.println("invalid jwt");
			}
		}
		else
			System.out.println("jwt token does not start with bearer");
		
		// 2. Validate token(once we have extracted the token from the request header)
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			if(this.jwtTokenHelper.validateToken(token, userDetails)) {
				// user is valid and now Authentication needs to be done
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
			else {
				System.out.println("invalid jwt token");
			}
		}
		else {
			System.out.println("username is null or context is not null");
		}
		
		filterChain.doFilter(request, response);
	}

}

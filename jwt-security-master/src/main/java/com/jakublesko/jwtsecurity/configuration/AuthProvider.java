package com.jakublesko.jwtsecurity.configuration;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {
	
	@Autowired	
	private UserDSService userService;
	public AuthProvider() {
		userService = new UserDSService();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		System.out.println(" reached in AuthProvider ");
		System.out.println(" authentication "+authentication.getClass());
		
		UserDetails user = null;
		UsernamePasswordAuthenticationToken token =(UsernamePasswordAuthenticationToken)authentication;
		if (token.getPrincipal() != null) {
			System.out.println(" " + token.getPrincipal());
			user =	userService.loadUserByUsername((String)token.getPrincipal());
		} else {
			System.out.println("token principle is null ");
			user =	userService.loadUserByUsername((String)token.getPrincipal());
		}
		if(user==null) {
			System.out.println("UserDetails not initialized");
		}
		user.getAuthorities();	
		token.setDetails(user);
		//token.getAuthorities().addAll(user.getAuthorities());
		//token.setAuthenticated(true);		
		return token;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return true;
	}

}

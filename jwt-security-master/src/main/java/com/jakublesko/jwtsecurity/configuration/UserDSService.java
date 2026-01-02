package com.jakublesko.jwtsecurity.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class UserDSService implements UserDetailsService {
	
	/*
	 * @Autowired UserDao userDao;
	 */

	@Override	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("UserName : "+username);
		if("sandeepb".equals(username)) {
			System.out.println(" in if of loadUserByUsername() ");
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority("Admin");
			Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
			auths.add(auth);
			UserDetails user = new  User(username, username, auths);
			return user;
		} else {
			System.out.println("in else of loadUserByUsername ");
			SimpleGrantedAuthority auth = new SimpleGrantedAuthority("Admin");
			Collection<SimpleGrantedAuthority> auths = new ArrayList<>();
			auths.add(auth);
			UserDetails user = new  User("sandeepb", "sandeepb", auths);
			return user;
		}
	}

}

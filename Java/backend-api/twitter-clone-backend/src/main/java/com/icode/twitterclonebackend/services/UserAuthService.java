package com.icode.twitterclonebackend.services;

import com.icode.twitterclonebackend.models.Role;
import com.icode.twitterclonebackend.models.User;
import com.icode.twitterclonebackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserAuthService implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		List<Role> userRoles = user.getRoles().stream().collect(Collectors.toList());

		List<GrantedAuthority> grantedAuthorities = userRoles.stream().map(r -> {
			return new SimpleGrantedAuthority(r.getName());
		}).collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorities);
	}

	public String saveUser(User user) {
		boolean userExists = userRepository.existsByUsername(user.getUsername());

		if (!userExists) {
			User newUser = new User(
					user.getFirstName(),
					user.getLastName(),
					user.getUsername(),
					passwordEncoder.encode(user.getPassword()),
					Arrays.asList(new Role("ROLE_USER"))
			);
			userRepository.save(newUser);

			return "true";
		}
		return "false";
	}

}

package com.epam.esm.service.security;

import com.epam.esm.dto.UserDto;
import com.epam.esm.service.UserService;
import com.epam.esm.service.security.entity.CustomUserDetails;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired UserService userService;

	public UserDetailsServiceImpl(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto userDto = userService.getByEmail(username);

		if (userDto == null) {
			throw new UsernameNotFoundException("user with name = " + username + " not found");
		}

		return new CustomUserDetails(
				userDto.getId(),
				userDto.getEmail(),
				userDto.getPassword(),
				userDto.getRoles().stream()
						.flatMap(role -> role.getAuthorities().stream())
						.distinct()
						.filter(Objects::nonNull)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet()));
	}
}

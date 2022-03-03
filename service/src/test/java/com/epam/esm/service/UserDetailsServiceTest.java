package com.epam.esm.service;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserRoleDto;
import com.epam.esm.service.security.UserDetailsServiceImpl;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceTest {

	@Mock private static final UserService userService = mock(UserService.class);

	private static final UserDetailsService detailService = new UserDetailsServiceImpl(userService);

	private static UserDto user;

	private static UserDetails userDetails;

	@BeforeAll
	static void initAll() {

		UserRoleDto role1 = new UserRoleDto("role1");
		UserRoleDto role2 = new UserRoleDto("role2");

		role1.setAuthorities(Set.of("auth1"));
		role2.setAuthorities(Set.of("auth2"));
		user = new UserDto();
		user.setId(1);
		user.setPassword("password");
		user.setRoles(Set.of(role1, role2));
		user.setEmail("email");

		Set<GrantedAuthority> authorities =
				user.getRoles().stream()
						.flatMap(role -> role.getAuthorities().stream())
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet());

		userDetails = new User(user.getEmail(), user.getPassword(), authorities);
	}

	@Test
	void getExistingUser() {
		when(userService.getByEmail(anyString())).thenReturn(user);
		Assertions.assertEquals(userDetails, detailService.loadUserByUsername("name"));
	}

	@Test
	void getNotExistingUser() {
		when(userService.getByEmail(anyString())).thenReturn(null);
		Assertions.assertThrows(
				UsernameNotFoundException.class, () -> detailService.loadUserByUsername("name"));
	}
}

package com.epam.esm.service.implementation;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.UserRoleRepository;
import com.epam.esm.service.UserService;
import com.epam.esm.service.implementation.UserServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceImplTest {

	@Mock private static final UserRepository userRepository = mock(UserRepository.class);

	@Mock private static final UserRoleRepository roleRepository = mock(UserRoleRepository.class);

	@Mock private static final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

	private static final UserService userService =
			new UserServiceImpl(userRepository, roleRepository, passwordEncoder);

	private static User testUser;

	@BeforeAll
	public static void init() {
		testUser = new User();
		testUser.setId(1);
		testUser.setEmail("email");
		testUser.setPassword("password");
	}

	@Test
	void getUser() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testUser));
		Assertions.assertEquals(UserDtoMapper.mapUserToDto(testUser), userService.getById(1));
	}

	@Test
	void getNotExistingUser() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(userService.getById(1));
	}

	@Test
	void getAllUser() {
		List<User> users = List.of(testUser, testUser);
		when(userRepository.findAll()).thenReturn(users);
		Assertions.assertEquals(
				users.stream().map(UserDtoMapper::mapUserToDto).collect(Collectors.toList()),
				userService.getAll());
	}

	@Test
	void getAllUserWitchLimitAndOffset() {
		List<User> users = List.of(testUser, testUser);
		when(userRepository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<User>(users));
		Assertions.assertEquals(
				users.stream().map(UserDtoMapper::mapUserToDto).collect(Collectors.toList()),
				userService.getAll(1, 1));
	}

	@Test
	void createUser() {
		when(userRepository.save(anyObject())).thenReturn(testUser);
		Assertions.assertEquals(
				testUser.getId(), userService.create(UserDtoMapper.mapUserToDto(testUser)));
	}

	@Test
	void createUserAndEncodePassword() {
		when(userRepository.save(anyObject())).thenReturn(testUser);
		Assertions.assertEquals(
				testUser.getId(),
				userService.createAndEncodePassword(UserDtoMapper.mapUserToDto(testUser)));
	}

	@Test
	void failedToCreateUser() {
		when(userRepository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		Assertions.assertThrows(
				DuplicateKeyException.class,
				() -> userService.create(UserDtoMapper.mapUserToDto(testUser)));
	}
}

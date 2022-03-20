package com.epam.esm.service.implementation;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.UserRoleRepository;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

	@Mock private static final UserRepository userRepository = mock(UserRepository.class);

	@Mock private static final UserRoleRepository roleRepository = mock(UserRoleRepository.class);

	@Mock private static final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

	private static final UserService userService =
			new UserServiceImpl(userRepository, roleRepository, passwordEncoder);

	private static User testUser;
	private static UserDto testUserDto;

	@BeforeAll
	public static void init() {
		testUser = new User();
		testUser.setId(1);
		testUser.setEmail("email");
		testUser.setPassword("password");

		testUserDto = UserDtoMapper.mapUserToDto(testUser);
	}

	@AfterEach
	public void resetMocks() {
		testUserDto = UserDtoMapper.mapUserToDto(testUser);
		Mockito.reset(userRepository, roleRepository,passwordEncoder);
	}

	@Test
	void createUser() {
		when(userRepository.save(anyObject())).thenReturn(testUser);
		Assertions.assertEquals(
				testUser.getId(), userService.create(testUserDto));
	}

	@Test
	void failedToCreateUser() {
		when(userRepository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		Assertions.assertThrows(
				DuplicateEntityException.class,
				() -> userService.create(testUserDto));
	}

	@Test
	void createUserAndEncodePassword() {
		when(userRepository.save(anyObject())).thenReturn(testUser);
		Assertions.assertEquals(
				testUser.getId(),
				userService.createAndEncodePassword(testUserDto));
	}

	@Test
	void failedToCreateUserAndEncodePassword() {
		when(userRepository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		Assertions.assertThrows(
				DuplicateEntityException.class,
				() -> userService.createAndEncodePassword(testUserDto));
	}

	@Test
	void getUserById() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testUser));
		Assertions.assertEquals(testUserDto, userService.getById(1));
	}

	@Test
	void getNotExistingUserById() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotExistException.class,()->userService.getById(anyLong()));
	}

	@Test
	void getUserByEmail(){
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
		Assertions.assertEquals(testUserDto,userService.getByEmail(anyString()));
	}

	@Test
	void getUserByNullEmail(){
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
		Assertions.assertThrows(BadInputException.class,()->userService.getByEmail(null));
	}

	@Test
	void getNotExistingUserByEmail(){
		when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotExistException.class,()->userService.getByEmail(anyString()));
	}

	@Test
	void getAllUserWitchLimitAndOffset() {
		List<User> users = List.of(testUser, testUser);
		when(userRepository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(users));
		Assertions.assertEquals(
				users.stream().map(UserDtoMapper::mapUserToDto).collect(Collectors.toList()),
				userService.getAll(1, 1));
	}

	@ParameterizedTest
	@CsvSource({"-1,-1", "-1,1", "1,-1"})
	void getAllUserWitchIncorrectLimitAndOffset(int limit,int offset) {
		List<User> users = List.of(testUser, testUser);
		when(userRepository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(users));
		Assertions.assertThrows(BadInputException.class,
				()->userService.getAll(limit, offset));
	}
}

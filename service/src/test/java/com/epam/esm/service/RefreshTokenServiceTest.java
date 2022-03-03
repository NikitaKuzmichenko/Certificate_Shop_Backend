package com.epam.esm.service;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.mapper.RefreshTokenDtoMapper;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.service.implementation.RefreshTokenServiceImpl;
import com.epam.esm.service.implementation.UserServiceImpl;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class RefreshTokenServiceTest {

	@Mock
	private static final RefreshTokenRepository tokenRepository = mock(RefreshTokenRepository.class);

	@Mock private static final UserServiceImpl userService = mock(UserServiceImpl.class);

	private static final RefreshTokenService refreshTokenService =
			new RefreshTokenServiceImpl(tokenRepository, userService);

	private static RefreshToken token;

	private static User user;

	@BeforeAll
	public static void init() {
		user = new User();
		user.setId(1);
		token = new RefreshToken();
		token.setId(1);
		token.setUserId(user);
		token.setExpirationDate(new Date());
		token.setCreationDate(new Date());
		token.setToken("token");
	}

	@AfterEach
	public void resetMocks() {
		Mockito.reset(tokenRepository, userService);
	}

	@Test
	void saveOrUpdateTest() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.ofNullable(token));
		when(tokenRepository.save(anyObject())).thenReturn(token);
		when(userService.getById(anyLong())).thenReturn(UserDtoMapper.mapUserToDto(user));
		Assertions.assertEquals(
				token.getId(),
				refreshTokenService.saveOrUpdate(RefreshTokenDtoMapper.mapRefreshTokenToDto(token)));
	}

	@Test
	void saveOrUpdateWithoutUserTest() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.ofNullable(null));
		when(tokenRepository.save(anyObject())).thenReturn(token);
		when(userService.getById(anyLong())).thenReturn(UserDtoMapper.mapUserToDto(null));
		Assertions.assertNull(
				refreshTokenService.saveOrUpdate(RefreshTokenDtoMapper.mapRefreshTokenToDto(token)));
	}

	@Test
	void findByUserIdTest() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.ofNullable(token));
		Assertions.assertNotNull(refreshTokenService.getByUserId(1));
	}

	@Test
	void findByNotExistingUserIdTest() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(refreshTokenService.getByUserId(1));
	}

	@Test
	void getByIdTest() {
		when(tokenRepository.findById(anyObject())).thenReturn(Optional.ofNullable(token));
		Assertions.assertNotNull(refreshTokenService.getById(1));
	}

	@Test
	void getByNotExistingIdTest() {
		when(tokenRepository.findById(anyObject())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(refreshTokenService.getById(1));
	}

	@Test
	void getByUserEmailTest() {
		when(tokenRepository.findByUserIdEmail(anyString())).thenReturn(Optional.ofNullable(token));
		Assertions.assertNotNull(refreshTokenService.getByUserEmail("e"));
	}

	@Test
	void getByNotExistingUserEmailTest() {
		when(tokenRepository.findByUserIdEmail(anyString())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(refreshTokenService.getByUserEmail("e"));
	}

	@Test
	void getByTokenTest() {
		when(tokenRepository.findByToken(anyString())).thenReturn(Optional.ofNullable(token));
		Assertions.assertNotNull(refreshTokenService.getByToken("1"));
	}

	@Test
	void getByNotExistingTokenTest() {
		when(tokenRepository.findByToken(anyString())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(refreshTokenService.getByToken("1"));
	}
}

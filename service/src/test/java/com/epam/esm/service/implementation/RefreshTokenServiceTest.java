package com.epam.esm.service.implementation;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.RefreshTokenDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.RefreshTokenDtoMapper;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.RefreshToken;
import com.epam.esm.entity.User;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.exception.InvalidTokenException;
import com.epam.esm.repository.RefreshTokenRepository;
import com.epam.esm.service.RefreshTokenService;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;

public class RefreshTokenServiceTest {

	@Mock
	private static final RefreshTokenRepository tokenRepository = mock(RefreshTokenRepository.class);

	@Mock private static final UserServiceImpl userService = mock(UserServiceImpl.class);

	private static final RefreshTokenService refreshTokenService =
			new RefreshTokenServiceImpl(tokenRepository, userService);

	private static RefreshToken token;
	private static RefreshTokenDto tokenDto;

	private static User user;
	private static UserDto userDto;

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

		tokenDto = RefreshTokenDtoMapper.mapRefreshTokenToDto(token);
		userDto = UserDtoMapper.mapUserToDto(user);
	}

	@AfterEach
	public void resetMocks() {
		Mockito.reset(tokenRepository, userService);
	}

	@Test
	void saveOrUpdateExistingToken() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.ofNullable(token));
		when(tokenRepository.save(anyObject())).thenReturn(token);
		when(userService.getById(anyLong())).thenReturn(userDto);
		Assertions.assertEquals(token.getId(), refreshTokenService.saveOrUpdate(tokenDto));
	}

	@Test
	void saveOrUpdateNewToken() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.empty());
		when(tokenRepository.save(anyObject())).thenReturn(token);
		when(userService.getById(anyLong())).thenReturn(userDto);
		Assertions.assertEquals(token.getId(), refreshTokenService.saveOrUpdate(tokenDto));
	}

	@Test
	void saveOrUpdateDuplicateToken() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.empty());
		when(tokenRepository.save(anyObject())).thenThrow(new DataIntegrityViolationException(""));
		when(userService.getById(anyLong())).thenReturn(UserDtoMapper.mapUserToDto(user));
		Assertions.assertThrows(
				DuplicateEntityException.class, () -> refreshTokenService.saveOrUpdate(tokenDto));
	}

	@Test
	void getById() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.ofNullable(token));
		Assertions.assertEquals(tokenDto, refreshTokenService.getByUserId(1));
	}

	@Test
	void findByNotExistingUserId() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(
				EntityNotExistException.class, () -> refreshTokenService.getByUserId(1));
	}

	@Test
	void getByUserEmail() {
		when(tokenRepository.findByUserIdEmail(anyString())).thenReturn(Optional.ofNullable(token));
		Assertions.assertEquals(tokenDto, refreshTokenService.getByUserEmail("e"));
	}

	@Test
	void getByNotExistingUserEmail() {
		when(tokenRepository.findByUserIdEmail(anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(
				EntityNotExistException.class, () -> refreshTokenService.getByUserEmail("e"));
	}

	@Test
	void getByUserId() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.ofNullable(token));
		Assertions.assertEquals(tokenDto, refreshTokenService.getByUserId(1));
	}

	@Test
	void getByNotExistingUserId() {
		when(tokenRepository.findByUserIdId(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(
				EntityNotExistException.class, () -> refreshTokenService.getByUserId(1));
	}

	@Test
	void getByTokenTest() {
		when(tokenRepository.findByToken(anyString())).thenReturn(Optional.ofNullable(token));
		Assertions.assertEquals(tokenDto, refreshTokenService.getByToken("1"));
	}

	@Test
	void getByNotExistingTokenTest() {
		when(tokenRepository.findByToken(anyString())).thenReturn(Optional.empty());
		Assertions.assertThrows(InvalidTokenException.class, () -> refreshTokenService.getByToken("1"));
	}
}

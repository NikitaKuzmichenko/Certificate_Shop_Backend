package com.epam.esm.web.controller;

import static com.epam.esm.web.exceptionhandler.ExceptionResponseCreator.*;

import com.epam.esm.dto.RefreshTokenDto;
import com.epam.esm.service.RefreshTokenService;
import com.epam.esm.service.UserService;
import com.epam.esm.web.security.token.jwt.JwtTokenManager;
import com.epam.esm.web.security.token.refresh.RefreshTokenManager;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

	@Autowired private RefreshTokenManager tokenManager;

	@Autowired private JwtTokenManager jwtTokenManager;

	@Autowired private RefreshTokenService tokenService;

	@Autowired private UserService userService;

	@PreAuthorize("permitAll()")
	@GetMapping(value = "refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> refreshToken(
			@RequestHeader(value = RefreshTokenManager.HEADER_NAME) String refreshToken, Locale locale) {
		if (refreshToken == null) {
			return badRequestResponse(locale);
		}

		RefreshTokenDto token = tokenService.getByToken(refreshToken);
		if (token == null) {
			return invalidTokenResponse(locale);
		}

		if (tokenManager.isTokenExpired(token)) {
			return tokenExpiredResponse(locale);
		}

		return ResponseEntity.status(HttpStatus.OK)
				.header(RefreshTokenManager.HEADER_NAME, tokenManager.refreshToken(refreshToken))
				.header(
						JwtTokenManager.HEADER_NAME,
						jwtTokenManager.createJwt(userService.getById(token.getUserId())))
				.build();
	}
}

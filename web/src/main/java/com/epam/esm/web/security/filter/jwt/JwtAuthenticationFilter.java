package com.epam.esm.web.security.filter.jwt;

import static com.epam.esm.web.exceptionhandler.ExceptionResponseCreator.unauthorizedResponse;

import com.epam.esm.service.security.entity.CustomUserDetails;
import com.epam.esm.web.security.entity.AuthenticationEntity;
import com.epam.esm.web.security.token.jwt.JwtTokenManager;
import com.epam.esm.web.security.token.refresh.RefreshTokenManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	private final JwtTokenManager jwtTokenManager;

	private final RefreshTokenManager refreshTokenManager;

	public JwtAuthenticationFilter(
			AuthenticationManager authenticationManager,
			JwtTokenManager jwtTokenManager,
			RefreshTokenManager refreshTokenManager) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenManager = jwtTokenManager;
		this.refreshTokenManager = refreshTokenManager;
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			AuthenticationEntity authenticationRequest =
					new ObjectMapper().readValue(request.getInputStream(), AuthenticationEntity.class);

			Authentication authentication =
					new UsernamePasswordAuthenticationToken(
							authenticationRequest.getEmail(), authenticationRequest.getPassword());

			return authenticationManager.authenticate(authentication);
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	protected void successfulAuthentication(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult)
			throws IOException, ServletException {
		CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();

		String jwt = jwtTokenManager.createJwt(user);
		String refreshToken = refreshTokenManager.generateToken(user.getUserId());

		response.setHeader(JwtTokenManager.HEADER_NAME, jwt);
		response.setHeader(RefreshTokenManager.HEADER_NAME, refreshToken);
	}

	@Override
	protected void unsuccessfulAuthentication(
			HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {

		ObjectMapper mapper = new ObjectMapper();
		ResponseEntity responseEntity = unauthorizedResponse(request.getLocale());
		response.setStatus(responseEntity.getStatusCodeValue());
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().print(mapper.writeValueAsString(responseEntity.getBody()));
		response.flushBuffer();
	}
}

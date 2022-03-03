package com.epam.esm.web;

import com.epam.esm.service.RefreshTokenService;
import com.epam.esm.web.configuration.ApplicationConfig;
import com.epam.esm.web.exceptionhandler.ExceptionResponseCreator;
import com.epam.esm.web.security.CustomSecurityConfig;
import com.epam.esm.web.security.PasswordEncoderConfig;
import com.epam.esm.web.security.failurehandler.CustomAccessDeniedHandler;
import com.epam.esm.web.security.failurehandler.JwtAuthenticationEntryPoint;
import com.epam.esm.web.security.token.jwt.JwtTokenManager;
import com.epam.esm.web.security.token.refresh.RefreshTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@Import({PasswordEncoderConfig.class, CustomSecurityConfig.class, ApplicationConfig.class})
public class GeneralTestConfiguration {

	@MockBean private UserDetailsService detailsService;

	@MockBean private RefreshTokenService refreshTokenService;

	@Bean
	public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		return new JwtAuthenticationEntryPoint();
	}

	@Bean
	public AccessDeniedHandler customAccessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	public JwtTokenManager jwtTokenManager() {
		return new JwtTokenManager("10", 10);
	}

	@Bean
	public RefreshTokenManager refreshTokenManager() {
		return new RefreshTokenManager(10);
	}

	@Bean
	public ExceptionResponseCreator exceptionResponseCreator(@Autowired MessageSource messageSource) {
		return new ExceptionResponseCreator(messageSource);
	}
}

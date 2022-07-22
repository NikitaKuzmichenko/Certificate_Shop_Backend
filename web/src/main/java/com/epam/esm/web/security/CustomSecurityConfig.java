package com.epam.esm.web.security;

import com.epam.esm.web.security.filter.exceprion.FilterChainExceptionHandler;
import com.epam.esm.web.security.filter.jwt.JwtAuthenticationFilter;
import com.epam.esm.web.security.filter.jwt.JwtTokenVerifier;
import com.epam.esm.web.security.token.jwt.JwtTokenManager;
import com.epam.esm.web.security.token.refresh.RefreshTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired private FilterChainExceptionHandler filterChainExceptionHandler;

	@Autowired private AccessDeniedHandler handler;

	@Autowired private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired private JwtTokenManager configuration;

	@Autowired private RefreshTokenManager refreshTokenManager;

	@Autowired private UserDetailsService detailsService;

	@Autowired private PasswordEncoder passwordEncoder;

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(detailsService);
		return provider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable().csrf().disable();

		http.authorizeRequests()
				.antMatchers(
						"/swagger-ui/**",
						"/v2/api-docs/**",
						"/configuration/security",
						"/webjar/**",
						"/swagger-resources/**")
				.permitAll();

		http.exceptionHandling()
				.accessDeniedHandler(handler)
				.authenticationEntryPoint(authenticationEntryPoint)
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
				.addFilter(
						new JwtAuthenticationFilter(
								authenticationManager(), configuration, refreshTokenManager))
				.addFilterBefore(new JwtTokenVerifier(configuration), JwtAuthenticationFilter.class);
	}
}

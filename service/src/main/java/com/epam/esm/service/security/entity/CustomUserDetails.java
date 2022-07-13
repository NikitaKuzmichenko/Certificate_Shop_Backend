package com.epam.esm.service.security.entity;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

// @EqualsAndHashCode
public class CustomUserDetails extends User {

	private long userId;

	public CustomUserDetails(
			long userId,
			String username,
			String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}

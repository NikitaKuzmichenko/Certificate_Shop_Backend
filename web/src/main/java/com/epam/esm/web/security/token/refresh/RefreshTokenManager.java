package com.epam.esm.web.security.token.refresh;

import com.epam.esm.dto.RefreshTokenDto;
import com.epam.esm.service.RefreshTokenService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:security/refreshToken.properties")
public class RefreshTokenManager {

	private long liveTime;

	public static final String HEADER_NAME = "REFRESH_TOKEN";

	@Autowired private RefreshTokenService service;

	public RefreshTokenManager(@Value("${liveTimeInSec}") long liveTime) {
		this.liveTime = liveTime;
	}

	public String generateToken(long userId) {
		RefreshTokenDto token = new RefreshTokenDto();
		token.setUserId(userId);
		token.setCreationDate(new Date());
		token.setExpirationDate(getExpirationDate(new Date()));
		token.setToken(UUID.randomUUID().toString());

		return service.saveOrUpdate(token) == null ? null : token.getToken();
	}

	public String refreshToken(String token) {
		RefreshTokenDto existingToken = service.getByToken(token);
		if (existingToken == null) {
			return null;
		}
		return generateToken(existingToken.getUserId());
	}

	public String refreshToken(RefreshTokenDto token) {
		if (token == null) {
			return null;
		}
		service.delete(token.getUserId());
		return generateToken(token.getUserId());
	}

	public boolean isTokenExpired(RefreshTokenDto token) {
		if (token == null) {
			return false;
		}
		return token.getExpirationDate().compareTo(Date.from(Instant.now())) < 0;
	}

	private Date getExpirationDate(Date date) {
		return Date.from(date.toInstant().plus(liveTime, ChronoUnit.SECONDS));
	}
}

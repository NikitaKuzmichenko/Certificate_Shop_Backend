package com.epam.esm.dto.mapper;

import com.epam.esm.dto.RefreshTokenDto;
import com.epam.esm.entity.RefreshToken;

public class RefreshTokenDtoMapper {
	private RefreshTokenDtoMapper() {}

	public static RefreshTokenDto mapRefreshTokenToDto(RefreshToken token) {
		if (token == null) {
			return null;
		}
		RefreshTokenDto dto = new RefreshTokenDto();
		dto.setId(token.getId());
		dto.setToken(token.getToken());
		dto.setExpirationDate(token.getExpirationDate());
		dto.setCreationDate(token.getCreationDate());
		if (token.getUserId() != null) {
			dto.setUserId(token.getUserId().getId());
		}
		return dto;
	}

	public static RefreshToken getRefreshTokenFromDto(RefreshTokenDto dto) {
		if (dto == null) {
			return null;
		}

		RefreshToken token = new RefreshToken();
		token.setId(dto.getId());
		token.setToken(dto.getToken());
		token.setExpirationDate(dto.getExpirationDate());
		token.setCreationDate(dto.getCreationDate());

		return token;
	}
}

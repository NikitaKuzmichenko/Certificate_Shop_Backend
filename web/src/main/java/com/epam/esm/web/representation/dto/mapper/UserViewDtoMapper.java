package com.epam.esm.web.representation.dto.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.web.representation.dto.UserViewDto;

public class UserViewDtoMapper {

	private UserViewDtoMapper() {}

	public static UserViewDto toViewDto(UserDto entity) {
		if (entity == null) {
			return null;
		}
		UserViewDto dto = new UserViewDto();
		dto.setEmail(entity.getEmail());
		dto.setId(entity.getId());
		return dto;
	}
}

package com.epam.esm.dto.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;
import java.util.stream.Collectors;

public class UserDtoMapper {

	private UserDtoMapper() {}

	public static UserDto mapUserToDto(User user) {
		if (user == null) {
			return null;
		}
		UserDto dto = new UserDto();
		dto.setEmail(user.getEmail());
		dto.setPassword(user.getPassword());
		dto.setId(user.getId());

		if (user.getRoles() != null) {
			dto.setRoles(
					user.getRoles().stream()
							.map(UserRoleDtoMapper::mapUserRoleToDto)
							.collect(Collectors.toSet()));
		}

		return dto;
	}

	public static User mapDtoToUser(UserDto user) {
		if (user == null) {
			return null;
		}
		User entity = new User();
		entity.setEmail(user.getEmail());
		entity.setPassword(user.getPassword());
		entity.setId(user.getId());

		if (user.getRoles() != null) {
			entity.setRoles(
					user.getRoles().stream()
							.map(UserRoleDtoMapper::mapDtoToUserRole)
							.collect(Collectors.toList()));
		}
		return entity;
	}
}

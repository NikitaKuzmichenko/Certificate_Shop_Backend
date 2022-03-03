package com.epam.esm.dto.mapper;

import com.epam.esm.dto.UserRoleDto;
import com.epam.esm.entity.Authorities;
import com.epam.esm.entity.UserRole;
import java.util.*;

public class UserRoleDtoMapper {

	public static UserRoleDto mapUserRoleToDto(UserRole entity) {
		if (entity == null) {
			return null;
		}
		UserRoleDto dto = new UserRoleDto();

		dto.setId(entity.getId());
		dto.setName(entity.getName());

		if (entity.getAuthorities() != null) {
			Set<String> authority = new HashSet<>();
			entity.getAuthorities().stream()
					.filter(Objects::nonNull)
					.forEach(e -> authority.add(e.getName()));
			dto.setAuthorities(authority);
		}

		return dto;
	}

	public static UserRole mapDtoToUserRole(UserRoleDto dto) {

		if (dto == null) {
			return null;
		}
		UserRole entity = new UserRole();

		entity.setId(dto.getId());
		entity.setName(dto.getName());

		if (dto.getAuthorities() != null) {
			List<Authorities> authorities = new LinkedList<>();
			dto.getAuthorities().stream()
					.filter(Objects::nonNull)
					.forEach(
							e -> {
								Authorities authority = new Authorities();
								authority.setName(e);
								authorities.add(authority);
							});
			entity.setAuthorities(authorities);
		}
		return entity;
	}
}

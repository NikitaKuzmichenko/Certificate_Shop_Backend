package com.epam.esm.web.representation.dto.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.web.representation.dto.TagViewDto;

public class TagViewDtoMapper {

	private TagViewDtoMapper() {}

	public static TagViewDto toViewDto(TagDto entity) {
		if (entity == null) {
			return null;
		}
		TagViewDto dto = new TagViewDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		return dto;
	}
}

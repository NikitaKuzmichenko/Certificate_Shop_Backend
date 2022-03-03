package com.epam.esm.dto.mapper;

import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Tag;

public class TagDtoMapper {

	private TagDtoMapper() {}

	public static TagDto mapTagToDto(Tag entity) {
		if (entity == null) {
			return null;
		}
		TagDto dto = new TagDto();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		return dto;
	}

	public static Tag getTagFromDto(TagDto dto) {
		if (dto == null) {
			return null;
		}

		Tag entity = new Tag();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		return entity;
	}
}

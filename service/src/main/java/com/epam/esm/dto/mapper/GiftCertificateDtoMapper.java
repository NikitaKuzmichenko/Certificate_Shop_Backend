package com.epam.esm.dto.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GiftCertificateDtoMapper {

	private GiftCertificateDtoMapper() {}

	public static GiftCertificateDto mapGiftCertificateToDto(GiftCertificate entity) {
		if (entity == null) {
			return null;
		}
		GiftCertificateDto dto = new GiftCertificateDto();

		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setPrice(entity.getPrice());
		dto.setDuration(entity.getDuration());
		dto.setCreationDate(entity.getCreationDate());
		dto.setLastUpdateDate(entity.getLastUpdateDate());

		Set<TagDto> tags = new HashSet<>();

		if (entity.getTags() != null) {
			tags = entity.getTags().stream().map(TagDtoMapper::mapTagToDto).collect(Collectors.toSet());
		}

		dto.setTags(tags);

		return dto;
	}

	public static GiftCertificate getGiftCertificateFromDto(GiftCertificateDto dto) {
		if (dto == null) {
			return null;
		}
		GiftCertificate entity = new GiftCertificate();

		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setDuration(dto.getDuration());
		entity.setCreationDate(dto.getCreationDate());
		entity.setLastUpdateDate(dto.getLastUpdateDate());

		List<Tag> tags = new ArrayList<>();
		if (dto.getTags() != null) {
			tags = dto.getTags().stream().map(TagDtoMapper::getTagFromDto).collect(Collectors.toList());
		}

		entity.setTags(tags);

		return entity;
	}
}

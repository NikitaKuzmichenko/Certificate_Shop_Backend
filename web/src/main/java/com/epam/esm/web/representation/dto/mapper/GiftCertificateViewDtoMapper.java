package com.epam.esm.web.representation.dto.mapper;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.web.representation.dto.GiftCertificateViewDto;
import com.epam.esm.web.representation.dto.TagViewDto;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GiftCertificateViewDtoMapper {

	private GiftCertificateViewDtoMapper() {}

	public static GiftCertificateViewDto toViewDto(GiftCertificateDto entity) {
		if (entity == null) {
			return null;
		}
		GiftCertificateViewDto dto = new GiftCertificateViewDto();

		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setDescription(entity.getDescription());
		dto.setPrice(entity.getPrice());
		dto.setDuration(entity.getDuration());
		dto.setCreationDate(entity.getCreationDate());
		dto.setLastUpdateDate(entity.getLastUpdateDate());

		Set<TagViewDto> tags = new HashSet<>();

		if (entity.getTags() != null) {
			tags = entity.getTags().stream().map(TagViewDtoMapper::toViewDto).collect(Collectors.toSet());
		}

		dto.setTags(tags);

		return dto;
	}
}

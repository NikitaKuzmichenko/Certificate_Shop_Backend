package com.epam.esm.service.implementation;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagDtoMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.compound.certificate.impl.function.ConfigurableFunction;
import com.epam.esm.repository.compound.certificate.impl.function.DescriptionPartCondition;
import com.epam.esm.repository.compound.certificate.impl.function.NamePartCondition;
import com.epam.esm.repository.compound.certificate.impl.function.TagContainingCondition;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import com.epam.esm.service.TagService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
* Service implementation of the {@code CertificateCriteriaBuilderService} interface. Implements all
* operations, and permits all elements, including {@code null}.
*/
public class CertificateCriteriaBuilderServiceImpl implements CertificateCriteriaBuilderService {

	@Autowired @Lazy private final TagService tagService;

	private final List<ConfigurableFunction> conditions;

	public CertificateCriteriaBuilderServiceImpl(TagService tagService) {
		this.tagService = tagService;
		conditions = new ArrayList<>();
	}

	@Override
	public CertificateCriteriaBuilderService addNamePartCondition(String namePart) {
		if (namePart != null) {
			conditions.add(new NamePartCondition(namePart));
		}
		return this;
	}

	@Override
	public CertificateCriteriaBuilderService addDescriptionPartCondition(String descriptionPart) {
		if (descriptionPart != null) {
			conditions.add(new DescriptionPartCondition(descriptionPart));
		}
		return this;
	}

	@Override
	public CertificateCriteriaBuilderService addTagContainingCondition(List<String> tagNames) {
		if (tagNames != null && !tagNames.isEmpty()) {
			List<Tag> tags = new ArrayList<>();
			for (String name : tagNames) {
				TagDto tempTag = tagService.getByName(name);
				tags.add(TagDtoMapper.getTagFromDto(tempTag));
			}
			conditions.add(new TagContainingCondition(tags));
		}
		return this;
	}

	@Override
	public List<ConfigurableFunction> getConditions() {
		return conditions;
	}
}

package com.epam.esm.service.implementation;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CertificateCriteriaBuilderServiceImplTest {
	@Mock public static final TagServiceImpl tagService = mock(TagServiceImpl.class);

	private CertificateCriteriaBuilderService service =
			new CertificateCriteriaBuilderServiceImpl(tagService);

	@BeforeEach
	void beforeEach() {
		service = new CertificateCriteriaBuilderServiceImpl(tagService);
	}

	@Test
	void DescriptionPartConditionTest() {
		String descPart = "part";
		service.addDescriptionPartCondition(descPart);
		Assertions.assertEquals(1, service.getConditions().size());
	}

	@Test
	void DescriptionPartConditionNullTest() {
		service.addDescriptionPartCondition(null);
		Assertions.assertEquals(0, service.getConditions().size());
	}

	@Test
	void NamePartConditionTest() {
		String namePart = "part";
		service.addNamePartCondition(namePart);
		Assertions.assertEquals(1, service.getConditions().size());
	}

	@Test
	void NamePartConditionNullTest() {
		service.addNamePartCondition(null);
		Assertions.assertEquals(0, service.getConditions().size());
	}

	@Test
	void TagContainingConditionTest() {
		TagDto tagDto = new TagDto();
		tagDto.setId(1);
		tagDto.setName("tagName");
		when(tagService.getByName(anyString())).thenReturn(tagDto);
		service.addTagContainingCondition(List.of("tag1", "tag2"));
		Assertions.assertEquals(1, service.getConditions().size());
	}

	@Test
	void TagContainingConditionNullTest() {
		service.addTagContainingCondition(null);
		Assertions.assertEquals(0, service.getConditions().size());
	}

	@Test
	void noConditionTest() {
		Assertions.assertEquals(0, service.getConditions().size());
	}

	@Test
	void severalConditionTest() {
		String part = "part";
		service.addNamePartCondition(part);
		service.addDescriptionPartCondition(part);
		Assertions.assertEquals(2, service.getConditions().size());
	}
}

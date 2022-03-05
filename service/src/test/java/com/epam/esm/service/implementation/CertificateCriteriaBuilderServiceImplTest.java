package com.epam.esm.service.implementation;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import com.epam.esm.service.implementation.CertificateCriteriaBuilderServiceImpl;
import com.epam.esm.service.implementation.TagServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class CertificateCriteriaBuilderServiceImplTest {
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
		Assertions.assertEquals(service.getConditions().size(), 1);
	}

	@Test
	void DescriptionPartConditionNullTest() {
		service.addDescriptionPartCondition(null);
		Assertions.assertEquals(service.getConditions().size(), 0);
	}

	@Test
	void NamePartConditionTest() {
		String namePart = "part";
		service.addNamePartCondition(namePart);
		Assertions.assertEquals(service.getConditions().size(), 1);
	}

	@Test
	void NamePartConditionNullTest() {
		service.addNamePartCondition(null);
		Assertions.assertEquals(service.getConditions().size(), 0);
	}

	@Test
	void TagContainingConditionTest() {
		TagDto tagDto = new TagDto();
		tagDto.setId(1);
		tagDto.setName("tagName");
		when(tagService.getByName(anyString())).thenReturn(tagDto);
		service.addTagContainingCondition(List.of("tag1", "tag2"));
		Assertions.assertEquals(service.getConditions().size(), 1);
	}

	@Test
	void TagContainingConditionNullTest() {
		service.addTagContainingCondition(null);
		Assertions.assertEquals(service.getConditions().size(), 0);
	}

	@Test
	void noConditionTest() {
		Assertions.assertEquals(service.getConditions().size(), 0);
	}

	@Test
	void severalConditionTest() {
		String part = "part";
		service.addNamePartCondition(part);
		service.addDescriptionPartCondition(part);
		Assertions.assertEquals(service.getConditions().size(), 2);
	}
}

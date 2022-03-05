package com.epam.esm.service.implementation;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.mapper.TagDtoMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.compound.tag.TagRepository;
import com.epam.esm.service.TagService;
import com.epam.esm.service.implementation.TagServiceImpl;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class TagServiceImplTest {

	@Mock private static final TagRepository tagRepository = mock(TagRepository.class);

	private static final TagService tagService = new TagServiceImpl(tagRepository);

	private static Tag testTag;

	@BeforeAll
	public static void initAll() {
		testTag = new Tag();
		testTag.setId(1);
		testTag.setName("testTag");
	}

	@Test
	void getTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testTag));
		Assertions.assertEquals(TagDtoMapper.mapTagToDto(testTag), tagService.getById(1));
	}

	@Test
	void getNotExistingTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(tagService.getById(1));
	}

	@Test
	void deleteTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testTag));
		Assertions.assertTrue(tagService.delete(1));
	}

	@Test
	void failedDeleteTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		Assertions.assertFalse(tagService.delete(1));
	}

	@Test
	void createTag() {
		when(tagRepository.save(anyObject())).thenReturn(testTag);
		Assertions.assertNotNull(tagService.create(TagDtoMapper.mapTagToDto(testTag)));
	}

	@Test
	void failedCreateTag() {
		when(tagRepository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		Assertions.assertThrows(
				DuplicateKeyException.class, () -> tagService.create(TagDtoMapper.mapTagToDto(testTag)));
	}

	@Test
	void getPopular() {
		when(tagRepository.getPopular()).thenReturn(testTag);
		Assertions.assertEquals(tagService.getPopular(), TagDtoMapper.mapTagToDto(testTag));
	}

	@Test
	void getAll() {
		List<Tag> tags = List.of(testTag, testTag);
		when(tagRepository.findAll()).thenReturn(tags);
		Assertions.assertEquals(
				tags.stream().map(TagDtoMapper::mapTagToDto).collect(Collectors.toList()),
				tagService.selectAll());
	}

	@Test
	void getAllWithLimitAndOffset() {
		List<Tag> tags = List.of(testTag, testTag);
		when(tagRepository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(tags));
		Assertions.assertEquals(
				tags.stream().map(TagDtoMapper::mapTagToDto).collect(Collectors.toList()),
				tagService.selectAll(2, 0));
	}
}

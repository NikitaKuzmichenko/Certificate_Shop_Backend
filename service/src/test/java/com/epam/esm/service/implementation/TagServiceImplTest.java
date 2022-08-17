package com.epam.esm.service.implementation;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagDtoMapper;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.repository.compound.tag.TagRepository;
import com.epam.esm.service.TagService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class TagServiceImplTest {

	@Mock private static final TagRepository tagRepository = mock(TagRepository.class);

	private static final TagService tagService = new TagServiceImpl(tagRepository);

	private static Tag testTag;

	private static TagDto testTagDto;

	@BeforeAll
	public static void initAll() {
		testTag = new Tag();
		testTag.setId(1);
		testTag.setName("testTag");

		testTagDto = TagDtoMapper.mapTagToDto(testTag);
	}

	@AfterEach
	public void resetMocks() {
		Mockito.reset(tagRepository);
	}

	@Test
	void getTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testTag));
		Assertions.assertEquals(testTagDto, tagService.getById(1));
	}

	@Test
	void getNotExistingTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotExistException.class, () -> tagService.getById(1));
	}

	@Test
	void createTag() {
		when(tagRepository.save(anyObject())).thenReturn(testTag);
		Assertions.assertEquals(testTag.getId(), tagService.create(testTagDto));
	}

	@Test
	void failedCreateTag() {
		when(tagRepository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		Assertions.assertThrows(DuplicateEntityException.class, () -> tagService.create(testTagDto));
	}

	@Test
	void deleteTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testTag));
		Assertions.assertDoesNotThrow(() -> tagService.delete(1));
	}

	@Test
	void DeleteNotExistingTag() {
		when(tagRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotExistException.class, () -> tagService.delete(1));
	}

	@Test
	void persistTags() {
		Set<Tag> tags = Set.of(testTag);
		Set<TagDto> dtos = Set.of(testTagDto);
		when(tagRepository.persistTags(anyCollection())).thenReturn(tags);
		Assertions.assertEquals(dtos, tagService.persistTags(dtos));
	}

	@Test
	void persistNullTags() {
		Set<Tag> tags = Set.of(testTag);
		Set<TagDto> dtos = Set.of();
		when(tagRepository.persistTags(anyCollection())).thenReturn(tags);
		Assertions.assertEquals(dtos, tagService.persistTags(null));
	}

	@Test
	void getPopular() {
		when(tagRepository.getPopular()).thenReturn(testTag);
		Assertions.assertEquals(testTagDto, tagService.getPopular());
	}

	@Test
	void getNotExistingPopular() {
		when(tagRepository.getPopular()).thenThrow(new NoResultException());
		Assertions.assertThrows(EntityNotExistException.class, tagService::getPopular);
	}

	@Test
	void getAllWithLimitAndOffset() {
		List<Tag> tags = List.of(testTag, testTag);
		when(tagRepository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(tags));
		Assertions.assertEquals(
				tags.stream().map(TagDtoMapper::mapTagToDto).collect(Collectors.toList()),
				tagService.getAll(1, 1));
	}

	@ParameterizedTest
	@CsvSource({"-1,-1", "-1,1", "1,-1"})
	void getAllUserWitchIncorrectLimitAndOffset(int limit, int offset) {
		List<Tag> tags = List.of(testTag, testTag);
		when(tagRepository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(tags));
		Assertions.assertThrows(BadInputException.class, () -> tagService.getAll(limit, offset));
	}
}

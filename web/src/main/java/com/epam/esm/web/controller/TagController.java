package com.epam.esm.web.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.service.implementation.TagServiceImpl;
import com.epam.esm.web.representation.assembler.TagRepresentationAssembler;
import com.epam.esm.web.representation.dto.collection.CollectionWrapper;
import com.epam.esm.web.representation.dto.mapper.TagViewDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequestMapping("/tags")
@RestController
public class TagController {

	public static final long DEFAULT_OFFSET = 0;
	public static final long DEFAULT_LIMIT = 10;

	@Autowired private TagServiceImpl service;

	@Autowired private TagRepresentationAssembler tagRepresentationAssembler;

	@PreAuthorize("hasAuthority('READ_ALL')")
	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTag(@PathVariable("id") long id, Locale locale) {
		TagDto tag = service.getById(id);
		return ResponseEntity.status(HttpStatus.OK.value())
						.body(tagRepresentationAssembler.toModel(TagViewDtoMapper.toViewDto(tag)));
	}

	@PreAuthorize("hasAuthority('READ_ALL')")
	@GetMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPopularTag(Locale locale) {

		TagDto tag = service.getPopular();
		return ResponseEntity.status(HttpStatus.OK.value())
						.body(tagRepresentationAssembler.toModel(TagViewDtoMapper.toViewDto(tag)));
	}

	@PreAuthorize("hasAuthority('READ_ALL')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getTags(
			@RequestParam(required = false) Long offset,
			@RequestParam(required = false) Long limit,
			Locale locale) {

		if (offset == null) {
			offset = DEFAULT_OFFSET;
		}
		if (limit == null) {
			limit = DEFAULT_LIMIT;
		}

		List<TagDto> tags = service.getAll(limit, offset);

		CollectionWrapper<CollectionModel> result = new CollectionWrapper<>();
		result.setCollection(
				tagRepresentationAssembler
						.toCollectionModel(
								tags.stream().map(TagViewDtoMapper::toViewDto).collect(Collectors.toList()))
						.add(tagRepresentationAssembler.getLinksForGetAll(limit, offset)));
		result.setCollectionSize(tags.size());
		result.setLimit(limit);
		result.setOffset(offset);

		return ResponseEntity.status(HttpStatus.OK.value()).body(result);
	}

	@PreAuthorize("hasAuthority('WRITE_ALL')")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createTag(@RequestBody TagDto tag, Locale locale) {
		Long id = service.create(tag);

		return ResponseEntity.status(HttpStatus.CREATED.value())
						.body(tagRepresentationAssembler.getLinksForCreate(id));
	}

	@PreAuthorize("hasAuthority('MODIFY_ALL')")
	@DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, value = "{id}")
	public ResponseEntity<?> deleteTag(@PathVariable("id") long id, Locale locale) {
		service.delete(id);

		return ResponseEntity.status(HttpStatus.OK.value())
						.body(tagRepresentationAssembler.getLinksForDelete(id));
	}
}

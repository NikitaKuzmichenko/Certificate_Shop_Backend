package com.epam.esm.web.controller;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserRoleDto;
import com.epam.esm.service.UserService;
import com.epam.esm.web.representation.assembler.UserRepresentationAssembler;
import com.epam.esm.web.representation.dto.collection.CollectionWrapper;
import com.epam.esm.web.representation.dto.mapper.UserViewDtoMapper;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RestController
public class UserController {

	public static final long DEFAULT_OFFSET = 0;
	public static final long DEFAULT_LIMIT = 10;

	private static final Set<UserRoleDto> DEFAULT_ROLES = Set.of(new UserRoleDto("USER"));

	@Autowired private UserService userService;

	@Autowired private UserRepresentationAssembler userRepresentationAssembler;

	@PreAuthorize("hasAuthority('READ_ALL') and #id == authentication.principal")
	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get user")
	public ResponseEntity<?> getUser(@PathVariable("id") long id, Locale locale) {
		UserDto user = userService.getById(id);
		return ResponseEntity.status(HttpStatus.OK.value())
				.body(userRepresentationAssembler.toModel(UserViewDtoMapper.toViewDto(user)));
	}

	@PreAuthorize("hasAuthority('READ_ALL')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get collection of users")
	public ResponseEntity<?> getUsers(
			@RequestParam(required = false) Long offset,
			@RequestParam(required = false) Long limit,
			Locale locale) {

		if (offset == null) {
			offset = DEFAULT_OFFSET;
		}
		if (limit == null) {
			limit = DEFAULT_LIMIT;
		}

		List<UserDto> users = userService.getAll(limit, offset);

		CollectionWrapper<CollectionModel> result = new CollectionWrapper<>();
		result.setCollection(
				userRepresentationAssembler
						.toCollectionModel(
								users.stream().map(UserViewDtoMapper::toViewDto).collect(Collectors.toList()))
						.add(userRepresentationAssembler.getLinksForGetAll(limit, offset)));

		result.setCollectionSize(users.size());
		result.setLimit(limit);
		result.setOffset(offset);

		return ResponseEntity.status(HttpStatus.OK.value()).body(result);
	}

	@PreAuthorize("permitAll()")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "create guest user")
	public ResponseEntity<?> createDefaultUsers(@RequestBody UserDto user, Locale locale) {
		user.setRoles(DEFAULT_ROLES);
		Long id = userService.createAndEncodePassword(user);
		return ResponseEntity.status(HttpStatus.CREATED.value())
				.body(userRepresentationAssembler.getLinksForCreate(id));
	}
}

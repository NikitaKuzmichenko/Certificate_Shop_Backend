package com.epam.esm.service.implementation;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.User;
import com.epam.esm.pagination.OffsetLimitPage;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.UserRoleRepository;
import com.epam.esm.service.UserService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
* Service implementation of the {@code UserService} interface. Implements all operations, and
* permits all elements, including {@code null}. This implementation works with {@code
* UserRepository}. This implementation works with {@code OrderServiceImpl}.
*/
@Service
@Validated
public class UserServiceImpl implements UserService {

	@Autowired private final UserRepository repository;

	@Autowired private final UserRoleRepository roleRepository;

	@Autowired private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(
			UserRepository repository,
			UserRoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Long create(@Valid UserDto user) {
		User savedUser = UserDtoMapper.mapDtoToUser(user);
		if (user.getRoles() != null && !user.getRoles().isEmpty()) {
			savedUser.setRoles(
					user.getRoles().stream()
							.filter(Objects::nonNull)
							.map(e -> roleRepository.findByName(e.getName()))
							.filter(Optional::isPresent)
							.map(Optional::get)
							.collect(Collectors.toList()));
		}
		return repository.save(savedUser).getId();
	}

	@Override
	public Long createAndEncodePassword(@Valid UserDto user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return create(user);
	}

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this database
	*/
	@Override
	public UserDto getById(long id) {
		return UserDtoMapper.mapUserToDto(repository.findById(id).orElse(null));
	}

	@Override
	public UserDto getByEmail(String email) {
		if (email == null) {
			return null;
		}
		return UserDtoMapper.mapUserToDto(repository.findByEmail(email).orElse(null));
	}

	/** @return elements in this database, if database empty return empty list */
	@Override
	public List<UserDto> getAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(UserDtoMapper::mapUserToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	@Override
	public List<UserDto> getAll(long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new IllegalArgumentException("limit or offset is negative");
		}

		return repository.findAll(new OffsetLimitPage((int) limit, (int) offset)).toList().stream()
				.map(UserDtoMapper::mapUserToDto)
				.collect(Collectors.toList());
	}
}

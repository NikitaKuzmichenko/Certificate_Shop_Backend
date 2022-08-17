package com.epam.esm.service.implementation;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.User;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.pagination.OffsetLimitPage;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.UserRoleRepository;
import com.epam.esm.service.UserService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
		try {
			return repository.save(savedUser).getId();
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public Long createAndEncodePassword(@Valid UserDto user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return create(user);
	}

	@Override
	public UserDto getById(long id) {
		return UserDtoMapper.mapUserToDto(
				repository.findById(id).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public UserDto getByEmail(String email) {
		if (email == null) {
			throw new BadInputException();
		}
		return UserDtoMapper.mapUserToDto(
				repository.findByEmail(email).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public List<UserDto> getAll(long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new BadInputException();
		}
		return repository.findAll(new OffsetLimitPage((int) limit, (int) offset)).toList().stream()
				.map(UserDtoMapper::mapUserToDto)
				.collect(Collectors.toList());
	}
}

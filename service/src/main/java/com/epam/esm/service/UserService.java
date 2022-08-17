package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Service;

/** An ordered service interface to work with {@code User} class. */
@Service
public interface UserService {

	/**
	* @param user entity to be appended to repository
	* @return PK of inserted element
	* @throws DuplicateEntityException if entity with this email already exist in repository
	*/
	Long create(@Valid UserDto user);

	/**
	* @param user entity to be appended to repository
	* @return PK of inserted element
	* @throws DuplicateEntityException if entity with this email already exist in repository
	*/
	Long createAndEncodePassword(@Valid UserDto user);

	/**
	* @param id PK of the entity to return
	* @return element with the specified PK in this database
	* @throws EntityNotExistException if entity with PK do not exist in repository
	*/
	UserDto getById(long id);

	/**
	* @param email email of the entity to return
	* @return element with the specified email in this database
	* @throws BadInputException email is {@code null}
	*/
	UserDto getByEmail(String email);

	/**
	* @param limit amount of elements in result list
	* @param offset amount of elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws BadInputException if limit or offset is negative
	*/
	List<UserDto> getAll(long limit, long offset);
}

package com.epam.esm.service;

import com.epam.esm.dto.UserDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Service;

/** An ordered service interface to work with {@code User} class. */
@Service
public interface UserService {

	/**
	* @param user element to be appended to repository
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	Long create(@Valid UserDto user);

	/**
	* @param user element to be appended to repository
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	Long createAndEncodePassword(@Valid UserDto user);

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this database
	*/
	UserDto getById(long id);

	/**
	* @param email email of the element to return
	* @return the element with the specified PK in this database
	*/
	UserDto getByEmail(String email);

	/** @return elements in this database, if database empty return empty list */
	List<UserDto> getAll();

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	List<UserDto> getAll(long limit, long offset);
}

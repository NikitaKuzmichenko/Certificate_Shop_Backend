package com.epam.esm.service;

import com.epam.esm.dto.RefreshTokenDto;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import javax.validation.Valid;

public interface RefreshTokenService {
	/**
	* @param token entity to be appended to repository
	* @return PK of inserted element, or {@code null} if value can not be inserted
	* @throws DuplicateEntityException if entity with this token already exist in repository
	*/
	Long saveOrUpdate(@Valid RefreshTokenDto token);

	/**
	* @param id PK of the entity to return
	* @return the element with the specified PK in this repository
	* @throws EntityNotExistException if entity with name do not exist in repository
	*/
	RefreshTokenDto getById(long id);

	/**
	* @param email entity of the {@code User} class
	* @return the entity with the specified PK in this repository
	* @throws BadInputException if email is {@code null}
	* @throws EntityNotExistException if entity with name do not exist in repository
	*/
	RefreshTokenDto getByUserEmail(String email);

	/**
	* @param id entity of the {@code User} class
	* @return entity with the specified PK in this repository
	* @throws EntityNotExistException if entity with this id do not exist in repository
	*/
	RefreshTokenDto getByUserId(long id);

	/**
	* @param token of the element to return
	* @return the element with the specified PK in this repository
	* @throws BadInputException if token is {@code null}
	*/
	RefreshTokenDto getByToken(String token);

	/**
	* @param id if repository containing element with this id - it will be removed from this
	*     repository
	* @throws EntityNotExistException if entity with this id do not exist in repository
	*/
	void delete(long id);
}

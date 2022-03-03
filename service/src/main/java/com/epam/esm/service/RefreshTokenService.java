package com.epam.esm.service;

import com.epam.esm.dto.RefreshTokenDto;
import javax.validation.Valid;

public interface RefreshTokenService {
	/**
	* @param token element to be appended to repository
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	Long saveOrUpdate(@Valid RefreshTokenDto token);

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this repository
	*/
	RefreshTokenDto getById(long id);

	/**
	* @param email of the {@code User} class
	* @return the element with the specified PK in this repository
	*/
	RefreshTokenDto getByUserEmail(String email);

	/**
	* @param id of the {@code User} class
	* @return the element with the specified PK in this repository
	*/
	RefreshTokenDto getByUserId(long id);

	/**
	* @param token of the element to return
	* @return the element with the specified PK in this repository
	*/
	RefreshTokenDto getByToken(String token);

	/**
	* @param id if repository containing element with this id - it will be removed from this
	*     repository
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	boolean delete(long id);
}

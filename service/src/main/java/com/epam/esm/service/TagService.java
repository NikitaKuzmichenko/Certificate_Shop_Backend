package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/** An ordered service interface to work with {@code TagRepository} class. */
@Service
@Validated
public interface TagService {

	/**
	* @param tag entity to be appended to repository
	* @return PK of inserted entity, or {@code null} if value can not be inserted
	* @throws DuplicateEntityException if entity with this name already exist in repository
	*/
	Long create(@Valid TagDto tag);

	/**
	* @param id PK of the entity to return
	* @return entity with the specified PK in this repository
	* @throws EntityNotExistException if entity with PK do not exist in repository
	*/
	TagDto getById(long id);

	/**
	* @param name name of the entity to return
	* @return entity with the specified PK in this repository
	* @throws EntityNotExistException if entity with name do not exist in repository
	*/
	TagDto getByName(String name);

	/**
	* @param id if repository containing entity with this id - it will be removed from this
	*     repository
	* @throws EntityNotExistException if entity with this id do not exist in repository
	*/
	void delete(long id);

	/**
	* Reattach tags from this repository to gift certificate with given id
	*
	* @param tags tags will be reattached to this element, if existing else they will be created. If
	*     {@code null} or empty function will return empty set.
	* @return persisted tags.
	*/
	Set<TagDto> persistTags(Set<@Valid TagDto> tags);

	/**
	* @param limit amount of elements in result list
	* @param offset amount of elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws BadInputException if limit or offset is negative
	*/
	List<TagDto> getAll(long limit, long offset);

	/** @return Get the most widely used tag of a user with the highest cost of all orders */
	TagDto getPopular();
}

package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
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
	* @param tag element to be appended to repository
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	Long create(@Valid TagDto tag);

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this repository
	*/
	TagDto getById(long id);

	/**
	* @param name name of the element to return
	* @return the element with the specified PK in this repository
	*/
	TagDto getByName(String name);

	/**
	* @param id if repository containing element with this id - it will be removed from this
	*     repository
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	boolean delete(long id);

	/**
	* Reattach tags from this repository to gift certificate with given id
	*
	* @param tags tags will be reattached to this element, if existing else they will be created
	*/
	Set<TagDto> persistTags(Set<@Valid TagDto> tags);

	/** @return all element in this database, if database empty return empty list */
	List<TagDto> selectAll();

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	List<TagDto> selectAll(long limit, long offset);

	/** @return Get the most widely used tag of a user with the highest cost of all orders */
	TagDto getPopular();
}

package com.epam.esm.service.implementation;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagDtoMapper;
import com.epam.esm.pagination.OffsetLimitPage;
import com.epam.esm.repository.compound.tag.TagRepository;
import com.epam.esm.service.TagService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
* Service implementation of the {@code TagService} interface. Implements all operations, and
* permits all elements, including {@code null}. This implementation works with {@code
* TagRepository}.
*/
@Service
@Validated
public class TagServiceImpl implements TagService {

	private final @Autowired TagRepository repository;

	public TagServiceImpl(TagRepository repository) {
		this.repository = repository;
	}

	/**
	* @param tag element to be appended to repository
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	@Override
	public Long create(@Valid TagDto tag) {
		return TagDtoMapper.mapTagToDto(repository.save(TagDtoMapper.getTagFromDto(tag))).getId();
	}

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this repository
	*/
	@Override
	public TagDto getById(long id) {
		return TagDtoMapper.mapTagToDto(repository.findById(id).orElse(null));
	}

	/**
	* @param name name of the element to return
	* @return the element with the specified PK in this repository
	*/
	@Override
	public TagDto getByName(String name) {
		return name == null ? null : TagDtoMapper.mapTagToDto(repository.findByName(name).orElse(null));
	}

	/**
	* @param id if repository containing element with this id - it will be removed from this
	*     repository
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	@Override
	public boolean delete(long id) {
		TagDto tag = getById(id);
		if (tag == null) {
			return false;
		}
		repository.delete(TagDtoMapper.getTagFromDto(tag));
		return true;
	}

	/**
	* Reattach tags from this repository to gift certificate with given id
	*
	* @param tags tags will be reattached to this element, if existing else they will be created
	*/
	@Override
	public Set<TagDto> persistTags(Set<@Valid TagDto> tags) {
		return repository
				.persistTags(tags.stream().map(TagDtoMapper::getTagFromDto).collect(Collectors.toSet()))
				.stream()
				.map(TagDtoMapper::mapTagToDto)
				.collect(Collectors.toSet());
	}

	/** @return Get the most widely used tag of a user with the highest cost of all orders */
	@Override
	public TagDto getPopular() {
		return TagDtoMapper.mapTagToDto(repository.getPopular());
	}

	/** @return all element in this database, if database empty return empty list */
	@Override
	public List<TagDto> selectAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(TagDtoMapper::mapTagToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	@Override
	public List<TagDto> selectAll(long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new IllegalArgumentException("limit or offset is negative");
		}
		return repository.findAll(new OffsetLimitPage((int) limit, (int) offset)).stream()
				.map(TagDtoMapper::mapTagToDto)
				.collect(Collectors.toList());
	}
}

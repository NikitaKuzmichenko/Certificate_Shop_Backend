package com.epam.esm.service.implementation;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.TagDtoMapper;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.pagination.OffsetLimitPage;
import com.epam.esm.repository.compound.tag.TagRepository;
import com.epam.esm.service.TagService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.NoResultException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

	@Override
	public Long create(@Valid TagDto tag) {
		try {
			return TagDtoMapper.mapTagToDto(repository.save(TagDtoMapper.getTagFromDto(tag))).getId();
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public TagDto getById(long id) {
		return TagDtoMapper.mapTagToDto(
				repository.findById(id).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public TagDto getByName(String name) {
		if (name == null) {
			throw new BadInputException();
		}
		return TagDtoMapper.mapTagToDto(
				repository.findByName(name).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public void delete(long id) {
		TagDto tag = getById(id);
		repository.delete(TagDtoMapper.getTagFromDto(tag));
	}

	@Override
	public Set<TagDto> persistTags(Set<@Valid TagDto> tags) {
		if(tags == null || tags.isEmpty()){
			return new HashSet<>();
		}
		return repository
				.persistTags(tags.stream().map(TagDtoMapper::getTagFromDto).collect(Collectors.toSet()))
				.stream()
				.map(TagDtoMapper::mapTagToDto)
				.collect(Collectors.toSet());
	}

	@Override
	public TagDto getPopular() {
		try {
			return TagDtoMapper.mapTagToDto(repository.getPopular());
		} catch (NoResultException e) {
			throw new EntityNotExistException();
		}
	}

	@Override
	public List<TagDto> getAll(long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new BadInputException();
		}
		return repository.findAll(new OffsetLimitPage((int) limit, (int) offset)).stream()
				.map(TagDtoMapper::mapTagToDto)
				.collect(Collectors.toList());
	}
}

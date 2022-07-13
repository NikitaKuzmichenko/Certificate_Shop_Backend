package com.epam.esm.service.implementation;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.GiftCertificateDtoMapper;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.pagination.OffsetLimitPage;
import com.epam.esm.repository.compound.certificate.GiftCertificateRepository;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
* Service implementation of the {@code GiftCertificateService} interface. Implements all
* operations, and permits all elements, including {@code null}. This implementation works with
* {@code CustomGiftCertificateRepository}. This implementation works with {@code TagService}.
*/
@Service
@Validated
public class GiftCertificateServiceImpl implements GiftCertificateService {

	@Autowired private final GiftCertificateRepository repository;

	@Autowired private final TagService tagService;

	public GiftCertificateServiceImpl(GiftCertificateRepository repository, TagService tagService) {
		this.repository = repository;
		this.tagService = tagService;
	}

	@Override
	public GiftCertificateDto getById(long id) {
		return GiftCertificateDtoMapper.mapGiftCertificateToDto(
				repository.findById(id).orElseThrow(EntityNotExistException::new));
	}

	@Override
	public Long create(@Valid GiftCertificateDto giftCertificate) {
		giftCertificate.setTags(tagService.persistTags(giftCertificate.getTags()));
		try {
			return repository
					.save(GiftCertificateDtoMapper.getGiftCertificateFromDto(giftCertificate))
					.getId();
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public Long patch(@Valid GiftCertificateDto giftCertificate) {
		getById(giftCertificate.getId());
		return create(giftCertificate);
	}

	@Override
	public Long patchOrCreate(GiftCertificateDto giftCertificate) {
		giftCertificate.setTags(tagService.persistTags(giftCertificate.getTags()));
		try {
			return repository
					.save(GiftCertificateDtoMapper.getGiftCertificateFromDto(giftCertificate))
					.getId();
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public void delete(long id) {
		repository.delete(GiftCertificateDtoMapper.getGiftCertificateFromDto(getById(id)));
	}

	private void paginationCheck(long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new BadInputException();
		}
	}

	@Override
	public List<GiftCertificateDto> getAll(long limit, long offset) {
		paginationCheck(limit, offset);

		return repository.findAll(new OffsetLimitPage((int) limit, (int) offset)).stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<GiftCertificateDto> getAll(
			CertificateCriteriaBuilderService builder, long limit, long offset) {
		paginationCheck(limit, offset);

		if (builder == null || builder.getConditions().isEmpty()) {
			return getAll(limit, offset);
		}

		return repository.getAll(builder.getConditions(), limit, offset).stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<GiftCertificateDto> getAll(
			String sortedColumn, boolean sortDirection, long limit, long offset) {
		paginationCheck(limit, offset);

		if (sortedColumn == null) {
			return getAll(limit, offset);
		}

		return repository.getAll(sortedColumn, sortDirection, limit, offset).stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<GiftCertificateDto> getAll(
			CertificateCriteriaBuilderService builder,
			String sortedColumn,
			boolean sortDirection,
			long limit,
			long offset) {

		if (sortedColumn == null) {
			return getAll(builder, limit, offset);
		}

		if (builder == null || builder.getConditions().isEmpty()) {
			return getAll(sortedColumn, sortDirection, limit, offset);
		}

		paginationCheck(limit, offset);

		return repository
				.getAll(builder.getConditions(), sortedColumn, sortDirection, limit, offset)
				.stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	@Override
	public GiftCertificateDto replaceAllNotNullParams(
			GiftCertificateDto original, GiftCertificateDto updated) {

		if (original == null || updated == null) {
			return null;
		}

		original.setId(updated.getId());

		Integer duration = updated.getDuration();
		if (duration != null) {
			original.setDuration(duration);
		}

		String string = updated.getDescription();
		if (string != null) {
			original.setDescription(string);
		}

		string = updated.getName();
		if (string != null) {
			original.setName(string);
		}

		BigDecimal price = updated.getPrice();
		if (price != null) {
			original.setPrice(price);
		}

		ZonedDateTime time = updated.getCreationDate();
		if (time != null) {
			original.setCreationDate(time);
		}

		time = updated.getLastUpdateDate();
		if (time != null) {
			original.setLastUpdateDate(time);
		}

		Set<TagDto> tags = updated.getTags();
		if (tags != null) {
			original.setTags(tags);
		}

		return original;
	}
}

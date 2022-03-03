package com.epam.esm.service.implementation;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.mapper.GiftCertificateDtoMapper;
import com.epam.esm.pagination.OffsetLimitPage;
import com.epam.esm.repository.compound.certificate.GiftCertificateRepository;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.TagService;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this database
	*/
	@Override
	public GiftCertificateDto getById(long id) {
		return GiftCertificateDtoMapper.mapGiftCertificateToDto(repository.findById(id).orElse(null));
	}

	/**
	* @param giftCertificate element to be appended to database
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	@Override
	public Long create(@Valid GiftCertificateDto giftCertificate) {
		giftCertificate.setTags(tagService.persistTags(giftCertificate.getTags()));
		return repository
				.save(GiftCertificateDtoMapper.getGiftCertificateFromDto(giftCertificate))
				.getId();
	}

	/**
	* @param giftCertificate element to be appended to database
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	@Override
	public boolean replace(@Valid GiftCertificateDto giftCertificate) {
		if (getById(giftCertificate.getId()) == null) {
			return false;
		}
		giftCertificate.setTags(tagService.persistTags(giftCertificate.getTags()));
		repository.save(GiftCertificateDtoMapper.getGiftCertificateFromDto(giftCertificate));
		return true;
	}

	/**
	* @param id if database containing element with this id - it will be removed from this database
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	@Override
	public boolean delete(long id) {
		GiftCertificateDto certificateDto = getById(id);
		if (certificateDto == null) {
			return false;
		}
		repository.delete(GiftCertificateDtoMapper.getGiftCertificateFromDto(certificateDto));
		return true;
	}

	/** @return all element in this database, if database empty return empty list */
	@Override
	public List<GiftCertificateDto> selectAll() {
		return StreamSupport.stream(repository.findAll().spliterator(), false)
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	private void paginationCheck(long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new IllegalArgumentException("limit or offset is negative");
		}
	}

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	@Override
	public List<GiftCertificateDto> selectAll(long limit, long offset) {
		paginationCheck(limit, offset);

		return repository.findAll(new OffsetLimitPage((int) limit, (int) offset)).stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param builder filled condition builder
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	@Override
	public List<GiftCertificateDto> selectAll(
			CertificateCriteriaBuilderService builder, long limit, long offset) {
		paginationCheck(limit, offset);

		if (builder == null || builder.getConditions().isEmpty()) {
			return selectAll(limit, offset);
		}

		return repository.getAll(builder.getConditions(), limit, offset).stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param sortedColumn column according to which performed sort
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	@Override
	public List<GiftCertificateDto> selectAll(
			String sortedColumn, boolean sortDirection, long limit, long offset) {
		paginationCheck(limit, offset);

		if (sortedColumn == null) {
			return selectAll(limit, offset);
		}

		return repository.getAll(sortedColumn, sortDirection, limit, offset).stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param sortedColumn column according to which performed sort
	* @param builder filled condition builder
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	@Override
	public List<GiftCertificateDto> selectAll(
			CertificateCriteriaBuilderService builder,
			String sortedColumn,
			boolean sortDirection,
			long limit,
			long offset) {
		paginationCheck(limit, offset);

		if (sortedColumn == null) {
			return selectAll(builder, limit, offset);
		}

		if (builder == null || builder.getConditions().isEmpty()) {
			return selectAll(sortedColumn, sortDirection, limit, offset);
		}

		return repository
				.getAll(builder.getConditions(), sortedColumn, sortDirection, limit, offset)
				.stream()
				.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
				.collect(Collectors.toList());
	}

	/** Changing time zone of a ZonedDateTime */
	private ZonedDateTime changeTimeZone(ZonedDateTime currentTime, TimeZone newTimeZone) {
		return currentTime.toLocalDateTime().atZone(newTimeZone.toZoneId());
	}

	/**
	* Changing time zone of a giftCertificate
	*
	* @param giftCertificate giftCertificate to change time zone in
	* @param newTimeZone new time zone
	*/
	@Override
	public void changeTimeZone(GiftCertificateDto giftCertificate, TimeZone newTimeZone) {
		if (giftCertificate == null || newTimeZone == null) {
			return;
		}

		giftCertificate.setCreationDate(changeTimeZone(giftCertificate.getCreationDate(), newTimeZone));
		giftCertificate.setLastUpdateDate(
				changeTimeZone(giftCertificate.getLastUpdateDate(), newTimeZone));
	}

	/**
	* Changing time zone of a giftCertificates list
	*
	* @param giftCertificates giftCertificates to change time zone in
	* @param newTimeZone new time zone
	*/
	@Override
	public void changeTimeZone(List<GiftCertificateDto> giftCertificates, TimeZone newTimeZone) {
		if (giftCertificates == null || newTimeZone == null) {
			return;
		}

		for (GiftCertificateDto giftCertificate : giftCertificates) {
			if (giftCertificate == null) {
				continue;
			}
			changeTimeZone(giftCertificate, newTimeZone);
		}
	}

	/**
	* @param original original exemplar of {@code GiftCertificateDto} class
	* @param updated exemplar of {@code GiftCertificateDto} class, with not null fields, that will be
	*     transferred to original
	* @return result of merging original and updated
	*/
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

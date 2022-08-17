package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/** An ordered service interface to work with {@code CustomGiftCertificateRepository} class. */
@Service
@Validated
public interface GiftCertificateService {

	/**
	* @param id PK of the entity to return
	* @return entity with the specified PK in this database
	* @throws EntityNotExistException if entity with this id do not exist in repository
	*/
	GiftCertificateDto getById(long id);

	/**
	* @param giftCertificate entity to be appended to database
	* @return PK of inserted entity
	* @throws DuplicateEntityException if entity with this name already exist in repository
	*/
	Long create(@Valid GiftCertificateDto giftCertificate);

	/**
	* @param giftCertificate element to be patched in database
	* @throws DuplicateEntityException if entity with this name already exist in repository
	*/
	Long patch(@Valid GiftCertificateDto giftCertificate);

	/**
	* @param giftCertificate entity to be patched or created in database
	* @throws DuplicateEntityException if entity with this name already exist in repository
	*/
	Long patchOrCreate(@Valid GiftCertificateDto giftCertificate);

	/**
	* @param id if database containing entity with this id - it will be removed from this database
	* @throws EntityNotExistException if entity with this id do not exist in repository
	*/
	void delete(long id);

	/**
	* @param limit amount of elementsin result list
	* @param offset amount of elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws BadInputException if limit or offset is negative
	*/
	List<GiftCertificateDto> getAll(long limit, long offset);

	/**
	* @param builder filled condition builder
	* @param limit amount of elements in result list
	* @param offset amount of elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws BadInputException if limit or offset is negative
	*/
	List<GiftCertificateDto> getAll(
			CertificateCriteriaBuilderService builder, long limit, long offset);

	/**
	* @param sortedColumn column according to which performed sort
	* @param sortDirection sorting direction
	* @param limit amount of elements in result list
	* @param offset amount of elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws BadInputException if limit or offset is negative
	*/
	List<GiftCertificateDto> getAll(
			String sortedColumn, boolean sortDirection, long limit, long offset);

	/**
	* @param sortedColumn column according to which performed sort
	* @param builder filled condition builder
	* @param sortDirection sorting direction
	* @param limit amount of elements in result list
	* @param offset amount of elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws BadInputException if limit or offset is negative
	*/
	List<GiftCertificateDto> getAll(
			CertificateCriteriaBuilderService builder,
			String sortedColumn,
			boolean sortDirection,
			long limit,
			long offset);

	/**
	* @param original original entity of {@code GiftCertificateDto} class
	* @param updated entity of {@code GiftCertificateDto} class, with not null fields, that will be
	*     transferred to original
	* @return result of merging original and updated
	*/
	GiftCertificateDto replaceAllNotNullParams(
			GiftCertificateDto original, GiftCertificateDto updated);
}

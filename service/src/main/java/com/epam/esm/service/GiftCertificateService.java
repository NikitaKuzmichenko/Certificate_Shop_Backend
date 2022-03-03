package com.epam.esm.service;

import com.epam.esm.dto.GiftCertificateDto;
import java.util.List;
import java.util.TimeZone;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/** An ordered service interface to work with {@code CustomGiftCertificateRepository} class. */
@Service
@Validated
public interface GiftCertificateService {

	/**
	* @param id PK of the element to return
	* @return the element with the specified PK in this database
	*/
	GiftCertificateDto getById(long id);

	/**
	* @param giftCertificate element to be appended to database
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	Long create(@Valid GiftCertificateDto giftCertificate);

	/**
	* @param giftCertificate element to be appended to database
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	boolean replace(@Valid GiftCertificateDto giftCertificate);

	/**
	* @param id if database containing element with this id - it will be removed from this database
	* @return {@code true} if operation vas successful, else return {@code false}
	*/
	boolean delete(long id);

	/** @return all element in this database, if database empty return empty list */
	List<GiftCertificateDto> selectAll();

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	List<GiftCertificateDto> selectAll(long limit, long offset);

	/**
	* @param builder filled condition builder
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	List<GiftCertificateDto> selectAll(
			CertificateCriteriaBuilderService builder, long limit, long offset);

	/**
	* @param sortedColumn column according to which performed sort
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	List<GiftCertificateDto> selectAll(
			String sortedColumn, boolean sortDirection, long limit, long offset);

	/**
	* @param sortedColumn column according to which performed sort
	* @param builder filled condition builder
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	List<GiftCertificateDto> selectAll(
			CertificateCriteriaBuilderService builder,
			String sortedColumn,
			boolean sortDirection,
			long limit,
			long offset);

	/**
	* Changing time zone of a giftCertificate
	*
	* @param giftCertificate giftCertificate to change time zone in
	* @param newTimeZone new time zone
	*/
	void changeTimeZone(GiftCertificateDto giftCertificate, TimeZone newTimeZone);

	/**
	* Changing time zone of a giftCertificates list
	*
	* @param giftCertificates giftCertificates to change time zone in
	* @param newTimeZone new time zone
	*/
	void changeTimeZone(List<GiftCertificateDto> giftCertificates, TimeZone newTimeZone);

	/**
	* @param original original exemplar of {@code GiftCertificateDto} class
	* @param updated exemplar of {@code GiftCertificateDto} class, with not null fields, that will be
	*     transferred to original
	* @return result of merging original and updated
	*/
	GiftCertificateDto replaceAllNotNullParams(
			GiftCertificateDto original, GiftCertificateDto updated);
}

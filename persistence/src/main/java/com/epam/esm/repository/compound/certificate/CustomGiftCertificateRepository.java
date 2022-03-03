package com.epam.esm.repository.compound.certificate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.compound.certificate.impl.function.ConfigurableFunction;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
* An ordered repository to work with{@code GiftCertificate} class. The user of this interface has
* precise control over database. The user can access elements by their integer index (PK of
* element).
*/
@Repository
public interface CustomGiftCertificateRepository {

	/**
	* @param searchParams parameters which restrict result
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws NullPointerException if the specified element is {@code null} and this
	*/
	List<GiftCertificate> getAll(List<ConfigurableFunction> searchParams, long limit, long offset);

	/**
	* @param sortedColumn column according to which performed sort
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws NullPointerException if the specified element is {@code null} and this
	*/
	List<GiftCertificate> getAll(String sortedColumn, boolean sortDirection, long limit, long offset);

	/**
	* @param sortedColumn column according to which performed sort
	* @param searchParams parameters which restrict result
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws NullPointerException if the specified element is {@code null} and this
	*/
	List<GiftCertificate> getAll(
			List<ConfigurableFunction> searchParams,
			String sortedColumn,
			boolean sortDirection,
			long limit,
			long offset);
}

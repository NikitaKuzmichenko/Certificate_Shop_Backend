package com.epam.esm.service;

import com.epam.esm.repository.compound.certificate.impl.function.ConfigurableFunction;
import java.util.List;
import org.springframework.stereotype.Component;

/** An ordered service interface to customise selection of {@code GiftCertificate} class. */
@Component
public interface CertificateCriteriaBuilderService {

	/**
	* @param namePart {@code GiftCertificate} entity name part
	* @return current state of {@code CertificateCriteriaBuilderService}
	*/
	CertificateCriteriaBuilderService addNamePartCondition(String namePart);

	/**
	* @param descriptionPart {@code GiftCertificate} entity description part
	* @return current state of {@code CertificateCriteriaBuilderService}
	*/
	CertificateCriteriaBuilderService addDescriptionPartCondition(String descriptionPart);

	/**
	* @param tagNames {@code GiftCertificate} entity must contain tag with this names
	* @return current state of {@code CertificateCriteriaBuilderService}
	*/
	CertificateCriteriaBuilderService addTagContainingCondition(List<String> tagNames);

	/** @return all conditions */
	List<ConfigurableFunction> getConditions();
}

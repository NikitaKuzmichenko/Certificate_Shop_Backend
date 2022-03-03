package com.epam.esm.web.controller;

import static com.epam.esm.web.exceptionhandler.ExceptionResponseCreator.badRequestResponse;
import static com.epam.esm.web.exceptionhandler.ExceptionResponseCreator.notFoundResponse;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import com.epam.esm.service.implementation.GiftCertificateServiceImpl;
import com.epam.esm.web.representation.assembler.GiftCertificateRepresentationAssembler;
import com.epam.esm.web.representation.dto.collection.CollectionWrapper;
import com.epam.esm.web.representation.dto.mapper.GiftCertificateViewDtoMapper;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/certificates")
@RestController
public class GiftCertificateController {

	public static final long DEFAULT_OFFSET = 0;
	public static final long DEFAULT_LIMIT = 10;

	@Autowired private GiftCertificateServiceImpl service;

	@Autowired private GiftCertificateRepresentationAssembler giftCertificateRepresentationAssembler;

	@Autowired @Lazy private CertificateCriteriaBuilderService builder;

	@PreAuthorize("hasAuthority('WRITE_ALL')")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createGiftCertificate(
			@RequestBody GiftCertificateDto certificate, Locale locale) {
		Long id = service.create(certificate);
		return id == null
				? badRequestResponse(locale)
				: ResponseEntity.status(HttpStatus.CREATED.value())
						.body(giftCertificateRepresentationAssembler.getLinksForCreate(id));
	}

	@PreAuthorize("hasAuthority('MODIFY_ALL')")
	@PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateGiftCertificates(
			@PathVariable("id") long id, @RequestBody GiftCertificateDto certificate, Locale locale) {

		if (certificate == null) {
			return badRequestResponse(locale);
		}

		certificate.setId(id);
		if (service.replace(certificate)) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(giftCertificateRepresentationAssembler.getLinksForUpdate(certificate));
		}

		Long createdId = service.create(certificate);
		return createdId == null
				? badRequestResponse(locale)
				: ResponseEntity.status(HttpStatus.CREATED)
						.body(giftCertificateRepresentationAssembler.getLinksForCreate(createdId));
	}

	@PreAuthorize("hasAuthority('MODIFY_ALL')")
	@PatchMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> patchGiftCertificates(
			@PathVariable("id") long id, @RequestBody GiftCertificateDto certificate, Locale locale) {
		if (certificate == null) {
			return badRequestResponse(locale);
		}
		GiftCertificateDto original = service.getById(id);
		if (original == null) {
			return notFoundResponse(locale);
		}

		certificate.setId(id);
		return !service.replace(service.replaceAllNotNullParams(original, certificate))
				? badRequestResponse(locale)
				: ResponseEntity.status(HttpStatus.OK)
						.body(giftCertificateRepresentationAssembler.getLinksForPatch(certificate));
	}

	@PreAuthorize("permitAll()")
	@GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getGiftCertificate(@PathVariable("id") long id, Locale locale) {
		GiftCertificateDto certificate = service.getById(id);

		if (certificate == null) {
			return notFoundResponse(locale);
		}

		return ResponseEntity.status(HttpStatus.OK.value())
				.body(
						giftCertificateRepresentationAssembler.toModel(
								GiftCertificateViewDtoMapper.toViewDto(certificate)));
	}

	@PreAuthorize("permitAll()")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAll(
			@RequestParam(required = false) String sortField,
			@RequestParam(required = false) boolean asc,
			@RequestParam(required = false) String nameFilter,
			@RequestParam(required = false) String descriptionFilter,
			@RequestParam(required = false) List<String> tagNamesFilter,
			@RequestParam(required = false) Long offset,
			@RequestParam(required = false) Long limit,
			Locale locale) {

		if (offset == null) {
			offset = DEFAULT_OFFSET;
		}
		if (limit == null) {
			limit = DEFAULT_LIMIT;
		}

		List<GiftCertificateDto> certificates =
				service.selectAll(
						builder
								.addNamePartCondition(nameFilter)
								.addDescriptionPartCondition(descriptionFilter)
								.addTagContainingCondition(tagNamesFilter),
						sortField,
						asc,
						limit,
						offset);

		if (certificates == null) {
			return badRequestResponse(locale);
		}

		CollectionWrapper<CollectionModel> result = new CollectionWrapper<>();
		result.setCollection(
				giftCertificateRepresentationAssembler
						.toCollectionModel(
								certificates.stream()
										.map(GiftCertificateViewDtoMapper::toViewDto)
										.collect(Collectors.toList()))
						.add(
								giftCertificateRepresentationAssembler.getLinksForGetAll(
										sortField, asc, nameFilter, descriptionFilter, tagNamesFilter, offset, limit)));

		result.setCollectionSize(certificates.size());
		result.setLimit(limit);
		result.setOffset(offset);

		return ResponseEntity.status(HttpStatus.OK.value()).body(result);
	}

	@PreAuthorize("hasAuthority('MODIFY_ALL')")
	@DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteGiftCertificate(@PathVariable("id") long id, Locale locale) {
		return !service.delete(id)
				? notFoundResponse(locale)
				: ResponseEntity.status(HttpStatus.OK.value())
						.body(giftCertificateRepresentationAssembler.getLinksForDelete(id));
	}
}

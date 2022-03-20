package com.epam.esm.web.representation.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.web.controller.GiftCertificateController;
import com.epam.esm.web.representation.dto.GiftCertificateViewDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class GiftCertificateRepresentationAssembler
		implements SimpleRepresentationModelAssembler<GiftCertificateViewDto> {

	public static final String CREATE_LINK_NAME = "create";
	public static final String UPDATE_LINK_NAME = "update";
	public static final String PATCH_LINK_NAME = "patch";
	public static final String DELETE_LINK_NAME = "delete";
	public static final String GET_LINK_NAME = "get";
	public static final String GET_ALL_LINK_NAME = "getAll";

	@Override
	public void addLinks(EntityModel<GiftCertificateViewDto> resource) {

		GiftCertificateViewDto dto = resource.getContent();
		long id=0;
		if(dto != null) {
			id = dto.getId();
		}

		resource.add(
				linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(id, null))
						.withSelfRel());
		resource.add(
				linkTo(methodOn(GiftCertificateController.class).createGiftCertificate(null, null))
						.withRel(CREATE_LINK_NAME));
		resource.add(
				linkTo(methodOn(GiftCertificateController.class).patchGiftCertificates(id, null, null))
						.withRel(PATCH_LINK_NAME));
		resource.add(
				linkTo(methodOn(GiftCertificateController.class).putGiftCertificates(id, null, null))
						.withRel(UPDATE_LINK_NAME));
		resource.add(
				linkTo(methodOn(GiftCertificateController.class).deleteGiftCertificate(id, null))
						.withRel(DELETE_LINK_NAME));
		resource.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.getAll(null, false, null, null, null, null, null, null))
						.withRel(GET_ALL_LINK_NAME));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<GiftCertificateViewDto>> resources) {
		// nothing to insert
	}

	public List<Link> getLinksForCreate(long id) {

		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(methodOn(GiftCertificateController.class).createGiftCertificate(null, null))
						.withSelfRel());
		links.add(
				linkTo(methodOn(GiftCertificateController.class).patchGiftCertificates(id, null, null))
						.withRel(PATCH_LINK_NAME));
		links.add(
				linkTo(methodOn(GiftCertificateController.class).deleteGiftCertificate(id, null))
						.withRel(DELETE_LINK_NAME));
		links.add(
				linkTo(methodOn(GiftCertificateController.class).putGiftCertificates(id, null, null))
						.withRel(UPDATE_LINK_NAME));
		links.add(
				linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(id, null))
						.withRel(GET_LINK_NAME));
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.getAll(null, false, null, null, null, null, null, null))
						.withRel(GET_ALL_LINK_NAME));

		return links;
	}

	public List<Link> getLinksForUpdate(GiftCertificateDto certificate) {

		long id = certificate.getId();
		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.putGiftCertificates(id, certificate, null))
						.withSelfRel());
		links.add(
				linkTo(methodOn(GiftCertificateController.class).createGiftCertificate(null, null))
						.withRel(CREATE_LINK_NAME));
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.patchGiftCertificates(id, certificate, null))
						.withRel(PATCH_LINK_NAME));
		links.add(
				linkTo(methodOn(GiftCertificateController.class).deleteGiftCertificate(id, null))
						.withRel(DELETE_LINK_NAME));
		links.add(
				linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(id, null))
						.withRel(GET_LINK_NAME));
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.getAll(null, false, null, null, null, null, null, null))
						.withRel(GET_ALL_LINK_NAME));

		return links;
	}

	public List<Link> getLinksForPatch(GiftCertificateDto certificate) {

		long id = certificate.getId();
		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.patchGiftCertificates(id, certificate, null))
						.withSelfRel());
		links.add(
				linkTo(methodOn(GiftCertificateController.class).createGiftCertificate(null, null))
						.withRel(CREATE_LINK_NAME));
		links.add(
				linkTo(methodOn(GiftCertificateController.class).deleteGiftCertificate(id, null))
						.withRel(DELETE_LINK_NAME));
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.putGiftCertificates(id, certificate, null))
						.withRel(UPDATE_LINK_NAME));
		links.add(
				linkTo(methodOn(GiftCertificateController.class).getGiftCertificate(id, null))
						.withRel(GET_LINK_NAME));
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.getAll(null, false, null, null, null, null, null, null))
						.withRel(GET_ALL_LINK_NAME));

		return links;
	}

	public List<Link> getLinksForDelete(long id) {

		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(methodOn(GiftCertificateController.class).deleteGiftCertificate(id, null))
						.withSelfRel());
		links.add(
				linkTo(methodOn(GiftCertificateController.class).createGiftCertificate(null, null))
						.withRel(CREATE_LINK_NAME));
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.getAll(null, false, null, null, null, null, null, null))
						.withRel(GET_ALL_LINK_NAME));

		return links;
	}

	public List<Link> getLinksForGetAll(
			String sortField,
			boolean asc,
			String nameFilter,
			String descriptionFilter,
			List<String> tagNamesFilter,
			Long offset,
			Long limit) {

		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(
								methodOn(GiftCertificateController.class)
										.getAll(
												sortField,
												asc,
												nameFilter,
												descriptionFilter,
												tagNamesFilter,
												limit,
												offset,
												null))
						.withSelfRel());
		return links;
	}
}

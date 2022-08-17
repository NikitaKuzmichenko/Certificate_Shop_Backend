package com.epam.esm.web.representation.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.representation.dto.TagViewDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class TagRepresentationAssembler implements SimpleRepresentationModelAssembler<TagViewDto> {

	public static final String CREATE_LINK_NAME = "create";
	public static final String GET_LINK_NAME = "get";
	public static final String DELETE_LINK_NAME = "delete";
	public static final String GET_ALL_LINK_NAME = "getAll";
	public static final String GET_POPULAR_LINK_NAME = "getPopular";

	@Override
	public void addLinks(EntityModel<TagViewDto> resource) {
		long id = 0;
		TagViewDto dto = resource.getContent();
		if (dto != null) {
			id = dto.getId();
		}

		resource.add(linkTo(methodOn(TagController.class).getTag(id, null)).withSelfRel());
		resource.add(
				linkTo(methodOn(TagController.class).deleteTag(id, null)).withRel(DELETE_LINK_NAME));
		resource.add(
				linkTo(methodOn(TagController.class).createTag(null, null)).withRel(CREATE_LINK_NAME));
		resource.add(
				linkTo(methodOn(TagController.class).getTags(null, null, null)).withRel(GET_ALL_LINK_NAME));
		resource.add(
				linkTo(methodOn(TagController.class).getPopularTag(null)).withRel(GET_POPULAR_LINK_NAME));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<TagViewDto>> resources) {
		// nothing to insert
	}

	public List<Link> getLinksForDelete(long id) {

		List<Link> links = new ArrayList<>();
		links.add(linkTo(methodOn(TagController.class).deleteTag(id, null)).withSelfRel());
		links.add(
				linkTo(methodOn(TagController.class).getTags(null, null, null)).withRel(GET_ALL_LINK_NAME));
		links.add(
				linkTo(methodOn(TagController.class).getPopularTag(null)).withRel(GET_POPULAR_LINK_NAME));

		return links;
	}

	public List<Link> getLinksForCreate(long id) {

		List<Link> links = new ArrayList<>();
		links.add(linkTo(methodOn(TagController.class).createTag(null, null)).withSelfRel());
		links.add(linkTo(methodOn(TagController.class).deleteTag(id, null)).withRel(DELETE_LINK_NAME));
		links.add(
				linkTo(methodOn(TagController.class).getTags(null, null, null)).withRel(GET_ALL_LINK_NAME));
		links.add(
				linkTo(methodOn(TagController.class).getPopularTag(null)).withRel(GET_POPULAR_LINK_NAME));
		links.add(linkTo(methodOn(TagController.class).getTag(id, null)).withRel(GET_LINK_NAME));

		return links;
	}

	public List<Link> getLinksForGetAll(long limit, long offset) {

		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(methodOn(TagController.class).getTags(limit, offset, null))
						.withRel(GET_ALL_LINK_NAME));
		links.add(
				linkTo(methodOn(TagController.class).getPopularTag(null)).withRel(GET_POPULAR_LINK_NAME));

		return links;
	}
}

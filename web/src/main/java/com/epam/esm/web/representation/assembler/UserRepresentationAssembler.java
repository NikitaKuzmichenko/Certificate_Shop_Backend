package com.epam.esm.web.representation.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.web.controller.UserController;
import com.epam.esm.web.representation.dto.UserViewDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserRepresentationAssembler
		implements SimpleRepresentationModelAssembler<UserViewDto> {

	public static final String GET_USER_LINK_NAME = "getAll";

	public static final String GET_USER_BY_ID_LINK_NAME = "get";

	@Override
	public void addLinks(EntityModel<UserViewDto> resource) {
		UserViewDto dto = resource.getContent();
		long id =0;
		if (dto != null) {
			id = dto.getId();
		}

		resource.add(linkTo(methodOn(UserController.class).getUser(id, null)).withSelfRel());
		resource.add(
				linkTo(methodOn(UserController.class).getUsers(null, null, null))
						.withRel(GET_USER_LINK_NAME));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<UserViewDto>> resources) {
		// nothing to insert
	}

	public List<Link> getLinksForGetAll(long limit, long offset) {
		List<Link> links = new ArrayList<>();
		links.add(linkTo(methodOn(UserController.class).getUsers(limit, offset, null)).withSelfRel());
		return links;
	}

	public List<Link> getLinksForCreate(long id) {
		List<Link> links = new ArrayList<>();
		links.add(linkTo(methodOn(UserController.class).createDefaultUsers(null, null)).withSelfRel());
		links.add(
				linkTo(methodOn(UserController.class).getUsers(null, null, null))
						.withRel(GET_USER_LINK_NAME));
		links.add(
				linkTo(methodOn(UserController.class).getUser(id, null)).withRel(GET_USER_BY_ID_LINK_NAME));
		return links;
	}
}

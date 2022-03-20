package com.epam.esm.web.representation.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.representation.dto.OrderViewDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class OrderRepresentationAssembler
		implements SimpleRepresentationModelAssembler<OrderViewDto> {

	public static final String GET_LINK_NAME = "getOrder";
	public static final String GET_BY_USER_ID_LINK_NAME = "getOrdersByUserId";

	@Override
	public void addLinks(EntityModel<OrderViewDto> resource) {
		long orderId = 0;
		OrderViewDto dto = resource.getContent();
		if(dto != null) {
			orderId = dto.getId();

			if (dto.getPurchases() != null && !dto.getPurchases().isEmpty()) {
				long userId = dto.getPurchases().get(0).getUserId();

				resource.add(
						linkTo(methodOn(OrderController.class).getUserOrders(userId, null, null, null))
								.withSelfRel());
			}
		}
		resource.add(
				linkTo(methodOn(OrderController.class).getOrders(orderId, null)).withRel(GET_LINK_NAME));
	}

	@Override
	public void addLinks(CollectionModel<EntityModel<OrderViewDto>> resources) {
		// nothing to insert
	}

	public List<Link> getLinksForCreateOrder(long orderId, long userId, Set<Long> certificateId) {

		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(methodOn(OrderController.class).createOrder(userId, certificateId, null))
						.withSelfRel());
		links.add(
				linkTo(methodOn(OrderController.class).getUserOrders(userId, null, null, null))
						.withRel(GET_BY_USER_ID_LINK_NAME));
		links.add(
				linkTo(methodOn(OrderController.class).getOrders(orderId, null)).withRel(GET_LINK_NAME));

		return links;
	}

	public List<Link> getLinksForGetAllByUserId(long userId, long limit, long offset) {

		List<Link> links = new ArrayList<>();
		links.add(
				linkTo(methodOn(OrderController.class).getUserOrders(userId, limit, offset, null))
						.withSelfRel());

		return links;
	}
}

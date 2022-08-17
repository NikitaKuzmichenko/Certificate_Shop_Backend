package com.epam.esm.web.controller;

import static com.epam.esm.web.exceptionhandler.ExceptionResponseCreator.badRequestResponse;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.representation.assembler.OrderRepresentationAssembler;
import com.epam.esm.web.representation.dto.collection.CollectionWrapper;
import com.epam.esm.web.representation.dto.mapper.OrderViewDtoMapper;
import io.swagger.annotations.ApiOperation;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/orders")
@RestController
public class OrderController {

	public static final long DEFAULT_OFFSET = 0;
	public static final long DEFAULT_LIMIT = 10;

	@Autowired private OrderService orderService;

	@Autowired private OrderRepresentationAssembler orderRepresentationAssembler;

	@PreAuthorize("hasAuthority('READ_ALL') and #userId == authentication.principal")
	@GetMapping(value = "user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get orders by user id")
	public ResponseEntity<?> getUserOrders(
			@PathVariable("userId") long userId,
			@RequestParam(required = false) Long offset,
			@RequestParam(required = false) Long limit,
			Locale locale) {

		if (offset == null) {
			offset = DEFAULT_OFFSET;
		}
		if (limit == null) {
			limit = DEFAULT_LIMIT;
		}

		List<OrderDto> orders = orderService.getByUserId(userId, limit, offset);

		if (orders == null) {
			return badRequestResponse(locale);
		}

		CollectionWrapper<CollectionModel> result = new CollectionWrapper<>();
		result.setCollection(
				orderRepresentationAssembler
						.toCollectionModel(
								orders.stream().map(OrderViewDtoMapper::toViewDto).collect(Collectors.toList()))
						.add(orderRepresentationAssembler.getLinksForGetAllByUserId(userId, limit, offset)));

		result.setCollectionSize(orders.size());
		result.setLimit(limit);
		result.setOffset(offset);

		return ResponseEntity.status(HttpStatus.OK.value()).body(result);
	}

	@PreAuthorize("hasAuthority('READ_ALL')")
	@GetMapping(value = "{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "get order by id")
	public ResponseEntity<?> getOrders(@PathVariable("orderId") long orderId, Locale locale) {
		OrderDto orders = orderService.getByOrderId(orderId);

		return ResponseEntity.status(HttpStatus.OK.value())
				.body(orderRepresentationAssembler.toModel(OrderViewDtoMapper.toViewDto(orders)));
	}

	@PreAuthorize(
			"(hasAuthority('WRITE_ALL') or hasAuthority('ORDER_CERTIFICATE')) and #userId == authentication.principal")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "create new order")
	public ResponseEntity<?> createOrder(
			@RequestParam long userId, @RequestParam Set<Long> certificateId, Locale locale) {

		OrderDto order = new OrderDto();
		List<PurchaseDto> purchases = new ArrayList<>();
		certificateId.forEach(e -> purchases.add(new PurchaseDto(e, userId)));
		order.setPurchases(purchases);
		order.setOrderDate(ZonedDateTime.now());

		Long id = orderService.create(order);
		return ResponseEntity.status(HttpStatus.CREATED.value())
				.body(orderRepresentationAssembler.getLinksForCreateOrder(id, userId, certificateId));
	}
}

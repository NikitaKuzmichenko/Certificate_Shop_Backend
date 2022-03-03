package com.epam.esm.dto.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import java.util.stream.Collectors;

public class OrderDtoMapper {

	private OrderDtoMapper() {}

	public static Order getOrderFromDto(OrderDto dto) {
		if (dto == null) {
			return null;
		}
		Order order = new Order();
		order.setId(dto.getId());
		if (dto.getOrderDate() != null) {
			order.setOrderDate(dto.getOrderDate());
		}
		return order;
	}

	public static OrderDto mapOrderToDto(Order entity) {
		if (entity == null) {
			return null;
		}
		OrderDto order = new OrderDto();
		order.setId(entity.getId());
		if (entity.getOrderDate() != null) {
			order.setOrderDate(entity.getOrderDate());
		}
		if (entity.getPurchases() != null) {
			order.setPurchases(
					entity.getPurchases().stream()
							.map(PurchaseDtoMapper::mapPurchaseToDto)
							.collect(Collectors.toList()));
		}
		return order;
	}
}

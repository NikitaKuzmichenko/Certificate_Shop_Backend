package com.epam.esm.web.representation.dto.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.web.representation.dto.OrderViewDto;
import java.util.stream.Collectors;

public class OrderViewDtoMapper {

	private OrderViewDtoMapper() {}

	public static OrderViewDto toViewDto(OrderDto entity) {
		if (entity == null) {
			return null;
		}
		OrderViewDto dto = new OrderViewDto();
		dto.setId(entity.getId());
		dto.setOrderTime(entity.getOrderDate());
		if (entity.getPurchases() != null) {
			dto.setPurchases(
					entity.getPurchases().stream()
							.map(PurchaseViewDtoMapper::toViewDto)
							.collect(Collectors.toList()));
		}
		return dto;
	}
}

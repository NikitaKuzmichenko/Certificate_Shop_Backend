package com.epam.esm.web.representation.dto.mapper;

import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.web.representation.dto.PurchaseViewDto;

public class PurchaseViewDtoMapper {
	private PurchaseViewDtoMapper() {}

	public static PurchaseViewDto toViewDto(PurchaseDto entity) {
		PurchaseViewDto dto = new PurchaseViewDto();
		dto.setPrice(entity.getPrice());
		dto.setOrderId(entity.getOrderId());
		dto.setUserId(entity.getUserId());
		dto.setGiftCertificateId(entity.getGiftCertificateId());
		return dto;
	}
}

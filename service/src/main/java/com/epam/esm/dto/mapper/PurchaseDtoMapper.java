package com.epam.esm.dto.mapper;

import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.entity.purchase.Purchase;

public class PurchaseDtoMapper {

	private PurchaseDtoMapper() {}

	public static PurchaseDto mapPurchaseToDto(Purchase entity) {
		if (entity == null) {
			return null;
		}
		PurchaseDto dto = new PurchaseDto();
		if (entity.getPrice() != null) {
			dto.setPrice(entity.getPrice());
		}
		if (entity.getOrderId() != null) {
			dto.setOrderId(entity.getOrderId().getId());
		}
		if (entity.getUserId() != null) {
			dto.setUserId(entity.getUserId().getId());
		}
		if (entity.getGiftCertificateId() != null) {
			dto.setGiftCertificateId(entity.getGiftCertificateId().getId());
		}

		return dto;
	}

	public static Purchase getPurchaseFromDto(
			Purchase entity, Order order, GiftCertificate certificate, User user) {
		if (entity == null || order == null || certificate == null || user == null) {
			return null;
		}
		Purchase purchase = new Purchase();
		purchase.setUserId(user);
		purchase.setOrderId(order);
		purchase.setGiftCertificateId(certificate);
		if (certificate.getPrice() != null) {
			purchase.setPrice(certificate.getPrice());
		}

		return purchase;
	}
}

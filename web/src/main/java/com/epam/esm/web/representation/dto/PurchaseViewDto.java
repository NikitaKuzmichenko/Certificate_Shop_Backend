package com.epam.esm.web.representation.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PurchaseViewDto implements Serializable {
	private long orderId;
	private long userId;
	private long giftCertificateId;
	private BigDecimal price;
}

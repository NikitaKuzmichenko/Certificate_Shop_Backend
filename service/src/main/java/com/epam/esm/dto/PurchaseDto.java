package com.epam.esm.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PurchaseDto implements Serializable {

	private long userId;

	private long giftCertificateId;

	private long orderId;

	@NotNull private BigDecimal price;

	public PurchaseDto(long giftCertificateId, long userId) {
		this.giftCertificateId = giftCertificateId;
		this.userId = userId;
	}
}

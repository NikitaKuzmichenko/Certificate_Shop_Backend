package com.epam.esm.entity.purchase;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchasePK implements Serializable {
	private long orderId;
	private long userId;
	private long giftCertificateId;
}

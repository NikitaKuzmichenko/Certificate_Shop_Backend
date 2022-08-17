package com.epam.esm.entity.purchase;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "Purchase")
@Table(name = "purchases")
@IdClass(PurchasePK.class)
// @Audited
public class Purchase implements Serializable {

	@Id
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User userId;

	@Id
	@ManyToOne
	@JoinColumn(name = "gift_certificate_id")
	private GiftCertificate giftCertificateId;

	@Id
	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order orderId;

	@Column(name = "order_cost", nullable = false)
	private BigDecimal price;
}

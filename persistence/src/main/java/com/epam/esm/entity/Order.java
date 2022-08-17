package com.epam.esm.entity;

import com.epam.esm.entity.purchase.Purchase;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "Order")
@Table(name = "orders")
// @Audited
public class Order implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id", nullable = false)
	private long id;

	@Column(name = "order_date", nullable = false)
	private ZonedDateTime orderDate;

	@OneToMany(
			mappedBy = "orderId",
			cascade = {CascadeType.MERGE, CascadeType.PERSIST},
			orphanRemoval = true)
	private List<Purchase> purchases = new ArrayList<>();
}

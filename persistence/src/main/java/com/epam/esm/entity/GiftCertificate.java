package com.epam.esm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "GiftCertificate")
@Table(name = "gift_certificates")
// @Audited
public class GiftCertificate implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "gift_certificate_id")
	private long id;

	@Column(name = "gift_name", unique = true, nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price", nullable = false)
	private BigDecimal price;

	@Column(name = "duration", nullable = false)
	private Integer duration;

	@Column(name = "create_date", nullable = false)
	private ZonedDateTime creationDate;

	@Column(name = "last_update_date")
	private ZonedDateTime lastUpdateDate;

	@ManyToMany(cascade = {CascadeType.MERGE})
	@JoinTable(
			name = "attachments",
			joinColumns = {
				@JoinColumn(name = "gift_certificates_id", referencedColumnName = "gift_certificate_id")
			},
			inverseJoinColumns = {@JoinColumn(name = "tags_id", referencedColumnName = "tag_id")})
	private List<Tag> tags = new ArrayList<>();
}

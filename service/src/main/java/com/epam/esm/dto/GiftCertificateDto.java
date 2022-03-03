package com.epam.esm.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificateDto implements Serializable {

	private long id;

	@NotNull
	@Size(max = 100)
	private String name;

	@Size(max = 100)
	private String description;

	@NotNull private BigDecimal price;

	@NotNull
	@Min(0)
	private Integer duration;

	@NotNull private ZonedDateTime creationDate;

	private ZonedDateTime lastUpdateDate;

	@NotNull private Set<TagDto> tags = new HashSet<>();
}

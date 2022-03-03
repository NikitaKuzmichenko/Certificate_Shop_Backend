package com.epam.esm.web.representation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class GiftCertificateViewDto implements Serializable {

	private long id;
	private String name;
	private String description;
	private BigDecimal price;
	private Integer duration;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
	private ZonedDateTime creationDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
	private ZonedDateTime lastUpdateDate;

	private Set<TagViewDto> tags;
}

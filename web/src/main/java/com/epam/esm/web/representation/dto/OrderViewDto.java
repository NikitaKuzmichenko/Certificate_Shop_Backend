package com.epam.esm.web.representation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class OrderViewDto implements Serializable {
	private long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX")
	private ZonedDateTime orderTime;

	private List<PurchaseViewDto> purchases = new ArrayList<>();
}

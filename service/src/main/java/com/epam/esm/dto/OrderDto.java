package com.epam.esm.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDto {
	private long id;

	@NotNull private ZonedDateTime orderDate;

	@NotNull private List<PurchaseDto> purchases = new ArrayList<>();
}

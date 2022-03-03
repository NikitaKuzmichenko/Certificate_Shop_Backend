package com.epam.esm.web.representation.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class TagViewDto implements Serializable {
	private long id;
	private String name;
}

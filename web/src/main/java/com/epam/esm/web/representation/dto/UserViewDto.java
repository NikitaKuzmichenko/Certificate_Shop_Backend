package com.epam.esm.web.representation.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserViewDto implements Serializable {
	private long id;
	private String email;
}

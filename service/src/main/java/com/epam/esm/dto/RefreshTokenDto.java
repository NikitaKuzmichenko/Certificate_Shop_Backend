package com.epam.esm.dto;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RefreshTokenDto implements Serializable {

	private long id;
	private long userId;
	@NotNull private String token;
	@NotNull private Date creationDate;
	@NotNull private Date expirationDate;
}

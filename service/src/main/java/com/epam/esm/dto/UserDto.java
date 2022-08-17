package com.epam.esm.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
	private long id;

	@NotNull
	@Size(max = 40)
	@NotEmpty
	private String email;

	@NotNull
	@Size(max = 100)
	@NotEmpty
	private String password;

	@NotNull private Set<UserRoleDto> roles = new HashSet<>();
}

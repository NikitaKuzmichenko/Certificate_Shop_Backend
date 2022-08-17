package com.epam.esm.dto;

import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleDto {

	private long id;

	@NotEmpty
	@NotNull
	@Size(max = 50)
	private String name;

	private Set<String> authorities = new HashSet<>();

	public UserRoleDto(String name) {
		this.name = name;
	}
}

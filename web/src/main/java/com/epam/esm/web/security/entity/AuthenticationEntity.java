package com.epam.esm.web.security.entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class AuthenticationEntity implements Serializable {
	private String email;
	private String password;
}

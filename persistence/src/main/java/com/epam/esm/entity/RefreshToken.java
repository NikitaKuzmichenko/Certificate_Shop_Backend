package com.epam.esm.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "refresh_tokens")
@Entity(name = "RefreshToken")
// @Audited
public class RefreshToken implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "refresh_token_id")
	private long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User userId;

	@Column(name = "refresh_token")
	private String token;

	@Column(name = "creationDate")
	private Date creationDate;

	@Column(name = "expirationDate")
	private Date expirationDate;
}

package com.epam.esm.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "Authorities")
@Table(name = "authorities")
// @Audited
public class Authorities implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "authority_id")
	private long id;

	@Column(name = "name")
	private String name;
}

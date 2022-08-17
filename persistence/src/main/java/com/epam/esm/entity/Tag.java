package com.epam.esm.entity;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity(name = "Tag")
@Table(name = "tags")
// @Audited
public class Tag implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tag_id")
	private long id;

	@Column(name = "tag_name", unique = true, nullable = false)
	private String name;
}

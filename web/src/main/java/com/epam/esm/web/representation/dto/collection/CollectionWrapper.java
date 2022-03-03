package com.epam.esm.web.representation.dto.collection;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectionWrapper<T> implements Serializable {
	@JsonUnwrapped private T collection;
	private long collectionSize;
	private long offset;
	private long limit;
}

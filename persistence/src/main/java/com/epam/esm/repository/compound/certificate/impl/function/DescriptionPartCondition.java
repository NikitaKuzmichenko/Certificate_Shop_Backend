package com.epam.esm.repository.compound.certificate.impl.function;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class DescriptionPartCondition implements ConfigurableFunction {

	public static final String SEQUENCE_START_SYMBOL = "%";
	public static final String SEQUENCE_END_SYMBOL = "%";
	private String descriptionPart;

	private Root<?> root;

	public DescriptionPartCondition(String descriptionPart) {
		this.descriptionPart = descriptionPart;
	}

	public String getDescriptionPart() {
		return descriptionPart;
	}

	public void setDescriptionPart(String descriptionPart) {
		this.descriptionPart = descriptionPart;
	}

	@Override
	public void setRoot(Root<?> root) {
		this.root = root;
	}

	@Override
	public List<Predicate> apply(CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(
				criteriaBuilder.like(
						root.get("description"),
						SEQUENCE_START_SYMBOL + descriptionPart + SEQUENCE_END_SYMBOL));
		return predicates;
	}
}

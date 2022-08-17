package com.epam.esm.repository.compound.certificate.impl.function;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class NamePartCondition implements ConfigurableFunction {

	public static final String SEQUENCE_START_SYMBOL = "%";
	public static final String SEQUENCE_END_SYMBOL = "%";

	private String namePart;

	private Root<?> root;

	public NamePartCondition(String descriptionPart) {
		this.namePart = descriptionPart;
	}

	public String getDescriptionPart() {
		return namePart;
	}

	public void setDescriptionPart(String namePart) {
		this.namePart = namePart;
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
						root.get("name"), SEQUENCE_START_SYMBOL + namePart + SEQUENCE_END_SYMBOL));
		return predicates;
	}
}

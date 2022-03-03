package com.epam.esm.repository.compound.certificate.impl.function;

import com.epam.esm.entity.Tag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class TagContainingCondition implements ConfigurableFunction {

	private List<Tag> tags;

	private Root<?> root;

	public TagContainingCondition(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Tag> getTagNames() {
		return tags;
	}

	public void setTagNames(List<Tag> tags) {
		this.tags = tags;
	}

	@Override
	public void setRoot(Root<?> root) {
		this.root = root;
	}

	@Override
	public List<Predicate> apply(CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		Expression<Collection<Tag>> certificateTags = root.get("tags");
		for (Tag tag : tags) {
			if (tag == null) {
				predicates.add(criteriaBuilder.disjunction());
				break;
			} else {
				predicates.add(criteriaBuilder.isMember(tag, certificateTags));
			}
		}
		return predicates;
	}
}

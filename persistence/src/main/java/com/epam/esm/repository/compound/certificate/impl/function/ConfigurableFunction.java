package com.epam.esm.repository.compound.certificate.impl.function;

import java.util.List;
import java.util.function.Function;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface ConfigurableFunction extends Function<CriteriaBuilder, List<Predicate>> {
	void setRoot(Root<?> root);
}

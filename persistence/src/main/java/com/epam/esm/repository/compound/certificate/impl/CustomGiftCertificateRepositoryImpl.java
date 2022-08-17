package com.epam.esm.repository.compound.certificate.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.compound.certificate.CustomGiftCertificateRepository;
import com.epam.esm.repository.compound.certificate.impl.function.ConfigurableFunction;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
* Repository implementation of the {@code CustomGiftCertificateRepository} interface. Implements
* all operations, and permits all elements, excluding {@code null}. This implementation works with
* {@code SessionFactory}.
*/
@Component
@Repository
public class CustomGiftCertificateRepositoryImpl implements CustomGiftCertificateRepository {

	@Autowired @PersistenceContext private EntityManager entityManager;

	/**
	* @param searchParams parameters which restrict result
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws NullPointerException if the specified element is {@code null} and this
	*/
	@Override
	@Transactional
	public List<GiftCertificate> getAll(
			List<ConfigurableFunction> searchParams, long limit, long offset) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
		Root<GiftCertificate> root = cq.from(GiftCertificate.class);

		List<Predicate> predicates = new ArrayList<>();
		for (ConfigurableFunction function : searchParams) {
			function.setRoot(root);
			predicates.addAll(function.apply(cb));
		}

		cq.select(root).where(predicates.toArray(new Predicate[0]));
		return entityManager
				.createQuery(cq)
				.setMaxResults((int) limit)
				.setFirstResult((int) offset)
				.getResultList();
	}

	/**
	* @param sortedColumn column according to which performed sort
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws NullPointerException if the specified element is {@code null} and this
	*/
	@Override
	@Transactional
	public List<GiftCertificate> getAll(
			String sortedColumn, boolean sortDirection, long limit, long offset) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
		Root<GiftCertificate> root = cq.from(GiftCertificate.class);
		cq.select(root);

		if (sortDirection) {
			cq.orderBy(cb.asc(root.get(sortedColumn)));
		} else {
			cq.orderBy(cb.desc(root.get(sortedColumn)));
		}

		return entityManager
				.createQuery(cq)
				.setMaxResults((int) limit)
				.setFirstResult((int) offset)
				.getResultList();
	}

	/**
	* @param sortedColumn column according to which performed sort
	* @param searchParams parameters which restrict result
	* @param sortDirection sorting direction
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return element in this database, if database empty return empty list
	* @throws NullPointerException if the specified element is {@code null} and this
	*/
	@Override
	@Transactional
	public List<GiftCertificate> getAll(
			List<ConfigurableFunction> searchParams,
			String sortedColumn,
			boolean sortDirection,
			long limit,
			long offset) {

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<GiftCertificate> cq = cb.createQuery(GiftCertificate.class);
		Root<GiftCertificate> root = cq.from(GiftCertificate.class);

		List<Predicate> predicates = new ArrayList<>();
		for (ConfigurableFunction function : searchParams) {
			function.setRoot(root);
			predicates.addAll(function.apply(cb));
		}

		if (sortDirection) {
			cq.orderBy(cb.asc(root.get(sortedColumn)));
		} else {
			cq.orderBy(cb.desc(root.get(sortedColumn)));
		}

		cq.select(root).where(predicates.toArray(new Predicate[0]));
		return entityManager
				.createQuery(cq)
				.setMaxResults((int) limit)
				.setFirstResult((int) offset)
				.getResultList();
	}
}

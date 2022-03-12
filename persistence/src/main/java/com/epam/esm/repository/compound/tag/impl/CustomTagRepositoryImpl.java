package com.epam.esm.repository.compound.tag.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.entity.purchase.Purchase;
import com.epam.esm.repository.compound.tag.CustomTagRepository;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
* Repository implementation of the {@code TagRepository} interface. Implements all operations, and
* permits all elements, excluding {@code null}. This implementation works with {@code
* SessionFactory}.
*/
@Component
public class CustomTagRepositoryImpl implements CustomTagRepository {
	private static final String USER_WITH_MAX_SPENDING =
			" select o from Purchase o"
					+ " group by o.id"
					+ " order by sum(o.price) desc";

	private static final String TEMP_USER = "user";

	private static final String USER_CERTIFICATES =
			" select t from GiftCertificate c"
					+ " join c.tags t"
					+ " join Purchase o on o.giftCertificateId = c"
					+ " where o.userId = : "
					+ TEMP_USER
					+ " group by t.id"
					+ " order by count(t.id) desc";

	private static final String TAG_NAME = "paramName";
	private static final String SELECT_BY_NAME = "from Tag where tag_name = : " + TAG_NAME;

	/** hibernate session factory */
	@Autowired @PersistenceContext private EntityManager entityManager;

	/**
	* @param tags if database containing element of GiftCertificate with this id - tags will be
	*     reattached to this element
	* @throws NullPointerException if the specified element is null and this repository does not
	*     permit null elements
	*/
	@Override
	@Transactional
	public Set<Tag> persistTags(Collection<Tag> tags) {
		Set<Tag> persistedTags = new HashSet<>();
		for (Tag tag : tags) {
			try {
				Tag temp =
						entityManager
								.createQuery(SELECT_BY_NAME, Tag.class)
								.setParameter(TAG_NAME, tag.getName())
								.getSingleResult();

				persistedTags.add(temp);
			} catch (NoResultException e) {
				persistedTags.add(entityManager.merge(tag));
			}
		}
		return persistedTags;
	}

	/** @return Get the most widely used tag of a user with the highest cost of all orders */
	@Override
	@Transactional
	public Tag getPopular() {
		try {
			Purchase purchase =
					entityManager
							.createQuery(USER_WITH_MAX_SPENDING, Purchase.class)
							.setMaxResults(1)
							.getSingleResult();

			return entityManager
					.createQuery(USER_CERTIFICATES, Tag.class)
					.setParameter(TEMP_USER, purchase.getUserId())
					.setMaxResults(1)
					.getSingleResult();

		} catch (NoResultException e) {
			return null;
		}
	}
}

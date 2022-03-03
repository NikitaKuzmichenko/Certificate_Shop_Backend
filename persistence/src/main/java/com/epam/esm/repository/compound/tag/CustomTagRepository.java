package com.epam.esm.repository.compound.tag;

import com.epam.esm.entity.Tag;
import java.util.Collection;
import java.util.Set;

public interface CustomTagRepository {
	/** @return Get the most widely used tag of a user with the highest cost of all orders */
	Tag getPopular();

	/**
	* @param tags if database containing element of GiftCertificate with this id - tags will be
	*     reattached to this element
	* @throws NullPointerException if the specified element is null and this repository does not
	*     permit null elements
	*/
	Set<Tag> persistTags(Collection<Tag> tags);
}

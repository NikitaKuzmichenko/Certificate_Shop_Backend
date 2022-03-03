package com.epam.esm.repository.compound.tag;

import com.epam.esm.entity.Tag;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
* An ordered repository to work with Tag class. The user of this interface has precise control over
* database. The user can access elements by their integer index (PK of element).
*/
@Component
public interface TagRepository extends PagingAndSortingRepository<Tag, Long>, CustomTagRepository {
	Optional<Tag> findByName(String name);
}

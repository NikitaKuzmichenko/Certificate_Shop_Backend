package com.epam.esm.repository;

import com.epam.esm.entity.User;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

/**
* An ordered repository to work with{@code User} class. The user of this interface has precise
* control over database. The user can access elements by their integer index (PK of element).
*/
@Component
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	Optional<User> findByEmail(String email);
}

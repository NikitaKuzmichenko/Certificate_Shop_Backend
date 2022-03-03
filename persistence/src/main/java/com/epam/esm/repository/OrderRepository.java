package com.epam.esm.repository;

import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
* An ordered repository to work with{@code Purchase} class. The user of this interface has precise
* control over database. The user can access elements by their integer index (PK of element).
*/
@Component
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {
	@Query("SELECT o FROM Order o join o.purchases p WHERE p.userId =:userId")
	List<Order> getOrders(@Param("userId") User id);

	@Query("SELECT o FROM Order o join o.purchases p WHERE p.userId =:userId")
	List<Order> getOrders(@Param("userId") User id, Pageable page);
}

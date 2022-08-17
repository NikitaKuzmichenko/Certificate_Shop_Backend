package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/** An ordered service interface to work with {@code Purchase} class. */
@Service
@Validated
public interface OrderService {

	/**
	* @param order entity to be appended to database
	* @return PK of inserted element, or {@code null} if value can not be inserted
	* @throws DuplicateEntityException if entity with this token already exist in repository
	*/
	Long create(@Valid OrderDto order);

	/**
	* @param userId {@code User} who owns orders
	* @param limit amount of elements in result list
	* @param offset amount of elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws BadInputException if limit or offset is negative
	*/
	List<OrderDto> getByUserId(long userId, long limit, long offset);

	/**
	* @param orderId id of {@code Purchase} entity
	* @return elements in this database, if database empty return empty list
	* @throws EntityNotExistException if entity with this orderId do not exist in repository
	*/
	OrderDto getByOrderId(long orderId);
}

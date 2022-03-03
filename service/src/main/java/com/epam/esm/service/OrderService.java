package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/** An ordered service interface to work with {@code Purchase} class. */
@Service
@Validated
public interface OrderService {

	/**
	* @param order element to be appended to database
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	Long create(@Valid OrderDto order);

	/**
	* @param userId {@code User} who owns orders
	* @return elements in this database, if database empty return empty list
	*/
	List<OrderDto> getByUserId(long userId);

	/**
	* @param userId {@code User} who owns orders
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return elements in this database, if database empty return empty list
	*/
	List<OrderDto> getByUserId(long userId, long limit, long offset);

	/**
	* @param orderId id of {@code Purchase} entity
	* @return elements in this database, if database empty return empty list
	*/
	OrderDto getByOrderId(long orderId);
}

package com.epam.esm.service.implementation;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.GiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.OrderDtoMapper;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.entity.purchase.Purchase;
import com.epam.esm.pagination.OffsetLimitPage;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
* Service implementation of the {@code OrderService} interface. Implements all operations, and
* permits all elements, including {@code null}. This implementation works with {@code UserService}.
* This implementation works with {@code GiftCertificateService}.
*/
@Service
@Validated
public class OrderServiceImpl implements OrderService {

	@Autowired private final OrderRepository orderRepository;

	@Autowired private final UserService userService;

	@Autowired private final GiftCertificateService giftCertificateService;

	public OrderServiceImpl(
			OrderRepository orderRepository,
			UserService userService,
			GiftCertificateService giftCertificateService) {
		this.orderRepository = orderRepository;
		this.userService = userService;
		this.giftCertificateService = giftCertificateService;
	}

	/**
	* @param orderDto element to be appended to database
	* @return PK of inserted element, or {@code null} if value can not be inserted
	*/
	@Override
	public Long create(@Valid OrderDto orderDto) {
		if (orderDto == null || orderDto.getPurchases() == null) {
			return null;
		}

		Order order = OrderDtoMapper.getOrderFromDto(orderDto);
		List<Purchase> purchases = new ArrayList<>();

		for (PurchaseDto purchaseDto : orderDto.getPurchases()) {
			UserDto user = userService.getById(purchaseDto.getUserId());
			if (user == null) {
				return null;
			}

			GiftCertificateDto certificateDto =
					giftCertificateService.getById(purchaseDto.getGiftCertificateId());
			if (certificateDto == null) {
				return null;
			}

			Purchase purchase = new Purchase();
			purchase.setPrice(certificateDto.getPrice());
			purchase.setGiftCertificateId(
					GiftCertificateDtoMapper.getGiftCertificateFromDto(certificateDto));
			purchase.setUserId(UserDtoMapper.mapDtoToUser(user));
			purchase.setOrderId(order);

			purchases.add(purchase);
		}

		order.setPurchases(purchases);

		return orderRepository.save(order).getId();
	}

	/**
	* @param userId {@code User} who owns orders
	* @return elements in this database, if database empty return empty list
	*/
	@Override
	public List<OrderDto> getByUserId(long userId) {
		User user = UserDtoMapper.mapDtoToUser(userService.getById(userId));
		if (user == null) {
			return null;
		}

		return orderRepository.getOrders(user).stream()
				.map(OrderDtoMapper::mapOrderToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	private void paginationCheck(long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new IllegalArgumentException("limit or offset is negative");
		}
	}

	/**
	* @param userId {@code User} who owns orders
	* @param limit max amount of elements in result list
	* @param offset elements skipped before reading
	* @return elements in this database, if database empty return empty list
	* @throws IllegalArgumentException if limit or offset is negative
	*/
	@Override
	public List<OrderDto> getByUserId(long userId, long limit, long offset) {
		paginationCheck(limit, offset);

		User user = UserDtoMapper.mapDtoToUser(userService.getById(userId));
		if (user == null) {
			return null;
		}
		return orderRepository.getOrders(user, new OffsetLimitPage((int) limit, (int) offset)).stream()
				.map(OrderDtoMapper::mapOrderToDto)
				.collect(Collectors.toList());
	}

	/**
	* @param orderId id of {@code Purchase} entity
	* @return elements in this database, if database empty return empty list
	*/
	@Override
	public OrderDto getByOrderId(long orderId) {
		return OrderDtoMapper.mapOrderToDto(orderRepository.findById(orderId).orElse(null));
	}
}

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
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
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
import org.springframework.dao.DataIntegrityViolationException;
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

	@Override
	public Long create(@Valid OrderDto orderDto) {
		if (orderDto == null || orderDto.getPurchases() == null) {
			throw new BadInputException();
		}

		Order order = OrderDtoMapper.getOrderFromDto(orderDto);
		List<Purchase> purchases = new ArrayList<>();

		for (PurchaseDto purchaseDto : orderDto.getPurchases()) {
			UserDto user = userService.getById(purchaseDto.getUserId());

			GiftCertificateDto certificateDto =
					giftCertificateService.getById(purchaseDto.getGiftCertificateId());

			Purchase purchase = new Purchase();
			purchase.setPrice(certificateDto.getPrice());
			purchase.setGiftCertificateId(
					GiftCertificateDtoMapper.getGiftCertificateFromDto(certificateDto));
			purchase.setUserId(UserDtoMapper.mapDtoToUser(user));
			purchase.setOrderId(order);

			purchases.add(purchase);
		}

		order.setPurchases(purchases);

		try {
			return orderRepository.save(order).getId();
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateEntityException();
		}
	}

	@Override
	public List<OrderDto> getByUserId(long userId, long limit, long offset) {
		if (limit < 0 || offset < 0) {
			throw new BadInputException();
		}

		User user = UserDtoMapper.mapDtoToUser(userService.getById(userId));
		return orderRepository.getOrders(user, new OffsetLimitPage((int) limit, (int) offset)).stream()
				.map(OrderDtoMapper::mapOrderToDto)
				.collect(Collectors.toList());
	}

	@Override
	public OrderDto getByOrderId(long orderId) {
		return OrderDtoMapper.mapOrderToDto(
				orderRepository.findById(orderId).orElseThrow(EntityNotExistException::new));
	}
}

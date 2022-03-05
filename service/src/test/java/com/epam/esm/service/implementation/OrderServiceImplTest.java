package com.epam.esm.service.implementation;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.mapper.GiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.OrderDtoMapper;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.entity.purchase.Purchase;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.implementation.GiftCertificateServiceImpl;
import com.epam.esm.service.implementation.OrderServiceImpl;
import com.epam.esm.service.implementation.UserServiceImpl;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class OrderServiceImplTest {

	@Mock private static final OrderRepository orderRepository = mock(OrderRepository.class);

	@Mock
	private static final GiftCertificateServiceImpl certificateService =
			mock(GiftCertificateServiceImpl.class);

	@Mock private static final UserServiceImpl userService = mock(UserServiceImpl.class);

	private static final OrderService orderService =
			new OrderServiceImpl(orderRepository, userService, certificateService);

	private static Order testOrder;
	private static User testUser;
	private static GiftCertificate testCertificate;

	@BeforeAll
	public static void init() {
		BigDecimal price = new BigDecimal("21.20");
		testCertificate = new GiftCertificate();
		testCertificate.setName("name1");
		testCertificate.setDescription("desc");
		testCertificate.setPrice(price);
		testCertificate.setDuration(10);
		testCertificate.setCreationDate(ZonedDateTime.now());
		testCertificate.setLastUpdateDate(ZonedDateTime.now());
		testCertificate.setId(1);
		testCertificate.setTags(null);

		testUser = new User();
		testUser.setId(1);
		testUser.setEmail("email");
		testUser.setPassword("password");

		testOrder = new Order();

		Purchase testPurchase = new Purchase();
		testPurchase.setPrice(price);
		testPurchase.setGiftCertificateId(testCertificate);
		testPurchase.setUserId(testUser);
		testPurchase.setOrderId(testOrder);

		testOrder.setOrderDate(ZonedDateTime.now());
		testOrder.setId(1);
		testOrder.setPurchases(List.of(testPurchase));
	}

	@AfterEach
	public void resetMocks() {
		Mockito.reset(orderRepository, certificateService, userService);
	}

	@Test
	void getOrder() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testOrder));
		Assertions.assertEquals(OrderDtoMapper.mapOrderToDto(testOrder), orderService.getByOrderId(1));
	}

	@Test
	void getNotExistingOrder() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(orderService.getByOrderId(1));
	}

	@Test
	void getByUserId() {
		when(orderRepository.getOrders(anyObject())).thenReturn(List.of(testOrder));

		when(certificateService.getById(anyInt()))
				.thenReturn(GiftCertificateDtoMapper.mapGiftCertificateToDto(testCertificate));

		when(userService.getById(anyInt())).thenReturn(UserDtoMapper.mapUserToDto(testUser));

		Assertions.assertEquals(
				Stream.of(testOrder).map(OrderDtoMapper::mapOrderToDto).collect(Collectors.toList()),
				orderService.getByUserId(1));
	}

	@Test
	void getOrderWithoutUser() {
		when(orderRepository.getOrders(anyObject())).thenReturn(List.of(testOrder));

		when(certificateService.getById(anyInt()))
				.thenReturn(GiftCertificateDtoMapper.mapGiftCertificateToDto(testCertificate));

		when(userService.getById(anyInt())).thenReturn(null);

		Assertions.assertNull(orderService.getByUserId(1));
	}

	@Test
	void createOrder() {
		when(orderRepository.save(anyObject())).thenReturn(testOrder);

		when(certificateService.getById(anyInt()))
				.thenReturn(GiftCertificateDtoMapper.mapGiftCertificateToDto(testCertificate));

		when(userService.getById(anyInt())).thenReturn(UserDtoMapper.mapUserToDto(testUser));

		Assertions.assertNotNull(orderService.create(OrderDtoMapper.mapOrderToDto(testOrder)));
	}

	@Test
	void createOrderWithoutUser() {
		when(orderRepository.save(anyObject())).thenReturn(Long.valueOf(1));

		when(certificateService.getById(anyInt()))
				.thenReturn(GiftCertificateDtoMapper.mapGiftCertificateToDto(testCertificate));

		when(userService.getById(anyInt())).thenReturn(null);

		Assertions.assertNull(orderService.create(OrderDtoMapper.mapOrderToDto(testOrder)));
	}

	@Test
	void createOrderWithoutCertificate() {
		when(orderRepository.save(anyObject())).thenReturn(Long.valueOf(1));

		when(certificateService.getById(anyInt())).thenReturn(null);

		when(userService.getById(anyInt())).thenReturn(UserDtoMapper.mapUserToDto(testUser));

		Assertions.assertNull(orderService.create(OrderDtoMapper.mapOrderToDto(testOrder)));
	}

	@Test
	void getAllByUserId() {
		List<Order> orders = List.of(testOrder);

		when(orderRepository.getOrders(anyObject())).thenReturn(orders);

		when(userService.getById(anyInt())).thenReturn(UserDtoMapper.mapUserToDto(testUser));

		Assertions.assertEquals(
				orders.stream().map(OrderDtoMapper::mapOrderToDto).collect(Collectors.toList()),
				orderService.getByUserId(1));
	}

	@Test
	void getAllByNotExistingUserId() {
		List<Order> orders = List.of(testOrder);

		when(orderRepository.getOrders(anyObject())).thenReturn(orders);

		when(userService.getById(anyInt())).thenReturn(UserDtoMapper.mapUserToDto(null));

		Assertions.assertNull(orderService.getByUserId(1));
	}

	@Test
	void getAllByUserIdWithLimitAndOffset() {
		List<Order> orders = List.of(testOrder);

		when(orderRepository.getOrders(anyObject(), anyObject())).thenReturn(orders);

		when(userService.getById(anyInt())).thenReturn(UserDtoMapper.mapUserToDto(testUser));

		Assertions.assertEquals(
				orders.stream().map(OrderDtoMapper::mapOrderToDto).collect(Collectors.toList()),
				orderService.getByUserId(1, 1, 1));
	}

	@Test
	void getAllByNotExistingUserIdWithLimitAndOffset() {
		List<Order> orders = List.of(testOrder);

		when(orderRepository.getOrders(anyObject(), anyObject())).thenReturn(orders);

		when(userService.getById(anyInt())).thenReturn(UserDtoMapper.mapUserToDto(null));

		Assertions.assertNull(orderService.getByUserId(1, 1, 1));
	}
}

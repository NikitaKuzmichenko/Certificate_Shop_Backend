package com.epam.esm.service.implementation;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.mapper.GiftCertificateDtoMapper;
import com.epam.esm.dto.mapper.OrderDtoMapper;
import com.epam.esm.dto.mapper.UserDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.entity.purchase.Purchase;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.EntityNotExistException;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class OrderServiceImplTest {

	@Mock private static final OrderRepository orderRepository = mock(OrderRepository.class);

	@Mock private static final GiftCertificateServiceImpl certificateService = mock(GiftCertificateServiceImpl.class);

	@Mock private static final UserServiceImpl userService = mock(UserServiceImpl.class);

	private static final OrderService orderService = new OrderServiceImpl(orderRepository, userService, certificateService);

	private static Order testOrder;

	private static OrderDto testOrderDto;
	private static UserDto testUserDto;
	private static GiftCertificateDto testCertificateDto;

	@BeforeAll
	public static void init() {
		BigDecimal price = new BigDecimal("21.20");
		GiftCertificate testCertificate = new GiftCertificate();
		testCertificate.setName("name1");
		testCertificate.setDescription("desc");
		testCertificate.setPrice(price);
		testCertificate.setDuration(10);
		testCertificate.setCreationDate(ZonedDateTime.now());
		testCertificate.setLastUpdateDate(ZonedDateTime.now());
		testCertificate.setId(1);
		testCertificate.setTags(null);

		User testUser = new User();
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

		testOrderDto = OrderDtoMapper.mapOrderToDto(testOrder);
		testUserDto = UserDtoMapper.mapUserToDto(testUser);
		testCertificateDto = GiftCertificateDtoMapper.mapGiftCertificateToDto(testCertificate);
	}

	@AfterEach
	public void resetMocks() {
		Mockito.reset(orderRepository, certificateService, userService);
	}

	@Test
	void createOrder() {
		when(orderRepository.save(anyObject())).thenReturn(testOrder);
		when(certificateService.getById(anyInt())).thenReturn(testCertificateDto);
		when(userService.getById(anyInt())).thenReturn(testUserDto);

		Assertions.assertNotNull(orderService.create(testOrderDto));
	}

	@Test
	void createOrderWithoutUser() {
		when(orderRepository.save(anyObject())).thenReturn(1L);
		when(certificateService.getById(anyInt())).thenReturn(testCertificateDto);
		when(userService.getById(anyInt())).thenThrow(new EntityNotExistException());

		Assertions.assertThrows(EntityNotExistException.class,()->orderService.create(testOrderDto));
	}

	@Test
	void createOrderWithoutCertificate() {
		when(orderRepository.save(anyObject())).thenReturn(1L);
		when(certificateService.getById(anyInt())).thenThrow(new EntityNotExistException());
		when(userService.getById(anyInt())).thenReturn(testUserDto);

		Assertions.assertThrows(EntityNotExistException.class,()->orderService.create(testOrderDto));
	}

	@Test
	void getAllByUserIdWithLimitAndOffset() {
		List<Order> orders = List.of(testOrder);
		when(orderRepository.getOrders(anyObject(), anyObject())).thenReturn(orders);
		when(userService.getById(anyInt())).thenReturn(testUserDto);

		Assertions.assertEquals(
				orders.stream().map(OrderDtoMapper::mapOrderToDto).collect(Collectors.toList()),
				orderService.getByUserId(1, 1, 1));
	}

	@Test
	void getAllByNotExistingUserIdWithLimitAndOffset() {
		List<Order> orders = List.of(testOrder);
		when(orderRepository.getOrders(anyObject(), anyObject())).thenReturn(orders);
		when(userService.getById(anyInt())).thenThrow(new EntityNotExistException());

		Assertions.assertThrows(EntityNotExistException.class, ()->orderService.getByUserId(1, 1, 1));
	}

	@ParameterizedTest
	@CsvSource({"-1,-1", "-1,1", "1,-1"})
	void getAllUserWitchIncorrectLimitAndOffset(int limit,int offset) {
		List<Order> orders = List.of(testOrder);
		when(orderRepository.getOrders(anyObject(), anyObject())).thenReturn(orders);
		when(userService.getById(anyInt())).thenReturn(testUserDto);

		Assertions.assertThrows(BadInputException.class, ()->orderService.getByUserId(1, limit, offset));
	}

	@Test
	void getOrderById() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testOrder));
		Assertions.assertEquals(testOrderDto, orderService.getByOrderId(1));
	}

	@Test
	void getNotExistingOrderById() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotExistException.class, ()->orderService.getByOrderId(1));
	}
}

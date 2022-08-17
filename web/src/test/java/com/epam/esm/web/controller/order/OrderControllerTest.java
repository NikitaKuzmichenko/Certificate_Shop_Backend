package com.epam.esm.web.controller.order;

import static com.epam.esm.web.utils.Utils.parsIntegerFromLong;
import static org.hamcrest.Matchers.equalTo;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.PurchaseDto;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.service.implementation.OrderServiceImpl;
import com.epam.esm.web.controller.OrderController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
@Import(OrderTestConfiguration.class)
class OrderControllerTest {

	public static final String GET_BY_ID_PATH = "/orders/1";
	public static final String GET_BY_USER_ID_PATH = "/orders/user/1";
	public static final String CREATE_PATH = "/orders";

	@MockBean private OrderServiceImpl orderService;

	@Autowired private MockMvc mvc;

	private static OrderDto dto;

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(mvc);
		Mockito.reset(orderService);
	}

	@BeforeAll
	static void init() {
		PurchaseDto purchase = new PurchaseDto();
		purchase.setOrderId(1);
		purchase.setPrice(BigDecimal.valueOf(10));
		purchase.setUserId(1);
		purchase.setGiftCertificateId(1);
		dto = new OrderDto();
		dto.setId(1);
		dto.setPurchases(List.of(purchase));
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getOrderById() {
		Mockito.when(orderService.getByOrderId(Mockito.anyLong())).thenReturn(dto);

		RestAssuredMockMvc.given()
				.when()
				.get(GET_BY_ID_PATH)
				.then()
				.statusCode(200)
				.body("id", equalTo(parsIntegerFromLong(dto.getId())))
				.body("orderDate", equalTo(dto.getOrderDate()))
				.body(
						"purchases[0].orderId",
						equalTo(parsIntegerFromLong(dto.getPurchases().get(0).getOrderId())))
				.body(
						"purchases[0].userId",
						equalTo(parsIntegerFromLong(dto.getPurchases().get(0).getUserId())))
				.body(
						"purchases[0].giftCertificateId",
						equalTo(parsIntegerFromLong(dto.getPurchases().get(0).getGiftCertificateId())))
				.body("purchases[0].price", equalTo((dto.getPurchases().get(0).getPrice().intValue())));
	}

	@Test
	void getOrderByIdWithOutAuthorization() {
		Mockito.when(orderService.getByOrderId(Mockito.anyLong())).thenReturn(dto);

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getNotExistingOrderById() {
		Mockito.when(orderService.getByOrderId(Mockito.anyLong()))
				.thenThrow(new EntityNotExistException());

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(404);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getOrderByUserIdFailed() {
		Mockito.when(orderService.getByUserId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(List.of(dto));

		RestAssuredMockMvc.given().when().get(GET_BY_USER_ID_PATH).then().statusCode(403);
	}

	@Test
	void getOrderByUserIdWithOutAuthorization() {
		Mockito.when(orderService.getByUserId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(List.of(dto));

		RestAssuredMockMvc.given().when().get(GET_BY_USER_ID_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getNotExistingOrderByUserId() {
		Mockito.when(orderService.getByUserId(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong()))
				.thenReturn(null);

		RestAssuredMockMvc.given().when().get(GET_BY_USER_ID_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"WRITE_ALL"})
	void tryCreateOrderForOtherUser() {
		Mockito.when(orderService.create(Mockito.any())).thenReturn(Long.valueOf(1));

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.param("userId", 1)
				.param("certificateId", Set.of(1))
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(403);
	}
}

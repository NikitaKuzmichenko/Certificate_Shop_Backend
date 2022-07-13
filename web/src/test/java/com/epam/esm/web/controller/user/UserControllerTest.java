package com.epam.esm.web.controller.user;

import static com.epam.esm.web.utils.Utils.parsIntegerFromLong;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;

import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.service.implementation.UserServiceImpl;
import com.epam.esm.web.controller.UserController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
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

@WebMvcTest(UserController.class)
@Import(UserTestConfiguration.class)
class UserControllerTest {

	public static final String GET_BY_ID_PATH = "/users/1";
	public static final String GET_ALL_PATH = "/users";
	public static final String CREATE_PATH = "/users";

	@MockBean private UserServiceImpl userService;

	@Autowired private MockMvc mvc;

	private static UserDto dto;

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(mvc);
		Mockito.reset(userService);
	}

	@BeforeAll
	static void init() {
		dto = new UserDto();
		dto.setId(1);
		dto.setEmail("email11");
		dto.setPassword("password11");
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getUserByIdFailed() {
		Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(dto);

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(403);
	}

	@Test
	void getUserByIdWithOutAuthorization() {
		Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(dto);

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getNotExistingUserById() {
		Mockito.when(userService.getById(Mockito.anyLong())).thenThrow(new EntityNotExistException());

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getAllUser() {
		Mockito.when(userService.getAll(anyLong(), anyLong())).thenReturn(List.of(dto, dto));

		RestAssuredMockMvc.given()
				.when()
				.get(GET_ALL_PATH)
				.then()
				.statusCode(200)
				.rootPath("content")
				.body(
						"id",
						equalTo(List.of(parsIntegerFromLong(dto.getId()), parsIntegerFromLong(dto.getId()))))
				.body("email", equalTo(List.of(dto.getEmail(), dto.getEmail())));
	}

	@Test
	void getAllUserWithOutAuthorization() {
		Mockito.when(userService.getAll(anyLong(), anyLong())).thenReturn(List.of(dto, dto));

		RestAssuredMockMvc.given().when().get(GET_ALL_PATH).then().statusCode(403);
	}

	@Test
	void createUser() {
		Mockito.when(userService.createAndEncodePassword(Mockito.any())).thenReturn(Long.valueOf(1));

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(201);
	}
}

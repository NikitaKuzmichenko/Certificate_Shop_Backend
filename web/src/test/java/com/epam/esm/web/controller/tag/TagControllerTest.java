package com.epam.esm.web.controller.tag;

import static com.epam.esm.web.utils.Utils.parsIntegerFromLong;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.service.implementation.TagServiceImpl;
import com.epam.esm.web.controller.TagController;
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

@WebMvcTest(TagController.class)
@Import(TagTestConfiguration.class)
class TagControllerTest {

	public static final String GET_BY_ID_PATH = "/tags/1";
	public static final String GET_ALL_PATH = "/tags";
	public static final String GET_POPULAR_PATH = "/tags/popular";
	public static final String DELETE_PATH = "/tags/1";
	public static final String CREATE_PATH = "/tags";

	@MockBean private TagServiceImpl tagService;

	@Autowired private MockMvc mvc;

	private static TagDto dto;

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(mvc);
		Mockito.reset(tagService);
	}

	@BeforeAll
	static void init() {
		dto = new TagDto();
		dto.setId(1);
		dto.setName("name");
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getTagById() {
		Mockito.when(tagService.getById(Mockito.anyLong())).thenReturn(dto);

		RestAssuredMockMvc.given()
				.when()
				.get(GET_BY_ID_PATH)
				.then()
				.statusCode(200)
				.body("id", equalTo(parsIntegerFromLong(dto.getId())))
				.body("name", equalTo(dto.getName()));
	}

	@Test
	void geTagByIdtWithOutAuthorization() {
		Mockito.when(tagService.getById(Mockito.anyLong())).thenReturn(dto);

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getNotExistingTagById() {
		Mockito.when(tagService.getById(Mockito.anyLong())).thenThrow(new EntityNotExistException());

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(404);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getAllTags() {
		Mockito.when(tagService.getAll(anyLong(), anyLong())).thenReturn(List.of(dto, dto));

		RestAssuredMockMvc.given()
				.when()
				.get(GET_ALL_PATH)
				.then()
				.statusCode(200)
				.rootPath("content")
				.body(
						"id",
						equalTo(List.of(parsIntegerFromLong(dto.getId()), parsIntegerFromLong(dto.getId()))))
				.body("name", equalTo(List.of(dto.getName(), dto.getName())));
	}

	@Test
	void getAllTagsWithOutAuthorization() {
		Mockito.when(tagService.getAll(anyLong(), anyLong())).thenReturn(List.of(dto, dto));

		RestAssuredMockMvc.given().when().get(GET_POPULAR_PATH).then().statusCode(403);
	}

	@Test
	void getPopularTagWithOutAuthorization() {
		Mockito.when(tagService.getPopular()).thenReturn(dto);

		RestAssuredMockMvc.given().when().get(GET_POPULAR_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void getPopularTag() {
		Mockito.when(tagService.getPopular()).thenReturn(dto);

		RestAssuredMockMvc.given()
				.when()
				.get(GET_POPULAR_PATH)
				.then()
				.statusCode(200)
				.body("id", equalTo(parsIntegerFromLong(dto.getId())))
				.body("name", equalTo(dto.getName()));
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"MODIFY_ALL"})
	void deleteTag() {
		Mockito.doNothing().when(tagService).delete(Mockito.anyLong());

		RestAssuredMockMvc.given().when().delete("/tags/1").then().statusCode(200);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"MODIFY_ALL"})
	void deleteNotExistingTag() {
		Mockito.doThrow(new EntityNotExistException()).when(tagService).delete(Mockito.anyLong());

		RestAssuredMockMvc.given().when().delete(DELETE_PATH).then().statusCode(404);
	}

	@Test
	@WithMockUser(username = "user")
	void deleteTagWithOutAuthorities() {
		Mockito.doNothing().when(tagService).delete(Mockito.anyLong());

		RestAssuredMockMvc.given().when().delete(DELETE_PATH).then().statusCode(403);
	}

	@Test
	void deleteTagWithOutAuthorization() {
		Mockito.doNothing().when(tagService).delete(Mockito.anyLong());
		RestAssuredMockMvc.given().when().delete(DELETE_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"WRITE_ALL"})
	void createTag() {
		Mockito.when(tagService.create(Mockito.any())).thenReturn(Long.valueOf(1));

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(201);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"WRITE_ALL"})
	void failedCreateTag() {
		Mockito.when(tagService.create(Mockito.any())).thenThrow(new DuplicateEntityException());

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(409);
	}

	@Test
	@WithMockUser(username = "user")
	void createTagWithOutAuthorities() {

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(403);
	}

	@Test
	void createTagWithOutAuthorization() {

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(403);
	}
}

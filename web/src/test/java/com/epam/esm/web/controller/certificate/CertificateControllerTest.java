package com.epam.esm.web.controller.certificate;

import static com.epam.esm.web.utils.Utils.parsIntegerFromLong;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.service.implementation.GiftCertificateServiceImpl;
import com.epam.esm.web.controller.GiftCertificateController;
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

@WebMvcTest(GiftCertificateController.class)
@Import(CertificateTestConfiguration.class)
class CertificateControllerTest {

	public static final String GET_BY_ID_PATH = "/certificates/1";
	public static final String GET_ALL_PATH = "/certificates";
	public static final String PUT_PATH = "/certificates/1";
	public static final String PATCH_PATH = "/certificates/1";
	public static final String DELETE_PATH = "/certificates/1";
	public static final String CREATE_PATH = "/certificates";

	@MockBean private GiftCertificateServiceImpl giftCertificateService;

	@Autowired private MockMvc mvc;

	private static GiftCertificateDto dto;

	@BeforeAll
	static void init() {
		dto = new GiftCertificateDto();
		dto.setId(1);
		dto.setName("name");
		dto.setDescription("desc");
		dto.setPrice(BigDecimal.valueOf(10));
		dto.setDuration(2);
		dto.setTags(Set.of(new TagDto(1, "1")));
	}

	@BeforeEach
	void setUp() {
		RestAssuredMockMvc.mockMvc(mvc);
		Mockito.reset(giftCertificateService);
	}

	@Test
	void getCertificateById() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(dto);

		RestAssuredMockMvc.given()
				.when()
				.get(GET_BY_ID_PATH)
				.then()
				.statusCode(200)
				.body("id", equalTo(parsIntegerFromLong(dto.getId())))
				.body("name", equalTo(dto.getName()))
				.body("description", equalTo(dto.getDescription()))
				.body("duration", equalTo(dto.getDuration()))
				.body("creationDate", equalTo(dto.getCreationDate()))
				.body("tags[0].id", equalTo(parsIntegerFromLong(dto.getTags().iterator().next().getId())))
				.body("tags[0].name", equalTo(dto.getTags().iterator().next().getName()));
	}

	@Test
	void getNotExistingCertificateById() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(null);

		RestAssuredMockMvc.given().when().get(GET_BY_ID_PATH).then().statusCode(404);
	}

	@Test
	void getAllCertificate() {
		Mockito.when(giftCertificateService.selectAll(any(), any(), anyBoolean(), anyLong(), anyLong()))
				.thenReturn(List.of(dto, dto));

		int tagId = parsIntegerFromLong(dto.getTags().iterator().next().getId());
		String tagName = dto.getTags().iterator().next().getName();
		RestAssuredMockMvc.given()
				.when()
				.get(GET_ALL_PATH)
				.then()
				.statusCode(200)
				.rootPath("content")
				.body(
						"id",
						equalTo(List.of(parsIntegerFromLong(dto.getId()), parsIntegerFromLong(dto.getId()))))
				.body("name", equalTo(List.of(dto.getName(), dto.getName())))
				.body("description", equalTo(List.of(dto.getDescription(), dto.getDescription())))
				.body("duration", equalTo(List.of(dto.getDuration(), dto.getDuration())))
				.body("tags[0].id", equalTo(List.of(tagId)))
				.body("tags[0].name", equalTo(List.of(tagName)));
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"MODIFY_ALL"})
	void putCertificate() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(dto);
		Mockito.when(giftCertificateService.replace(any())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.put(PUT_PATH)
				.then()
				.statusCode(200);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"READ_ALL"})
	void putCertificateWithOutAuthorities() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(dto);
		Mockito.when(giftCertificateService.replace(any())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.put(PUT_PATH)
				.then()
				.statusCode(403);
	}

	@Test
	void putCertificateWithOutAuthorization() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(dto);
		Mockito.when(giftCertificateService.replace(any())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.put(PUT_PATH)
				.then()
				.statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"MODIFY_ALL"})
	void patchCertificate() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(dto);
		Mockito.when(giftCertificateService.replace(any())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.patch(PATCH_PATH)
				.then()
				.statusCode(200);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"MODIFY_ALL"})
	void patchNotExistingCertificate() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(null);
		Mockito.when(giftCertificateService.replace(any())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.patch(PATCH_PATH)
				.then()
				.statusCode(404);
	}

	@Test
	@WithMockUser(username = "user")
	void patchCertificateWithOutAuthorities() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(dto);
		Mockito.when(giftCertificateService.replace(any())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.patch(PATCH_PATH)
				.then()
				.statusCode(403);
	}

	@Test
	void patchCertificateWithOutAuthorization() {
		Mockito.when(giftCertificateService.getById(Mockito.anyLong())).thenReturn(dto);
		Mockito.when(giftCertificateService.replace(any())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.patch(PATCH_PATH)
				.then()
				.statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"MODIFY_ALL"})
	void deleteCertificate() {
		Mockito.when(giftCertificateService.delete(Mockito.anyLong())).thenReturn(true);

		RestAssuredMockMvc.given().when().delete(DELETE_PATH).then().statusCode(200);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"MODIFY_ALL"})
	void deleteNotExistingCertificate() {
		Mockito.when(giftCertificateService.delete(Mockito.anyLong())).thenReturn(false);

		RestAssuredMockMvc.given().when().delete(DELETE_PATH).then().statusCode(404);
	}

	@Test
	@WithMockUser(username = "user")
	void deleteCertificateWithOutAuthorities() {
		Mockito.when(giftCertificateService.delete(Mockito.anyLong())).thenReturn(true);

		RestAssuredMockMvc.given().when().delete(DELETE_PATH).then().statusCode(403);
	}

	@Test
	void deleteCertificateWithOutAuthorization() {
		Mockito.when(giftCertificateService.delete(Mockito.anyLong())).thenReturn(true);

		RestAssuredMockMvc.given().when().delete(DELETE_PATH).then().statusCode(403);
	}

	@Test
	@WithMockUser(
			username = "user",
			authorities = {"WRITE_ALL"})
	void createCertificate() {
		Mockito.when(giftCertificateService.create(Mockito.any())).thenReturn(Long.valueOf(1));

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
	void failedCreateCertificate() {
		Mockito.when(giftCertificateService.create(Mockito.any())).thenReturn(null);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(400);
	}

	@Test
	@WithMockUser(username = "user")
	void createCertificateWithOutAuthorities() {
		Mockito.when(giftCertificateService.delete(Mockito.anyLong())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(403);
	}

	@Test
	void createCertificateWithOutAuthorization() {
		Mockito.when(giftCertificateService.delete(Mockito.anyLong())).thenReturn(true);

		RestAssuredMockMvc.given()
				.contentType("application/json")
				.body(dto)
				.when()
				.post(CREATE_PATH)
				.then()
				.statusCode(403);
	}
}

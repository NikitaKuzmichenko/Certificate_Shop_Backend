package com.epam.esm.service.implementation;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.dto.mapper.GiftCertificateDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.BadInputException;
import com.epam.esm.exception.DuplicateEntityException;
import com.epam.esm.exception.EntityNotExistException;
import com.epam.esm.repository.compound.certificate.GiftCertificateRepository;
import com.epam.esm.repository.compound.certificate.impl.function.ConfigurableFunction;
import com.epam.esm.repository.compound.certificate.impl.function.DescriptionPartCondition;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.implementation.GiftCertificateServiceImpl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class GiftCertificateServiceImplTest {
	@Mock
	public static final CertificateCriteriaBuilderService criteriaBuilder
			= mock(CertificateCriteriaBuilderService.class);

	@Mock
	public static final GiftCertificateRepository repository = mock(GiftCertificateRepository.class);

	@Mock public static final TagService tagService = mock(TagService.class);

	private static final GiftCertificateServiceImpl service =
			new GiftCertificateServiceImpl(repository, tagService);

	private static GiftCertificate giftCertificate;
	private static GiftCertificateDto giftCertificateDto;

	private static List<GiftCertificate> giftCertificates;

	@BeforeAll
	public static void initAll() {
		ZonedDateTime creationDate =
				LocalDateTime.of(2021, 12, 4, 0, 0, 0)
                        .atZone(ZoneId.systemDefault());

		ZonedDateTime creationDate2 =
				LocalDateTime.of(2021, 12, 4, 1, 0, 0)
                        .atZone(ZoneId.systemDefault());

		List<Tag> tags = new ArrayList<>();

		Tag tag1 = new Tag();
		tag1.setId(1);
		tag1.setName("tag1");

		Tag tag2 = new Tag();
		tag2.setId(2);
		tag2.setName("tag2");
		tags.add(tag1);
		tags.add(tag2);

		giftCertificate = new GiftCertificate();
		giftCertificate.setName("name1");
		giftCertificate.setDescription("desc");
		giftCertificate.setPrice(new BigDecimal("21.20"));
		giftCertificate.setDuration(10);
		giftCertificate.setCreationDate(creationDate);
		giftCertificate.setLastUpdateDate(creationDate);
		giftCertificate.setId(1);
		giftCertificate.setTags(tags);

		giftCertificates = new LinkedList<>();
		giftCertificates.add(giftCertificate);
		giftCertificates.add(giftCertificate);

		giftCertificateDto = GiftCertificateDtoMapper.mapGiftCertificateToDto(giftCertificate);
	}

	@AfterEach
	public void resetMocks() {
		Mockito.reset(repository, tagService);
	}

	@Test
	void getGiftCertificateById() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertEquals(giftCertificateDto, service.getById(1));
	}

	@Test
	void getNotExistingGiftCertificateById() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotExistException.class,()->service.getById(1));
	}

	@Test
	void createCertificate() {
		when(repository.save(anyObject())).thenReturn(giftCertificate);
		Assertions.assertEquals(giftCertificate.getId(), service.create(giftCertificateDto));
	}

	@Test
	void failedCreateCertificate() {
		when(repository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		Assertions.assertThrows(DuplicateEntityException.class, () -> service.create(giftCertificateDto));
	}

	@Test
	void updateCertificate() {
		when(repository.save(anyObject())).thenReturn(giftCertificate);
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertEquals(giftCertificate.getId(), service.patch(giftCertificateDto));
	}

	@Test
	void failedUpdateCertificate() {
		when(repository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertThrows(DuplicateEntityException.class, () -> service.patch(giftCertificateDto));
	}

	@Test
	void updateOrCreateCertificate() {
		when(repository.save(anyObject())).thenReturn(giftCertificate);
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertEquals(giftCertificate.getId(), service.patchOrCreate(giftCertificateDto));
	}

	@Test
	void failedUpdateOrCreateCertificate() {
		when(repository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertThrows(DuplicateEntityException.class, () -> service.patchOrCreate(giftCertificateDto));
	}

	@Test
	void deleteCertificate() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertDoesNotThrow(()->service.delete(1));
	}

	@Test
	void failedDeleteCertificate() {
		when(repository.findById(anyLong())).thenReturn(Optional.empty());
		Assertions.assertThrows(EntityNotExistException.class,()->service.delete(1));
	}

	@Test
	void getAll() {
		when(repository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(giftCertificates));
		Assertions.assertEquals(
				giftCertificates.stream()
						.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
						.collect(Collectors.toList()),
				service.getAll(1,1));
	}

	@ParameterizedTest
	@CsvSource({"-1,-1", "-1,1", "1,-1"})
	void getAllUserWitchIncorrectLimitAndOffset(int limit,int offset) {
		when(repository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(giftCertificates));
		Assertions.assertThrows(BadInputException.class,
				()->service.getAll(limit, offset));
	}

	@Test
	void getAllWithCriteria() {
		List<ConfigurableFunction> functions = new ArrayList<>();
		functions.add(new DescriptionPartCondition("1"));

		when(repository.getAll(anyList(),anyLong(),anyLong())).thenReturn(giftCertificates);
		when(criteriaBuilder.getConditions()).thenReturn(functions);

		Assertions.assertEquals(
				giftCertificates.stream()
						.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
						.collect(Collectors.toList()),
				service.getAll(criteriaBuilder,1,1));
	}

	@Test
	void getAllWithEmptyCriteria() {
		when(repository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(giftCertificates));
		when(criteriaBuilder.getConditions()).thenReturn(new ArrayList<>());
		Assertions.assertEquals(
				giftCertificates.stream()
						.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
						.collect(Collectors.toList()),
				service.getAll(criteriaBuilder,1,1));
	}

	@Test
	void getAllWithNullCriteria() {
		when(repository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(giftCertificates));
		Assertions.assertEquals(
				giftCertificates.stream()
						.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
						.collect(Collectors.toList()),
				service.getAll(null,1,1));
	}

	@ParameterizedTest
	@CsvSource({"-1,-1", "-1,1", "1,-1"})
	void getAllWithCriteriaWitchIncorrectLimitAndOffset(int limit,int offset) {
		when(repository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(giftCertificates));
		Assertions.assertThrows(BadInputException.class,
				()->service.getAll(limit, offset));
	}

	@Test
	void getAllWithSort() {
		when(repository.getAll(anyString(),anyBoolean(),anyLong(),anyLong())).thenReturn(giftCertificates);

		Assertions.assertEquals(
				giftCertificates.stream()
						.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
						.collect(Collectors.toList()),
				service.getAll("1",true,1,1));
	}

	@Test
	void getAllWithNullSort() {
		when(repository.getAll(anyString(),anyBoolean(),anyLong(),anyLong())).thenReturn(giftCertificates);
		when(repository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(giftCertificates));

		Assertions.assertEquals(
				giftCertificates.stream()
						.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
						.collect(Collectors.toList()),
				service.getAll(null,true,1,1));
	}

	@ParameterizedTest
	@CsvSource({"-1,-1", "-1,1", "1,-1"})
	void getAllWithSortWitchIncorrectLimitAndOffset(int limit,int offset) {
		when(repository.findAll((Pageable) anyObject())).thenReturn(new PageImpl<>(giftCertificates));
		Assertions.assertThrows(BadInputException.class,
				()->service.getAll(limit, offset));
	}
}

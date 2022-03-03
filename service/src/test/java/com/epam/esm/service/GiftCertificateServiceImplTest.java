package com.epam.esm.service;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.epam.esm.dto.mapper.GiftCertificateDtoMapper;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.compound.certificate.GiftCertificateRepository;
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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;

public class GiftCertificateServiceImplTest {

	@Mock
	public static final GiftCertificateRepository repository = mock(GiftCertificateRepository.class);

	@Mock public static final TagService tagService = mock(TagService.class);

	private static final GiftCertificateServiceImpl service =
			new GiftCertificateServiceImpl(repository, tagService);

	private static GiftCertificate giftCertificate;
	private static List<GiftCertificate> giftCertificates;

	@BeforeAll
	public static void initAll() {
		ZonedDateTime creationDate =
				LocalDateTime.of(2021, 12, 4, 0, 0, 0).atZone(ZoneId.systemDefault());

		ZonedDateTime creationDate2 =
				LocalDateTime.of(2021, 12, 4, 1, 0, 0).atZone(ZoneId.systemDefault());

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

		List<Tag> tags2 = new ArrayList<>();
		tags2.add(null);

		GiftCertificate giftCertificate2 = new GiftCertificate();
		giftCertificate2.setName("name2");
		giftCertificate2.setDescription("desc");
		giftCertificate2.setPrice(new BigDecimal("21.20"));
		giftCertificate2.setDuration(10);
		giftCertificate2.setCreationDate(creationDate2);
		giftCertificate2.setLastUpdateDate(creationDate2);
		giftCertificate2.setId(2);
		giftCertificate2.setTags(tags2);

		giftCertificates = new LinkedList<>();
		giftCertificates.add(giftCertificate);
		giftCertificates.add(giftCertificate2);
	}

	@AfterEach
	public void resetMocks() {
		Mockito.reset(repository, tagService);
	}

	@Test
	void getGiftCertificateById() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertEquals(
				GiftCertificateDtoMapper.mapGiftCertificateToDto(giftCertificate), service.getById(1));
	}

	@Test
	void getNotExistingGiftCertificateById() {
		when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		Assertions.assertNull(service.getById(1));
	}

	@Test
	void createCertificate() {
		when(repository.save(anyObject())).thenReturn(giftCertificate);
		Assertions.assertEquals(
				giftCertificate.getId(),
				service.create(GiftCertificateDtoMapper.mapGiftCertificateToDto(giftCertificate)));
	}

	@Test
	void failedCreateCertificate() {
		when(repository.save(anyObject())).thenThrow(new DuplicateKeyException(""));
		Assertions.assertThrows(
				DuplicateKeyException.class,
				() -> service.create(GiftCertificateDtoMapper.mapGiftCertificateToDto(giftCertificate)));
	}

	@Test
	void updateCertificate() {
		when(repository.save(anyObject())).thenReturn(giftCertificate);
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertTrue(
				service.replace(GiftCertificateDtoMapper.mapGiftCertificateToDto(giftCertificate)));
	}

	@Test
	void failedUpdateCertificate() {
		when(repository.save(anyObject())).thenThrow(new DuplicateKeyException("q"));
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertThrows(
				DuplicateKeyException.class,
				() -> service.replace(GiftCertificateDtoMapper.mapGiftCertificateToDto(giftCertificate)));
	}

	@Test
	void deleteCertificate() {
		when(repository.findById(anyLong())).thenReturn(Optional.of(giftCertificate));
		Assertions.assertTrue(service.delete(1));
	}

	@Test
	void failedDeleteCertificate() {
		when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
		Assertions.assertFalse(service.delete(1));
	}

	@Test
	void getAll() {
		when(repository.findAll()).thenReturn(giftCertificates);
		Assertions.assertEquals(
				giftCertificates.stream()
						.map(GiftCertificateDtoMapper::mapGiftCertificateToDto)
						.collect(Collectors.toList()),
				service.selectAll());
	}
}

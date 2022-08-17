package com.epam.esm.repository.compound.certificate;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GiftCertificateRepository
		extends PagingAndSortingRepository<GiftCertificate, Long>, CustomGiftCertificateRepository {}

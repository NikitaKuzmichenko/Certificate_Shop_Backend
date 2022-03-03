package com.epam.esm.web.controller.certificate;

import com.epam.esm.service.TagService;
import com.epam.esm.service.implementation.TagServiceImpl;
import com.epam.esm.web.GeneralTestConfiguration;
import com.epam.esm.web.representation.assembler.GiftCertificateRepresentationAssembler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({GeneralTestConfiguration.class})
public class CertificateTestConfiguration {

	@Bean
	GiftCertificateRepresentationAssembler giftCertificateRepresentationAssembler() {
		return new GiftCertificateRepresentationAssembler();
	}

	@Bean
	TagService tagService() {
		return new TagServiceImpl(null);
	}
}

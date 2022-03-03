package com.epam.esm.web.controller.user;

import com.epam.esm.web.GeneralTestConfiguration;
import com.epam.esm.web.representation.assembler.UserRepresentationAssembler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({GeneralTestConfiguration.class})
public class UserTestConfiguration {

	@Bean
	UserRepresentationAssembler userRepresentationAssembler() {
		return new UserRepresentationAssembler();
	}
}

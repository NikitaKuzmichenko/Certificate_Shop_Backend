package com.epam.esm.web.controller.tag;

import com.epam.esm.web.GeneralTestConfiguration;
import com.epam.esm.web.representation.assembler.TagRepresentationAssembler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({GeneralTestConfiguration.class})
public class TagTestConfiguration {

	@Bean
	TagRepresentationAssembler tagRepresentationAssembler() {
		return new TagRepresentationAssembler();
	}
}

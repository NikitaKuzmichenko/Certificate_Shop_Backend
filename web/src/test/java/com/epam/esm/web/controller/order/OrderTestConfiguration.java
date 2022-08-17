package com.epam.esm.web.controller.order;

import com.epam.esm.web.GeneralTestConfiguration;
import com.epam.esm.web.representation.assembler.OrderRepresentationAssembler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({GeneralTestConfiguration.class})
public class OrderTestConfiguration {

	@Bean
	OrderRepresentationAssembler orderRepresentationAssembler() {
		return new OrderRepresentationAssembler();
	}
}

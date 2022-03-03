package com.epam.esm.web.configuration;

import com.epam.esm.Config;
import com.epam.esm.service.CertificateCriteriaBuilderService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.implementation.CertificateCriteriaBuilderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@Import(Config.class)
public class ApplicationConfig {

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
		source.setDefaultEncoding("UTF-8");
		source.setBasename("classpath:locales/ErrorMsg");
		return source;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("local");
		return localeChangeInterceptor;
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		return new InternalResourceViewResolver();
	}

	@Bean
	@RequestScope
	@Autowired
	public CertificateCriteriaBuilderService getCertificateCriteriaBuilder(TagService service) {
		return new CertificateCriteriaBuilderServiceImpl(service);
	}
}

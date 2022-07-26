package com.epam.esm.web.configuration;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@PropertySource(value = "classpath:swagger.properties")
@EnableSwagger2
public class SwaggerConfig {

	@Value("${swagger.apiInfo.title:}")
	private String apiInfoTitle;

	@Value("${swagger.apiInfo.description:}")
	private String apiInfoDesc;

	@Value("${swagger.apiInfo.version:}")
	private String apiInfoVersion;

	@Value("${swagger.apiInfo.termsOfService.url:}")
	private String apiInfoTermsOfServiceUrl;

	@Value("${swagger.apiInfo.contacts.name:}")
	private String apiInfoContactsName;

	@Value("${swagger.apiInfo.contacts.url:}")
	private String apiInfoContactsUrl;

	@Value("${swagger.apiInfo.contacts.email:}")
	private String apiInfoContactsEmail;

	@Value("${swagger.apiInfo.license}")
	private String apiInfoLicense;

	@Value("${swagger.apiInfo.license.url:}")
	private String apiInfoLicenseUrl;

	@Value("${swagger.apiInfo.vendorExtensions:}")
	private String[] apiInfoVendorExtensions;

	@Value("${swagger.authorization.scope.name:}")
	private String authorizationScopeName;

	@Value("${swagger.authorization.scope.description:}")
	private String authorizationScopeDesc;

	@Value("${swagger.apiKey.headerName:}")
	private String apiKeyHeaderName;

	@Value("${swagger.apiKey.keyName:}")
	private String apiKeyName;

	@Value("${swagger.apiKey.passKeyAs:}")
	private String apiKeyPassing;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.securityContexts(List.of(securityContext()))
				.securitySchemes(List.of(apiKey()))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.epam.esm.web.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		Contact contact;

		if (apiInfoContactsName.isEmpty()
				&& apiInfoContactsUrl.isEmpty()
				&& apiInfoContactsEmail.isEmpty()) {
			contact = null;
		} else {
			contact = new Contact(apiInfoContactsName, apiInfoContactsUrl, apiInfoContactsEmail);
		}

		return new ApiInfo(
				apiInfoTitle,
				apiInfoDesc,
				apiInfoVersion,
				apiInfoTermsOfServiceUrl,
				contact,
				apiInfoLicense,
				apiInfoLicenseUrl,
				Collections.emptyList());
	}

	private ApiKey apiKey() {
		return new ApiKey(apiKeyHeaderName, apiKeyName, apiKeyPassing);
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope =
				new AuthorizationScope(authorizationScopeName, authorizationScopeDesc);
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return List.of(new SecurityReference(apiKeyHeaderName, authorizationScopes));
	}

	@Bean
	UiConfiguration uiConfig() {
		return UiConfigurationBuilder.builder().defaultModelsExpandDepth(-1).build();
	}
}

package com.code.blog.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getInfo())
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
	
	private ApiInfo getInfo() {
		
		return new ApiInfo("Blogging Application: Backend Course", 
				"Practice Project", "1.0", "Terms of Service",
				new Contact("Harvard22", "http://localhost:9090/v3/api-docs", "hvvashishtha2209@gmail.com"),
				"License of APIS", "API license URL", Collections.emptyList());
	}
}

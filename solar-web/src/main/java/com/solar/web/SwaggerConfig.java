package com.solar.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger配置
 *
 * @author longlanghua
 * @since 2017-12-27
 */
@EnableWebMvc
@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.solar.web.controller")) // 注意修改此处的包名
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("消息规范").description("接口验证") // 任意，请稍微规范点
				.termsOfServiceUrl("http://localhost:8080/solar-web/swagger-ui.html") // 将“url”换成自己的ip:port
				.contact(new Contact("longlh", "", "s@a.com")) // 无所谓（这里是作者的别称）
				.version("0.0.1").build();
	}
}
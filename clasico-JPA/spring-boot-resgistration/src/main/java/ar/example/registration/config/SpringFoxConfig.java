package ar.example.registration.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ar.example.registration.controller.UserController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {    
	
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
        		.apiInfo(apiInfo())
        		.select()
        		.apis(RequestHandlerSelectors.basePackage("ar.example.registration.controller"))
                .paths(PathSelectors.ant("/v1.0/**"))
        		.build();
    }
    
    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("User REST API"
        		, "User Register."
        		, "API Register"
        		, "Terms of service"
        		,null 
        		, "License of API"
        		, "URL"
        		, Collections.emptyList());
        return apiInfo;
    }
    
}



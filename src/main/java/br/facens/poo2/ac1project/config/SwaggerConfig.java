package br.facens.poo2.ac1project.config;

import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  
  private static final String BASE_PACKAGE = "br.facens.poo2.ac1project.controller";
  private static final String API_TITLE = "POO2/AC1 - Event Scheduler";
  private static final String API_DESCRIPTION = "Event Scheduler REST API";
  private static final String CONTACT_NAME = "@vaaaarlos";
  private static final String CONTACT_GITHUB = "https://github.com/vaaaarlos";
  private static final String CONTACT_EMAIL = "kadu.cerq@gmail.com";

  @Bean
  public Docket api() { 
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(basePackage(BASE_PACKAGE))
        .paths(any())
        .build()
        .apiInfo(buildApiInfo());
  }

  private ApiInfo buildApiInfo() {
    return new ApiInfoBuilder()
        .title(API_TITLE)
        .description(API_DESCRIPTION)
        .version("1.0.0")
        .contact(new Contact(CONTACT_NAME, CONTACT_GITHUB, CONTACT_EMAIL))
        .build();
  }

}

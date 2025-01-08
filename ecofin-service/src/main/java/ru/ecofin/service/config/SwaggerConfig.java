package ru.ecofin.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Ecofin API")
            .description("API для работы Ecofin")
            .version("v1")
            .license(new License()
                .name("MIT License")
                .url("https://mit-license.org/"))
            .contact(new io.swagger.v3.oas.models.info.Contact()
                .email("DronovEgorVl@yandex.com")));
  }
}

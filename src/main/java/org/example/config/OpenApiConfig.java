package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Rest Book Shelf API",
                version = "1.0.0",
                description = "Сервис учёта книг"
        )
)
@Configuration
public class OpenApiConfig {
}

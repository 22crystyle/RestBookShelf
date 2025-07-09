package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Rest Book Shelf API",
                version = "1.0.0",
                description = "Сервис учёта книг"
        ),
        servers = {
                @Server(
                        url = "http://localhost:1024",
                        description = "Локальный сервер"
                )
        }
)
@Configuration
public class OpenApiConfig {
}

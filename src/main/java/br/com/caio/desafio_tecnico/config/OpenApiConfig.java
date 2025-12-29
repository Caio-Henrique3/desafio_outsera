package br.com.caio.desafio_tecnico.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Desafio Tecnico API",
                description = "API para consulta de intervalos de premios do Golden Raspberry Awards.",
                version = "v1"
        )
)
public class OpenApiConfig {
}

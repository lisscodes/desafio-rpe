package com.evoluir.fintech.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API - Sistema de Clientes e Faturas")
                        .version("1.0.0")
                        .description("Documentação da API REST para gerenciamento de clientes, faturas e pagamentos."));
    }
}

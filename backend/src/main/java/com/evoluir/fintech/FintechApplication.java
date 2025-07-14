package com.evoluir.fintech;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "API - Sistema de Clientes e Faturas",
				version = "1.0.0",
				description = "Documentação da API REST para gerenciamento de clientes, faturas e pagamentos."
		)
)

@SpringBootApplication
public class FintechApplication {

	public static void main(String[] args) {
		SpringApplication.run(FintechApplication.class, args);
	}

}

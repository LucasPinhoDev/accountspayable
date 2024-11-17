package com.example.accountspayable.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;

public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Accounts Payable API")
                        .version("1.0")
                        .description("API para gerenciar contas a pagar, incluindo criação, atualização, exclusão, e mais."));
    }
}

package com.project.backend.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.Contact
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Backend API")
                    .description("Spring Boot Kotlin Backend API")
                    .version("1.0.0")
                    .contact(
                        Contact()
                            .name("Team")
                            .url("https://example.com")
                    )
            )
    }
}

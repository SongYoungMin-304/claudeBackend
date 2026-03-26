package com.project.backend.domain.hello.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/hello")
@Tag(name = "Hello", description = "Hello World API")
class HelloController {

    @GetMapping
    @Operation(summary = "Hello World 반환", description = "간단한 Hello World 메시지를 반환합니다.")
    fun hello(): Map<String, String> {
        return mapOf("message" to "Hello, World!")
    }
}

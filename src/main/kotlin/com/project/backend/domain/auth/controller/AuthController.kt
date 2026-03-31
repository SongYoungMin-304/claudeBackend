package com.project.backend.domain.auth.controller

import com.project.backend.domain.auth.dto.LoginRequest
import com.project.backend.domain.auth.dto.LoginResponse
import com.project.backend.domain.auth.dto.SignupRequest
import com.project.backend.domain.auth.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Authentication", description = "Authentication APIs")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @Operation(summary = "User login")
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "User signup")
    @PostMapping("/signup")
    fun signup(@Valid @RequestBody request: SignupRequest): ResponseEntity<Map<String, Any>> {
        val user = authService.signup(request)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapOf(
                "message" to "Signup successful",
                "userId" to user.id,
                "email" to user.email
            ))
    }
}

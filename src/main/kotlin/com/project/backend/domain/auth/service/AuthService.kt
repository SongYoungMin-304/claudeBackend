package com.project.backend.domain.auth.service

import com.project.backend.domain.auth.dto.LoginRequest
import com.project.backend.domain.auth.dto.LoginResponse
import com.project.backend.domain.auth.dto.SignupRequest
import com.project.backend.domain.user.entity.User
import com.project.backend.domain.user.repository.UserRepository
import com.project.backend.global.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid email or password")
        }

        val token = jwtTokenProvider.generateToken(user.id, user.email)

        return LoginResponse(
            accessToken = token,
            userId = user.id,
            email = user.email
        )
    }

    @Transactional
    fun signup(request: SignupRequest): User {
        if (userRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("Email already exists")
        }

        val user = User(
            name = request.name,
            email = request.email,
            password = passwordEncoder.encode(request.password)!!
        )

        return userRepository.save(user)
    }
}

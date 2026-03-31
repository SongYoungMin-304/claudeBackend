package com.project.backend.global.config

import com.project.backend.domain.user.entity.User
import com.project.backend.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class DataInitializer {

    @Bean
    fun initData(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ): ApplicationRunner = ApplicationRunner {
        if (userRepository.count() == 0L) {
            val users = listOf(
                User(name = "admin", email = "admin@test.com", password = passwordEncoder.encode("1234")!!),
                User(name = "user1", email = "user1@test.com", password = passwordEncoder.encode("1234")!!),
                User(name = "user2", email = "user2@test.com", password = passwordEncoder.encode("1234")!!)
            )
            userRepository.saveAll(users)
            println("✅ Initial users created")
        }
    }
}

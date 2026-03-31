package com.project.backend.domain.user.service

import com.project.backend.domain.user.entity.User
import com.project.backend.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun getAllUsers(): List<User> = userRepository.findAll()
    fun getUserById(id: Long): User? = userRepository.findById(id).orElse(null)
    fun deleteUser(id: Long): Boolean = if (userRepository.existsById(id)) { userRepository.deleteById(id); true } else false
}

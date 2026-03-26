package com.project.backend.domain.user.service

import com.project.backend.domain.user.entity.User
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong

@Service
class UserService {
    private val users = mutableMapOf<Long, User>()
    private val idCounter = AtomicLong(1)

    fun getAllUsers(): List<User> = users.values.toList()

    fun getUserById(id: Long): User? = users[id]

    fun createUser(name: String, email: String): User {
        val id = idCounter.getAndIncrement()
        val user = User(id, name, email)
        users[id] = user
        return user
    }

    fun deleteUser(id: Long): Boolean = users.remove(id) != null
}

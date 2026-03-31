package com.project.backend.domain.user.controller

import com.project.backend.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "사용자 관리 API")
class UserController(private val userService: UserService) {

    @GetMapping
    @Operation(summary = "모든 사용자 조회", description = "등록된 모든 사용자를 조회합니다.")
    fun getAllUsers(): ResponseEntity<List<Map<String, Any>>> {
        val users = userService.getAllUsers().map {
            mapOf("id" to it.id, "name" to it.name, "email" to it.email)
        }
        return ResponseEntity.ok(users)
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 조회", description = "ID로 특정 사용자를 조회합니다.")
    fun getUserById(@PathVariable id: Long): ResponseEntity<Map<String, Any>> {
        val user = userService.getUserById(id)
        return if (user != null) {
            ResponseEntity.ok(mapOf("id" to user.id, "name" to user.name, "email" to user.email))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "사용자 삭제", description = "ID로 사용자를 삭제합니다.")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return if (userService.deleteUser(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

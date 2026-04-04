package com.project.backend.domain.post.controller

import com.project.backend.domain.post.dto.PostNavigationResponse
import com.project.backend.domain.post.entity.Post
import com.project.backend.domain.post.service.PostService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Post", description = "게시글 API")
class PostController(private val postService: PostService) {

    @GetMapping
    @Operation(summary = "모든 게시글 조회", description = "등록된 모든 게시글을 최신순으로 조회합니다.")
    fun getAllPosts(): ResponseEntity<List<Post>> {
        return ResponseEntity.ok(postService.getAllPosts())
    }

    @GetMapping("/{id}")
    @Operation(summary = "게시글 조회", description = "ID로 특정 게시글을 조회합니다.")
    fun getPostById(@PathVariable id: Long): ResponseEntity<Post> {
        val post = postService.getPostById(id)
        return if (post != null) ResponseEntity.ok(post) else ResponseEntity.notFound().build()
    }

    @GetMapping("/{id}/navigation")
    @Operation(summary = "이전글/다음글 조회", description = "현재 게시글의 이전글과 다음글 정보를 조회합니다.")
    fun getPostNavigation(@PathVariable id: Long): ResponseEntity<PostNavigationResponse> {
        return try {
            val navigation = postService.getPostNavigation(id)
            ResponseEntity.ok(navigation)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    fun createPost(@RequestBody request: CreatePostRequest): ResponseEntity<Post> {
        val post = postService.createPost(request.title, request.content, request.author)
        return ResponseEntity.status(HttpStatus.CREATED).body(post)
    }

    @PutMapping("/{id}")
    @Operation(summary = "게시글 수정", description = "ID로 게시글을 수정합니다.")
    fun updatePost(@PathVariable id: Long, @RequestBody request: CreatePostRequest): ResponseEntity<Post> {
        val post = postService.updatePost(id, request.title, request.content, request.author)
        return if (post != null) ResponseEntity.ok(post) else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "게시글 삭제", description = "ID로 게시글을 삭제합니다.")
    fun deletePost(@PathVariable id: Long): ResponseEntity<Void> {
        return if (postService.deletePost(id)) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}

data class CreatePostRequest(
    val title: String,
    val content: String,
    val author: String
)

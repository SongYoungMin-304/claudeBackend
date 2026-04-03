package com.project.backend.domain.comment.controller

import com.project.backend.domain.comment.dto.*
import com.project.backend.domain.comment.service.CommentService
import com.project.backend.domain.comment.service.UnauthorizedException
import com.project.backend.global.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@Tag(name = "Comment", description = "댓글 API")
class CommentController(private val commentService: CommentService) {

    @GetMapping
    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 모든 댓글을 조회합니다")
    fun getComments(@PathVariable postId: Long): ResponseEntity<CommentListResponse> {
        return try {
            val response = commentService.getCommentsByPostId(postId)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/count")
    @Operation(summary = "댓글 수 조회", description = "특정 게시글의 댓글 수를 조회합니다")
    fun getCommentCount(@PathVariable postId: Long): ResponseEntity<CommentCountResponse> {
        return try {
            val response = commentService.getCommentCount(postId)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping
    @Operation(summary = "댓글 작성", description = "특정 게시글에 댓글을 작성합니다")
    fun createComment(
        @PathVariable postId: Long,
        @Valid @RequestBody request: CreateCommentRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<CommentResponse> {
        return try {
            val response = commentService.createComment(postId, principal.userId, request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException -> ResponseEntity.badRequest().build()
                else -> ResponseEntity.notFound().build()
            }
        }
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "내가 작성한 댓글을 수정합니다")
    fun updateComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @Valid @RequestBody request: UpdateCommentRequest,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<CommentResponse> {
        return try {
            val response = commentService.updateComment(postId, commentId, principal.userId, request)
            ResponseEntity.ok(response)
        } catch (e: Exception) {
            when (e) {
                is UnauthorizedException -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
                else -> ResponseEntity.notFound().build()
            }
        }
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "내가 작성한 댓글을 삭제합니다")
    fun deleteComment(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<Void> {
        return try {
            commentService.deleteComment(postId, commentId, principal.userId)
            ResponseEntity.noContent().build()
        } catch (e: Exception) {
            when (e) {
                is UnauthorizedException -> ResponseEntity.status(HttpStatus.FORBIDDEN).build()
                else -> ResponseEntity.notFound().build()
            }
        }
    }
}

package com.project.backend.domain.like.controller

import com.project.backend.domain.like.dto.LikeResponse
import com.project.backend.domain.like.service.LikeService
import com.project.backend.global.security.UserPrincipal
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "Like", description = "좋아요 API")
class LikeController(private val likeService: LikeService) {

    @PostMapping("/api/v1/posts/{postId}/like")
    @Operation(summary = "게시글 좋아요 토글", description = "좋아요가 있으면 취소, 없으면 추가합니다")
    fun togglePostLike(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<LikeResponse> {
        val response = likeService.togglePostLike(postId, principal.userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/posts/{postId}/like")
    @Operation(summary = "게시글 좋아요 상태 조회", description = "좋아요 수와 현재 사용자의 좋아요 여부를 조회합니다")
    fun getPostLikeStatus(
        @PathVariable postId: Long,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<LikeResponse> {
        val response = likeService.getPostLikeStatus(postId, principal.userId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/posts/{postId}/comments/{commentId}/like")
    @Operation(summary = "댓글 좋아요 토글", description = "좋아요가 있으면 취소, 없으면 추가합니다")
    fun toggleCommentLike(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<LikeResponse> {
        val response = likeService.toggleCommentLike(commentId, principal.userId)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/posts/{postId}/comments/{commentId}/like")
    @Operation(summary = "댓글 좋아요 상태 조회", description = "좋아요 수와 현재 사용자의 좋아요 여부를 조회합니다")
    fun getCommentLikeStatus(
        @PathVariable postId: Long,
        @PathVariable commentId: Long,
        @AuthenticationPrincipal principal: UserPrincipal
    ): ResponseEntity<LikeResponse> {
        val response = likeService.getCommentLikeStatus(commentId, principal.userId)
        return ResponseEntity.ok(response)
    }
}

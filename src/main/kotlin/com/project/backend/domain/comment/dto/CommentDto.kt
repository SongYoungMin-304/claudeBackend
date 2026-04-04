package com.project.backend.domain.comment.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class CommentResponse(
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val authorName: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?,
    val parentId: Long?,
    val replies: List<CommentResponse> = emptyList()
)

data class CommentListResponse(
    val comments: List<CommentResponse>,
    val totalCount: Int
)

data class CreateCommentRequest(
    @field:NotBlank(message = "댓글 내용은 필수입니다")
    @field:Size(min = 1, max = 1000, message = "댓글은 1~1000자까지 입력 가능합니다")
    val content: String,
    val parentId: Long? = null
)

data class UpdateCommentRequest(
    @field:NotBlank(message = "댓글 내용은 필수입니다")
    @field:Size(min = 1, max = 1000, message = "댓글은 1~1000자까지 입력 가능합니다")
    val content: String
)

data class CommentCountResponse(
    val postId: Long,
    val count: Long
)

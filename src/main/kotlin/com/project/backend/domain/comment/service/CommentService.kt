package com.project.backend.domain.comment.service

import com.project.backend.domain.comment.dto.*
import com.project.backend.domain.comment.entity.Comment
import com.project.backend.domain.comment.repository.CommentRepository
import com.project.backend.domain.post.repository.PostRepository
import com.project.backend.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    fun getCommentsByPostId(postId: Long): CommentListResponse {
        if (!postRepository.existsById(postId)) {
            throw PostNotFoundException("게시글을 찾을 수 없습니다")
        }

        val comments = commentRepository.findByPostIdOrderByCreatedAtDesc(postId)
        val commentResponses = comments.map { it.toResponse() }

        return CommentListResponse(
            comments = commentResponses,
            totalCount = commentResponses.size
        )
    }

    fun getCommentCount(postId: Long): CommentCountResponse {
        if (!postRepository.existsById(postId)) {
            throw PostNotFoundException("게시글을 찾을 수 없습니다")
        }

        val count = commentRepository.countByPostId(postId)
        return CommentCountResponse(postId = postId, count = count)
    }

    fun createComment(postId: Long, authorId: Long, request: CreateCommentRequest): CommentResponse {
        if (!postRepository.existsById(postId)) {
            throw PostNotFoundException("게시글을 찾을 수 없습니다")
        }

        val comment = Comment(
            postId = postId,
            authorId = authorId,
            content = request.content
        )

        val savedComment = commentRepository.save(comment)
        return savedComment.toResponse()
    }

    fun updateComment(postId: Long, commentId: Long, authorId: Long, request: UpdateCommentRequest): CommentResponse {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException("댓글을 찾을 수 없습니다") }

        if (comment.postId != postId) {
            throw CommentNotFoundException("해당 게시글의 댓글이 아닙니다")
        }

        if (comment.authorId != authorId) {
            throw UnauthorizedException("작성자만 수정할 수 있습니다")
        }

        comment.content = request.content
        comment.updatedAt = LocalDateTime.now()

        val updatedComment = commentRepository.save(comment)
        return updatedComment.toResponse()
    }

    fun deleteComment(postId: Long, commentId: Long, authorId: Long) {
        val comment = commentRepository.findById(commentId)
            .orElseThrow { CommentNotFoundException("댓글을 찾을 수 없습니다") }

        if (comment.postId != postId) {
            throw CommentNotFoundException("해당 게시글의 댓글이 아닙니다")
        }

        if (comment.authorId != authorId) {
            throw UnauthorizedException("작성자만 삭제할 수 있습니다")
        }

        commentRepository.delete(comment)
    }

    private fun Comment.toResponse(): CommentResponse {
        val authorName = userRepository.findById(this.authorId)
            .map { it.name }
            .orElse("Unknown")

        return CommentResponse(
            id = this.id,
            postId = this.postId,
            authorId = this.authorId,
            authorName = authorName,
            content = this.content,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}

class PostNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String) : RuntimeException(message)

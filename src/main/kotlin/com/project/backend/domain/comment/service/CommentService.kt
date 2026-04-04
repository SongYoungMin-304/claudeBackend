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

        val topLevelComments = commentRepository.findByPostIdAndParentIdIsNullOrderByCreatedAtDesc(postId)
        val commentResponses = topLevelComments.map { buildCommentWithReplies(it) }
        val totalCount = commentRepository.countByPostId(postId).toInt()

        return CommentListResponse(
            comments = commentResponses,
            totalCount = totalCount
        )
    }

    private fun buildCommentWithReplies(comment: Comment): CommentResponse {
        val authorName = userRepository.findById(comment.authorId)
            .map { it.name }
            .orElse("Unknown")

        val replies = commentRepository.findByParentIdOrderByCreatedAtAsc(comment.id)
            .map { reply ->
                CommentResponse(
                    id = reply.id,
                    postId = reply.postId,
                    authorId = reply.authorId,
                    authorName = userRepository.findById(reply.authorId).map { it.name }.orElse("Unknown"),
                    content = reply.content,
                    createdAt = reply.createdAt,
                    updatedAt = reply.updatedAt,
                    parentId = reply.parentId,
                    replies = emptyList()
                )
            }

        return CommentResponse(
            id = comment.id,
            postId = comment.postId,
            authorId = comment.authorId,
            authorName = authorName,
            content = comment.content,
            createdAt = comment.createdAt,
            updatedAt = comment.updatedAt,
            parentId = comment.parentId,
            replies = replies
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

        val parentId = request.parentId
        if (parentId != null) {
            val parentComment = commentRepository.findById(parentId)
                .orElseThrow { CommentNotFoundException("부모 댓글을 찾을 수 없습니다") }

            if (parentComment.postId != postId) {
                throw IllegalArgumentException("부모 댓글은 같은 게시글에 속해야 합니다")
            }

            if (parentComment.parentId != null) {
                throw IllegalArgumentException("답글의 답글은 불가합니다")
            }
        }

        val comment = Comment(
            postId = postId,
            authorId = authorId,
            content = request.content,
            parentId = parentId
        )

        val savedComment = commentRepository.save(comment)
        return buildCommentWithReplies(savedComment.copy(parentId = parentId))
    }

    private fun Comment.copy(parentId: Long?): Comment {
        return Comment(
            id = this.id,
            postId = this.postId,
            authorId = this.authorId,
            parentId = parentId,
            content = this.content,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
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
        return CommentResponse(
            id = updatedComment.id,
            postId = updatedComment.postId,
            authorId = updatedComment.authorId,
            authorName = userRepository.findById(updatedComment.authorId).map { it.name }.orElse("Unknown"),
            content = updatedComment.content,
            createdAt = updatedComment.createdAt,
            updatedAt = updatedComment.updatedAt,
            parentId = updatedComment.parentId,
            replies = emptyList()
        )
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

        deleteReplies(commentId)
        commentRepository.delete(comment)
    }

    private fun deleteReplies(commentId: Long) {
        val replies = commentRepository.findByParentIdOrderByCreatedAtAsc(commentId)
        replies.forEach { commentRepository.delete(it) }
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
            updatedAt = this.updatedAt,
            parentId = this.parentId,
            replies = emptyList()
        )
    }
}

class PostNotFoundException(message: String) : RuntimeException(message)
class CommentNotFoundException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String) : RuntimeException(message)

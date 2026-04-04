package com.project.backend.domain.like.service

import com.project.backend.domain.comment.entity.Comment
import com.project.backend.domain.comment.repository.CommentRepository
import com.project.backend.domain.like.dto.LikeResponse
import com.project.backend.domain.like.entity.Like
import com.project.backend.domain.like.entity.TargetType
import com.project.backend.domain.like.repository.LikeRepository
import com.project.backend.domain.post.entity.Post
import com.project.backend.domain.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LikeService(
    private val likeRepository: LikeRepository,
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository
) {

    @Transactional
    fun togglePostLike(postId: Long, userId: Long): LikeResponse {
        val existingLike = likeRepository.findByUserIdAndTargetTypeAndTargetId(
            userId, TargetType.POST, postId
        )

        val isLiked: Boolean
        val likeCount: Long

        if (existingLike != null) {
            likeRepository.delete(existingLike)
            likeCount = updatePostLikeCount(postId, -1)
            isLiked = false
        } else {
            likeRepository.save(Like(userId = userId, targetType = TargetType.POST, targetId = postId))
            likeCount = updatePostLikeCount(postId, 1)
            isLiked = true
        }

        return LikeResponse(
            targetType = TargetType.POST.name,
            targetId = postId,
            likeCount = likeCount,
            isLiked = isLiked
        )
    }

    @Transactional(readOnly = true)
    fun getPostLikeStatus(postId: Long, userId: Long): LikeResponse {
        val isLiked = likeRepository.findByUserIdAndTargetTypeAndTargetId(
            userId, TargetType.POST, postId
        ) != null

        val likeCount = likeRepository.countByTargetTypeAndTargetId(TargetType.POST, postId)

        return LikeResponse(
            targetType = TargetType.POST.name,
            targetId = postId,
            likeCount = likeCount,
            isLiked = isLiked
        )
    }

    @Transactional
    fun toggleCommentLike(commentId: Long, userId: Long): LikeResponse {
        val existingLike = likeRepository.findByUserIdAndTargetTypeAndTargetId(
            userId, TargetType.COMMENT, commentId
        )

        val isLiked: Boolean
        val likeCount: Long

        if (existingLike != null) {
            likeRepository.delete(existingLike)
            likeCount = updateCommentLikeCount(commentId, -1)
            isLiked = false
        } else {
            likeRepository.save(Like(userId = userId, targetType = TargetType.COMMENT, targetId = commentId))
            likeCount = updateCommentLikeCount(commentId, 1)
            isLiked = true
        }

        return LikeResponse(
            targetType = TargetType.COMMENT.name,
            targetId = commentId,
            likeCount = likeCount,
            isLiked = isLiked
        )
    }

    @Transactional(readOnly = true)
    fun getCommentLikeStatus(commentId: Long, userId: Long): LikeResponse {
        val isLiked = likeRepository.findByUserIdAndTargetTypeAndTargetId(
            userId, TargetType.COMMENT, commentId
        ) != null

        val likeCount = likeRepository.countByTargetTypeAndTargetId(TargetType.COMMENT, commentId)

        return LikeResponse(
            targetType = TargetType.COMMENT.name,
            targetId = commentId,
            likeCount = likeCount,
            isLiked = isLiked
        )
    }

    private fun updatePostLikeCount(postId: Long, delta: Long): Long {
        val post = postRepository.findById(postId).orElse(null) ?: return 0
        val newCount = (post.likeCount + delta).coerceAtLeast(0)
        post.likeCount = newCount
        postRepository.save(post)
        return newCount
    }

    private fun updateCommentLikeCount(commentId: Long, delta: Long): Long {
        val comment = commentRepository.findById(commentId).orElse(null) ?: return 0
        val newCount = (comment.likeCount + delta).coerceAtLeast(0)
        comment.likeCount = newCount
        commentRepository.save(comment)
        return newCount
    }
}

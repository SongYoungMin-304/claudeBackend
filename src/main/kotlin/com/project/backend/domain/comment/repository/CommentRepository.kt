package com.project.backend.domain.comment.repository

import com.project.backend.domain.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByPostIdOrderByCreatedAtDesc(postId: Long): List<Comment>
    fun countByPostId(postId: Long): Long
    fun findByIdAndAuthorId(commentId: Long, authorId: Long): Comment?
}

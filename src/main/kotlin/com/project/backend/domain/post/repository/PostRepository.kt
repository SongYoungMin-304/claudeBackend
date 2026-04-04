package com.project.backend.domain.post.repository

import com.project.backend.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.id < :id ORDER BY p.id DESC LIMIT 1")
    fun findPrevPost(@Param("id") id: Long): Post?
    
    @Query("SELECT p FROM Post p WHERE p.id > :id ORDER BY p.id ASC LIMIT 1")
    fun findNextPost(@Param("id") id: Long): Post?
}

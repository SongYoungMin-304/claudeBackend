package com.project.backend.domain.comment.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "comments")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "post_id", nullable = false)
    val postId: Long,

    @Column(name = "author_id", nullable = false)
    val authorId: Long,

    @Column(name = "parent_id")
    val parentId: Long? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
) {
    protected constructor() : this(
        id = 0,
        postId = 0,
        authorId = 0,
        parentId = null,
        content = "",
        createdAt = LocalDateTime.now(),
        updatedAt = null
    )

    constructor(postId: Long, authorId: Long, content: String, parentId: Long? = null) : this(
        postId = postId,
        authorId = authorId,
        parentId = parentId,
        content = content,
        createdAt = LocalDateTime.now(),
        updatedAt = null
    )
}

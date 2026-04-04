package com.project.backend.domain.post.dto

import java.time.LocalDateTime

data class PostNavigationResponse(
    val prev: PostNavItem?,
    val next: PostNavItem?
)

data class PostNavItem(
    val id: Long,
    val title: String,
    val createdAt: LocalDateTime
)

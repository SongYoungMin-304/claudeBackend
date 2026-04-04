package com.project.backend.domain.like.dto

data class LikeResponse(
    val targetType: String,
    val targetId: Long,
    val likeCount: Long,
    val isLiked: Boolean
)

package com.project.backend.domain.like.repository

import com.project.backend.domain.like.entity.Like
import com.project.backend.domain.like.entity.TargetType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LikeRepository : JpaRepository<Like, Long> {

    fun findByUserIdAndTargetTypeAndTargetId(
        userId: Long,
        targetType: TargetType,
        targetId: Long
    ): Like?

    fun countByTargetTypeAndTargetId(targetType: TargetType, targetId: Long): Long

    fun findByUserIdAndTargetType(userId: Long, targetType: TargetType): List<Like>
}

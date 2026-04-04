package com.project.backend.domain.like.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "likes",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "target_type", "target_id"])]
)
class Like(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    val targetType: TargetType,

    @Column(name = "target_id", nullable = false)
    val targetId: Long,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    protected constructor() : this(
        id = 0,
        userId = 0,
        targetType = TargetType.POST,
        targetId = 0,
        createdAt = LocalDateTime.now()
    )
}

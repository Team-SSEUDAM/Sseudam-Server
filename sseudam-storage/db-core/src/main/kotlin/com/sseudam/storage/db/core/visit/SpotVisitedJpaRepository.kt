package com.sseudam.storage.db.core.visit

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface SpotVisitedJpaRepository :
    JpaRepository<SpotVisitedEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByUserId(userId: Long): List<SpotVisitedEntity>

    fun countBySpotId(spotId: Long): Long

    fun findAllByUserIdAndDate(
        userId: Long,
        today: LocalDate,
    ): List<SpotVisitedEntity>

    fun findFirstByUserIdAndSpotIdOrderByCreatedAtDesc(
        userId: Long,
        spotId: Long,
    ): SpotVisitedEntity?

    fun findAllByUserIdAndSpotIdAndDate(
        userId: Long,
        spotId: Long,
        today: LocalDate,
    ): MutableList<SpotVisitedEntity>
}

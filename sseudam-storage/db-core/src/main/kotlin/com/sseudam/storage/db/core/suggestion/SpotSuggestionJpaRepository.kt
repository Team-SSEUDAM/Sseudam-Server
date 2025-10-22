package com.sseudam.storage.db.core.suggestion

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import com.sseudam.suggestion.SuggestionStatus
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository

interface SpotSuggestionJpaRepository :
    JpaRepository<SpotSuggestionEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByUserIdAndDeletedAtIsNull(userId: Long): List<SpotSuggestionEntity>

    fun findByAddressSiteAndDeletedAtIsNull(site: String): SpotSuggestionEntity?

    fun findByPointAndDeletedAtIsNull(point: Point): SpotSuggestionEntity?

    fun findByPointAndStatusAndDeletedAtIsNull(
        point: Point,
        status: SuggestionStatus,
    ): SpotSuggestionEntity?

    fun findByIdAndDeletedAtIsNull(suggestionId: Long): SpotSuggestionEntity?

    fun existsBySpotNameAndDeletedAtIsNull(name: String): Boolean
}

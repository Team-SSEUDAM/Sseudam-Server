package com.sseudam.storage.db.core.suggestion

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.locationtech.jts.geom.Point
import org.springframework.data.jpa.repository.JpaRepository

interface SpotSuggestionJpaRepository :
    JpaRepository<SpotSuggestionEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByUserId(userId: Long): List<SpotSuggestionEntity>

    fun findByAddressSiteAndDeletedAtIsNull(site: String): SpotSuggestionEntity?

    fun findByPointAndDeletedAtIsNull(point: Point): SpotSuggestionEntity?

    fun existsBySpotName(name: String): Boolean
}

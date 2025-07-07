package com.sseudam.storage.db.core.suggestion

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface SpotSuggestionJpaRepository :
    JpaRepository<SpotSuggestionEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findAllByUserId(userId: Long): List<SpotSuggestionEntity>

    // TODO: 수정 필요
    fun findLastBySpotId(spotId: Long): SpotSuggestionEntity?

    fun findByAddressSite(site: String): SpotSuggestionEntity?

    fun existsBySpotName(name: String): Boolean
}

package com.sseudam.storage.db.core.suggestion.reject

import com.linecorp.kotlinjdsl.support.spring.data.jpa.repository.KotlinJdslJpqlExecutor
import org.springframework.data.jpa.repository.JpaRepository

interface SuggestionRejectJpaRepository :
    JpaRepository<SuggestionRejectEntity, Long>,
    KotlinJdslJpqlExecutor {
    fun findBySuggestionId(suggestionId: Long): SuggestionRejectEntity?
}

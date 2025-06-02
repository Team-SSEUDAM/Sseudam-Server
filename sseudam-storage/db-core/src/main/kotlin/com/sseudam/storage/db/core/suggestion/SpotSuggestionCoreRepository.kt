package com.sseudam.storage.db.core.suggestion

import com.sseudam.suggestion.SpotSuggestionRepository
import org.springframework.stereotype.Repository

@Repository
class SpotSuggestionCoreRepository(
    private val spotSuggestionJpaRepository: SpotSuggestionJpaRepository,
) : SpotSuggestionRepository

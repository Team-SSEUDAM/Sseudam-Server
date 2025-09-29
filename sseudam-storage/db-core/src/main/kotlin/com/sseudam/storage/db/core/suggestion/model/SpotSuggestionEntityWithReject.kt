package com.sseudam.storage.db.core.suggestion.model

import com.sseudam.storage.db.core.suggestion.SpotSuggestionEntity

data class SpotSuggestionEntityWithReject(
    val entity: SpotSuggestionEntity,
    val rejectReason: String?,
)

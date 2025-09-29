package com.sseudam.suggestion.reject

import java.time.LocalDateTime

class SuggestionReject {
    data class Create(
        val suggestionId: Long,
        val reason: String,
    )

    data class Info(
        val suggestionId: Long,
        val reason: String?,
        val createdAt: LocalDateTime,
    )
}

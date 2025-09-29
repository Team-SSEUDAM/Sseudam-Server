package com.sseudam.suggestion

data class UpdateSuggestionCommand(
    val suggestionId: Long,
    val status: SuggestionStatus,
    val reason: String?,
)

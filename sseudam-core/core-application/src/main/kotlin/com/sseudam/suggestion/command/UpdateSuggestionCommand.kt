package com.sseudam.suggestion.command

import com.sseudam.suggestion.SuggestionStatus

data class UpdateSuggestionCommand(
    val suggestionId: Long,
    val status: SuggestionStatus,
    val reason: String?,
)

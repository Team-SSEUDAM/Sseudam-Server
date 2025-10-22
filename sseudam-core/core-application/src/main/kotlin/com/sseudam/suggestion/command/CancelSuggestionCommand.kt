package com.sseudam.suggestion.command

data class CancelSuggestionCommand(
    val userId: Long,
    val suggestionId: Long,
)

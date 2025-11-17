package com.sseudam.suggestion.command

import com.sseudam.suggestion.SuggestionStatus

data class UpdateSuggestionCommand(
    val suggestionId: Long,
    val status: SuggestionStatus,
    val reason: String?,
    val rewardPoint: Long = 0L, // 승인 시 지급되는 포인트
)

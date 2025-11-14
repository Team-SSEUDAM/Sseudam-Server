package com.sseudam.suggestion.event

import com.sseudam.suggestion.SpotSuggestion

data class SuggestionUpdateEvent(
    val suggestion: SpotSuggestion.Info,
    val reason: String?,
    val rewardPoint: Long = 0L, // 승인 시 지급되는 포인트
)

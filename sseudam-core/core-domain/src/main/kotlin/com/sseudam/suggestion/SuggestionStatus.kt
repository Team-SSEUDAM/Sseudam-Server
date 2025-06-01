package com.sseudam.suggestion

enum class SuggestionStatus(
    val displayName: String,
) {
    APPROVE("승인"),
    REJECT("반려"),
    WAITING("대기"),
}

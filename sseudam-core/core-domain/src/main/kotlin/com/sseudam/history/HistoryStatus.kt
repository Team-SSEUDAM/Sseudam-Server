package com.sseudam.history

enum class HistoryStatus(
    val displayName: String,
) {
    APPROVE("승인"),
    REJECT("반려"),
    WAITING("대기"),
}

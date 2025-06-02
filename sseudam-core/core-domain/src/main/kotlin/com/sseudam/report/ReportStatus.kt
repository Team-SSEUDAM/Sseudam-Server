package com.sseudam.report

enum class ReportStatus(
    val displayName: String,
) {
    APPROVE("승인"),
    REJECT("반려"),
    WAITING("대기"),
}

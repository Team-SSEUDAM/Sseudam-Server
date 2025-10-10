package com.sseudam.notification

enum class NotificationType {
    APPROVE_SUGGESTION, // 제보 승인
    REJECT_SUGGESTION, // 제보 반려
    APPROVE_REPORT, // 신고 승인
    REJECT_REPORT, // 신고 반려
    NEW_PET_SEASON, // 새로운 펫 시즌 오픈
    ANONYMOUS_VISITED_SPOT, // 누군가 내 제보 장소 방문
    ADMIN_PUSH, // 관리자 푸시
    REGULAR, // 정기 푸시 알림 (화요일 오전 9시)
    ;

    companion object {
        fun of(type: String): NotificationType =
            when (type) {
                "APPROVE_SUGGESTION" -> APPROVE_SUGGESTION
                "REJECT_SUGGESTION" -> REJECT_SUGGESTION
                "APPROVE_REPORT" -> APPROVE_REPORT
                "REJECT_REPORT" -> REJECT_REPORT
                "NEW_PET_SEASON" -> NEW_PET_SEASON
                "ANONYMOUS_VISITED_SPOT" -> ANONYMOUS_VISITED_SPOT
                "ADMIN_PUSH" -> ADMIN_PUSH
                "REGULAR" -> REGULAR
                else -> throw IllegalArgumentException("Unknown NotificationType: $type")
            }
    }
}

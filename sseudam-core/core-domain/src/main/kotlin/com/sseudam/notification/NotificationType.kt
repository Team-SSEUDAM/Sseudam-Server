package com.sseudam.notification

enum class NotificationType {
    APPROVE_SUGGESTION,
    REJECT_SUGGESTION,
    APPROVE_REPORT,
    REJECT_REPORT,
    NEW_PET_SEASON,
    ANONYMOUS_VISITED_SPOT,
    ADMIN_PUSH,
    REGULAR,
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

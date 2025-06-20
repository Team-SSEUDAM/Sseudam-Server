package com.sseudam.pet

import java.time.LocalDateTime

/** 펫 포인트 히스토리 */
class PetPointHistory {
    data class Create(
        val userPetId: Long,
        val event: PointEvent,
        val previousPoint: Long,
        val additionalPoint: Long,
    )

    data class Info(
        val id: Long,
        val userPetId: Long,
        val event: PointEvent,
        val previousPoint: Long,
        val additionalPoint: Long,
        val createdAt: LocalDateTime,
    )

    enum class PointEvent {
        SPOT_VISITED,
        REPORT,
        REPORT_APPROVED,
        SUGGESTION,
        SUGGESTION_APPROVED,
        ATTENDANCE,
    }
}

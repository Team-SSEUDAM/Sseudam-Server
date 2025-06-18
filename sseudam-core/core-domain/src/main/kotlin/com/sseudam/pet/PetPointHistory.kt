package com.sseudam.pet

import java.time.LocalDateTime

/** 펫 포인트 히스토리 */
class PetPointHistory {
    data class Create(
        val userId: Long,
        val petId: Long,
        val event: PointEvent,
        val point: Long,
    )

    data class Info(
        val id: Long,
        val userId: Long,
        val petId: Long,
        val event: PointEvent,
        val point: Long,
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

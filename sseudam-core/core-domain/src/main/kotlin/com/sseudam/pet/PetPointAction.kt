package com.sseudam.pet

enum class PetPointAction(
    val point: Long,
) {
    SPOT_VISITED(5),
    REPORT(5),
    REPORT_APPROVED(15),
    SUGGESTION(5),
    SUGGESTION_APPROVED(15),
    ATTENDANCE(2),
}

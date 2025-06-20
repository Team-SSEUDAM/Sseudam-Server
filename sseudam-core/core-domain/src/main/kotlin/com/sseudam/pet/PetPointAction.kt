package com.sseudam.pet

enum class PetPointAction(
    val point: Long,
) {
    /** 방문 인증 */
    SPOT_VISITED(5),

    /** 신고하기 */
    REPORT(5),

    /** 신고 승인하기 */
    REPORT_APPROVED(15),

    /** 제보하기 */
    SUGGESTION(5),

    /** 제보 승인하기 */
    SUGGESTION_APPROVED(15),

    /** 출석 체크 */
    ATTENDANCE(2),
}

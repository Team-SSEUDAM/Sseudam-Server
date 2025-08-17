package com.sseudam.attendance

import java.time.LocalDate
import java.time.LocalDateTime

class Attendance {
    /** 출석 생성
     * @property userId 사용자 ID
     * @property date 출석 날짜
     * @property continuity 연속 출석 일수
     */
    data class Create(
        val userId: Long,
        val date: LocalDate,
        val continuity: Int = 1,
    )

    /** 출석 정보
     * @property id 출석 ID
     * @property userId 사용자 ID
     * @property date 출석 날짜
     * @property continuity 연속 출석 일수
     * @property createdAt 생성 일시 (출석 시간)
     */
    data class Info(
        val id: Long,
        val userId: Long,
        val date: LocalDate,
        val continuity: Int = 1,
        val createdAt: LocalDateTime,
    )

    data class Complete(
        val id: Long,
        val userId: Long,
        val date: LocalDate,
        val continuity: Int = 1,
        val isToday: Boolean,
        val createdAt: LocalDateTime,
    )
}

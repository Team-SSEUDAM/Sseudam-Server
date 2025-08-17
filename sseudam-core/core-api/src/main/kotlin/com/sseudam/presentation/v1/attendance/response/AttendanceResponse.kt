package com.sseudam.presentation.v1.attendance.response

import com.sseudam.attendance.Attendance
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

@Schema(
    description = "출석 체크 응답 Json",
    example = """
        {
            "userId": 1,
            "date": "2025-06-30",
            "continuity": 3,
            "isContinuity": true,
            "createdAt": "2025-06-30T12:00:00"
        }
    """,
)
data class AttendanceResponse(
    @Schema(description = "사용자 ID", example = "1")
    val userId: Long,
    @Schema(description = "출석 날짜", example = "2025-06-30")
    val date: LocalDate,
    @Schema(description = "출석 연속 횟수", example = "3")
    val continuity: Int,
    @Schema(description = "연속 출석 여부", example = "true")
    val isContinuity: Boolean,
    @Schema(description = "출석 시간", example = "2025-06-30T12:00:00")
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(
            isContinuity: Boolean,
            attendance: Attendance.Complete,
        ): AttendanceResponse =
            AttendanceResponse(
                userId = attendance.userId,
                date = attendance.date,
                continuity = attendance.continuity,
                isContinuity = isContinuity,
                createdAt = attendance.createdAt,
            )
    }
}

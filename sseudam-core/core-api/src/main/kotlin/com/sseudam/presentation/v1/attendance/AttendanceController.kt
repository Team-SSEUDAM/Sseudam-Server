package com.sseudam.presentation.v1.attendance

import com.sseudam.attendance.AttendanceFacade
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.attendance.response.AttendanceResponse
import com.sseudam.user.User
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping

@Tag(name = "🗓️ Attendance API", description = "출석 관련 API")
@ApiV1Controller
class AttendanceController(
    private val attendanceFacade: AttendanceFacade,
) {
    @Operation(
        summary = "출석 체크",
        description = "사용자의 출석을 체크합니다. 출석은 매일 00:00에 초기화됩니다.",
    )
    @PostMapping("/attendance")
    fun attendanceCheck(user: User): AttendanceResponse {
        val attendanceResult =
            attendanceFacade.todayAttendance(
                userId = user.id,
            )
        return AttendanceResponse.from(attendanceResult)
    }
}

package com.sseudam.attendance

import java.time.LocalDate

class Attendance {
    data class Create(
        val userId: Long,
        val date: LocalDate,
        val continuousCount: Int = 0,
    )
}

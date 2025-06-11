package com.sseudam.admin.domain

import java.time.LocalDateTime

class Admin {
    data class Info(
        val id: Long,
        val name: String,
        val loginId: String,
        val createdAt: LocalDateTime,
    )
}

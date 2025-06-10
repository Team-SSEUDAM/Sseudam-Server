package com.sseudam.admin

import java.time.LocalDateTime

data class Admin(
    val id: Long,
    val name: String,
    val loginId: String,
    val createdAt: LocalDateTime,
)

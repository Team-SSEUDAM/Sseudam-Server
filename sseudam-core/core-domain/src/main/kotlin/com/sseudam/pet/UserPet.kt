package com.sseudam.pet

import java.time.LocalDateTime

class UserPet {
    data class Create(
        val userId: Long,
        val nickname: String,
        val petId: Long,
        val point: Long,
    )

    data class Info(
        val id: Long,
        val userId: Long,
        val nickname: String,
        val petId: Long,
        val point: Long,
        val createdAt: LocalDateTime,
    )
}

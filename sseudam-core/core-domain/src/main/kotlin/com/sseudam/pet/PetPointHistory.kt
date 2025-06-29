package com.sseudam.pet

import java.time.LocalDateTime

/** 펫 포인트 히스토리 */
class PetPointHistory {
    data class Create(
        val userPetId: Long,
        val pointAction: PetPointAction,
        val previousPoint: Long,
        val additionalPoint: Long,
    )

    data class Info(
        val id: Long,
        val userPetId: Long,
        val pointAction: PetPointAction,
        val previousPoint: Long,
        val additionalPoint: Long,
        val createdAt: LocalDateTime,
    )
}

package com.sseudam.pet

import java.time.LocalDateTime
import java.time.Month

/** 펫 정보 */
class Pet {
    data class Create(
        val name: String,
        val levelType: LevelType,
        val year: Int,
        val monthly: Month,
    )

    data class Info(
        val id: Long,
        val name: String,
        val levelType: LevelType,
        val year: Int,
        val monthly: Month,
        val createdAt: LocalDateTime,
    )

    enum class LevelType(
        val level: Int,
        val adjective: String,
    ) {
        LEVEL_1(1, "작고 소중한 "),
        LEVEL_2(2, "호기심 가득한 "),
        LEVEL_3(3, "쑥쑥 자라나는 "),
        LEVEL_4(4, "왕 커서 귀여운 "),
        SPECIAL(5, "스페셜 "),
    }
}

package com.sseudam.pet

import java.time.LocalDateTime
import java.time.Month

/** 펫 정보 */
class Pet {
    data class Create(
        val name: String,
        val levelType: LevelType,
        val monthly: Month,
    )

    data class Info(
        val id: Long,
        val name: String,
        val levelType: LevelType,
        val monthly: Month,
        val createdAt: LocalDateTime,
    )

    enum class LevelType(
        val level: Int,
        val description: String,
    ) {
        LEVEL_1(1, "작고 소중한 냥이"),
        LEVEL_2(2, "호기심 가득한 냥이"),
        LEVEL_3(3, "쑥쑥 자라나는 냥이"),
        LEVEL_4(4, "왕 커서 귀여운 냥이"),
        LEVEL_5(5, "스페셜 냥이"),
    }
}

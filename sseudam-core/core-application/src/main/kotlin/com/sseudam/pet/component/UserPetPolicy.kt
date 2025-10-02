package com.sseudam.pet.component

import com.sseudam.pet.Pet
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Component
import java.time.Month

@Component
class UserPetPolicy {
    fun getLevelType(cumulativePoint: Long): Pet.LevelType =
        when {
            cumulativePoint in LevelStandard.LEVEL_1_RANGE -> Pet.LevelType.LEVEL_1
            cumulativePoint in LevelStandard.LEVEL_2_RANGE -> Pet.LevelType.LEVEL_2
            cumulativePoint in LevelStandard.LEVEL_3_RANGE -> Pet.LevelType.LEVEL_3
            cumulativePoint in LevelStandard.LEVEL_4_RANGE -> Pet.LevelType.LEVEL_4
            LevelStandard.SPECIAL_MIN <= cumulativePoint -> Pet.LevelType.SPECIAL
            else -> throw ErrorException(ErrorType.INVALID_CUMULATIVE_POINT)
        }

    fun getMaxLevelStandard(levelType: Pet.LevelType): Long =
        when (levelType) {
            Pet.LevelType.LEVEL_1 -> LevelStandard.LEVEL_2_MIN.toLong()
            Pet.LevelType.LEVEL_2 -> LevelStandard.LEVEL_3_MIN.toLong()
            Pet.LevelType.LEVEL_3 -> LevelStandard.LEVEL_4_MIN.toLong()
            Pet.LevelType.LEVEL_4 -> LevelStandard.SPECIAL_MIN.toLong()
            Pet.LevelType.SPECIAL -> 0L
        }

    fun getMinLevelStandard(levelType: Pet.LevelType): Long =
        when (levelType) {
            Pet.LevelType.LEVEL_1 -> LevelStandard.LEVEL_1_MIN.toLong()
            Pet.LevelType.LEVEL_2 -> LevelStandard.LEVEL_2_MIN.toLong()
            Pet.LevelType.LEVEL_3 -> LevelStandard.LEVEL_3_MIN.toLong()
            Pet.LevelType.LEVEL_4 -> LevelStandard.LEVEL_4_MIN.toLong()
            Pet.LevelType.SPECIAL -> LevelStandard.SPECIAL_MIN.toLong()
        }

    fun getSeasonByPetInfo(petInfo: Pet.Info): String = "%04d-%02d".format(petInfo.year, petInfo.monthly.value)

    fun getSeasonByYearMonth(
        year: Int,
        month: Month,
    ): String = "%04d-%02d".format(year, month.value)
}

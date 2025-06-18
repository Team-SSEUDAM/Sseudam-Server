package com.sseudam.pet

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Component

@Component
class UserPetPolicy {
    fun getLevelType(cumulativePoint: Long): Pet.LevelType =
        when {
            cumulativePoint in 0..20 -> Pet.LevelType.LEVEL_1
            cumulativePoint in 21..110 -> Pet.LevelType.LEVEL_2
            cumulativePoint in 111..220 -> Pet.LevelType.LEVEL_3
            cumulativePoint in 221..300 -> Pet.LevelType.LEVEL_4
            301 <= cumulativePoint -> Pet.LevelType.LEVEL_5
            else -> throw ErrorException(ErrorType.INVALID_CUMULATIVE_POINT)
        }
}

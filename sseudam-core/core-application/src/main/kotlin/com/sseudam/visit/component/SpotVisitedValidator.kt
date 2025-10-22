package com.sseudam.visit.component

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.visit.SpotVisited
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class SpotVisitedValidator {
    fun verifyVisited(
        todayVisited: List<SpotVisited.Info>,
        todayVisitedSpot: SpotVisited.Info?,
    ) {
        if (todayVisited.size >= 5 && todayVisitedSpot == null) {
            throw ErrorException(ErrorType.SPOT_VISITED_LIMIT_EXCEEDED)
        }
        if (todayVisitedSpot != null) {
            val lastVisitTime = todayVisited.maxBy { it.visitedAt }.visitedAt
            if (lastVisitTime.isAfter(LocalDateTime.now().minusMinutes(5))) {
                throw ErrorException(ErrorType.SPOT_VISITED_ALREADY)
            }
            if (todayVisited.size >= 5) {
                throw ErrorException(ErrorType.SPOT_VISITED_LIMIT_EXCEEDED)
            }
        }
    }
}

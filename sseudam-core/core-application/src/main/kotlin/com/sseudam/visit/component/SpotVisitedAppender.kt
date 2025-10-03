package com.sseudam.visit.component

import com.sseudam.visit.SpotVisited
import com.sseudam.visit.repository.SpotVisitedRepository
import org.springframework.stereotype.Component

@Component
class SpotVisitedAppender(
    private val spotVisitedRepository: SpotVisitedRepository,
) {
    fun append(spotVisited: SpotVisited.Create) = spotVisitedRepository.create(spotVisited)
}

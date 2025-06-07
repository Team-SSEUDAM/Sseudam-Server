package com.sseudam.visit

import org.springframework.stereotype.Component

@Component
class SpotVisitedAppender(
    private val spotVisitedRepository: SpotVisitedRepository,
) {
    fun append(spotVisited: SpotVisited.Create) = spotVisitedRepository.create(spotVisited)
}

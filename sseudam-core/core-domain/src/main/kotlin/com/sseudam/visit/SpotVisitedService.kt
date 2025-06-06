package com.sseudam.visit

import org.springframework.stereotype.Service

@Service
class SpotVisitedService(
    private val spotVisitedAppender: SpotVisitedAppender,
) {
    fun append(spotVisited: SpotVisited.Create): SpotVisited.Info = spotVisitedAppender.append(spotVisited)
}

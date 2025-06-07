package com.sseudam.visit

import org.springframework.stereotype.Service

@Service
class SpotVisitedService(
    private val spotVisitedAppender: SpotVisitedAppender,
    private val spotVisitedReader: SpotVisitedReader,
) {
    fun append(spotVisited: SpotVisited.Create): SpotVisited.Info = spotVisitedAppender.append(spotVisited)

    fun findAll(userId: Long): List<SpotVisited.Info> = spotVisitedReader.readByUserId(userId)
}

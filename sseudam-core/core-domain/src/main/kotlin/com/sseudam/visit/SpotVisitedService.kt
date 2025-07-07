package com.sseudam.visit

import org.springframework.stereotype.Service

@Service
class SpotVisitedService(
    private val spotVisitedAppender: SpotVisitedAppender,
    private val spotVisitedReader: SpotVisitedReader,
) {
    fun append(spotVisited: SpotVisited.Create) = spotVisitedAppender.append(spotVisited)

    fun findAllByUser(userId: Long): List<SpotVisited.Info> = spotVisitedReader.readByUserId(userId)

    fun countBySpotId(spotId: Long): Long = spotVisitedReader.countBySpotId(spotId)
}

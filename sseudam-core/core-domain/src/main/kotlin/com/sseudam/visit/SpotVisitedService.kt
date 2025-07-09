package com.sseudam.visit

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SpotVisitedService(
    private val spotVisitedAppender: SpotVisitedAppender,
    private val spotVisitedReader: SpotVisitedReader,
) {
    fun append(spotVisited: SpotVisited.Create) = spotVisitedAppender.append(spotVisited)

    fun findAllByUser(userId: Long): List<SpotVisited.Info> = spotVisitedReader.readByUserId(userId)

    fun findTodaySpotVisitedByUserAndSpot(userId: Long): List<SpotVisited.Info> = spotVisitedReader.readTodayAllBy(userId, LocalDate.now())

    fun findTodaySpotVisitedByUserAndSpot(
        userId: Long,
        spotId: Long,
    ): List<SpotVisited.Info> = spotVisitedReader.readTodayAllBy(userId, spotId, LocalDate.now())

    fun findLatestByUserAndSpot(
        userId: Long,
        spotId: Long,
    ): SpotVisited.Info? = spotVisitedReader.readLastVisited(userId, spotId)

    fun countBySpotId(spotId: Long): Long = spotVisitedReader.countBySpotId(spotId)
}

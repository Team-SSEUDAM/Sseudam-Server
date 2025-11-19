package com.sseudam.visit

import com.sseudam.contract.visit.SpotVisitedQueryContract
import com.sseudam.visit.component.SpotVisitedAppender
import com.sseudam.visit.component.SpotVisitedReader
import com.sseudam.visit.component.SpotVisitedValidator
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SpotVisitedService(
    private val spotVisitedAppender: SpotVisitedAppender,
    private val spotVisitedReader: SpotVisitedReader,
    private val spotVisitedValidator: SpotVisitedValidator,
) : SpotVisitedQueryContract {
    fun append(spotVisited: SpotVisited.Create) = spotVisitedAppender.append(spotVisited)

    fun findAllByUser(userId: Long): List<SpotVisited.Info> = spotVisitedReader.readByUserId(userId)

    fun findTodaySpotVisitedByUser(userId: Long): List<SpotVisited.Info> = spotVisitedReader.readTodayAllBy(userId, LocalDate.now())

    fun findTodaySpotVisitedByUserAndSpot(
        userId: Long,
        spotId: Long,
    ): List<SpotVisited.Info> = spotVisitedReader.readTodayAllBy(userId, spotId, LocalDate.now())

    fun findLatestByUserAndSpot(
        userId: Long,
        spotId: Long,
    ): SpotVisited.Info? = spotVisitedReader.readLastVisited(userId, spotId)

    override fun countBySpotId(spotId: Long): Long = spotVisitedReader.countBySpotId(spotId)

    fun verifyVisited(
        todayVisited: List<SpotVisited.Info>,
        todayVisitedSpot: SpotVisited.Info?,
    ) {
        spotVisitedValidator.verifyVisited(todayVisited, todayVisitedSpot)
    }
}

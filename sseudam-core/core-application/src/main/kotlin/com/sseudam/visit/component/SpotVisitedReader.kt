package com.sseudam.visit.component

import com.sseudam.visit.SpotVisited
import com.sseudam.visit.repository.SpotVisitedRepository
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class SpotVisitedReader(
    private val spotVisitedRepository: SpotVisitedRepository,
) {
    fun readByUserId(userId: Long): List<SpotVisited.Info> = spotVisitedRepository.findByUserId(userId)

    fun readLastVisited(
        userId: Long,
        spotId: Long,
    ): SpotVisited.Info? = spotVisitedRepository.findLastVisited(userId, spotId)

    fun countBySpotId(spotId: Long): Long = spotVisitedRepository.countBySpotId(spotId)

    fun readTodayAllBy(
        userId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info> = spotVisitedRepository.findTodayAllByUserId(userId, today)

    fun readTodayAllBy(
        userId: Long,
        spotId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info> = spotVisitedRepository.findTodayAllByUserIdAndSpotId(userId, spotId, today)
}

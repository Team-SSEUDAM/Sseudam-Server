package com.sseudam.visit.repository

import com.sseudam.visit.SpotVisited
import java.time.LocalDate

interface SpotVisitedRepository {
    fun create(spotVisited: SpotVisited.Create): SpotVisited.Info

    fun findByUserId(userId: Long): List<SpotVisited.Info>

    fun findLastVisited(
        userId: Long,
        spotId: Long,
    ): SpotVisited.Info?

    fun countBySpotId(spotId: Long): Long

    fun findTodayAllByUserId(
        userId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info>

    fun findTodayAllByUserIdAndSpotId(
        userId: Long,
        spotId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info>
}

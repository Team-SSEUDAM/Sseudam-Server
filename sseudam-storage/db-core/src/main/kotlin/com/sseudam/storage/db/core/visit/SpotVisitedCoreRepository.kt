package com.sseudam.storage.db.core.visit

import com.sseudam.support.tx.Tx
import com.sseudam.visit.SpotVisited
import com.sseudam.visit.repository.SpotVisitedRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class SpotVisitedCoreRepository(
    private val spotVisitedJpaRepository: SpotVisitedJpaRepository,
) : SpotVisitedRepository {
    override fun create(spotVisited: SpotVisited.Create): SpotVisited.Info =
        Tx.writeable {
            spotVisitedJpaRepository
                .save(
                    SpotVisitedEntity(spotVisited),
                ).toSpotVisitedInfo()
        }

    override fun findByUserId(userId: Long): List<SpotVisited.Info> =
        Tx.readable {
            spotVisitedJpaRepository
                .findAllByUserId(userId)
                .map { it.toSpotVisitedInfo() }
        }

    override fun findLastVisited(
        userId: Long,
        spotId: Long,
    ): SpotVisited.Info? =
        Tx.readable {
            spotVisitedJpaRepository
                .findFirstByUserIdAndSpotIdOrderByCreatedAtDesc(userId, spotId)
                ?.toSpotVisitedInfo()
        }

    override fun countBySpotId(spotId: Long): Long =
        Tx.readable {
            spotVisitedJpaRepository
                .countBySpotId(spotId)
        }

    override fun findTodayAllByUserId(
        userId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info> =
        Tx.readable {
            spotVisitedJpaRepository
                .findAllByUserIdAndDate(userId, today)
                .map { it.toSpotVisitedInfo() }
        }

    override fun findTodayAllByUserIdAndSpotId(
        userId: Long,
        spotId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info> =
        Tx.readable {
            spotVisitedJpaRepository
                .findAllByUserIdAndSpotIdAndDate(userId, spotId, today)
                .map { it.toSpotVisitedInfo() }
        }
}

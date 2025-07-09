package com.sseudam.storage.db.core.visit

import com.sseudam.support.tx.TxAdvice
import com.sseudam.visit.SpotVisited
import com.sseudam.visit.SpotVisitedRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class SpotVisitedCoreRepository(
    private val spotVisitedJpaRepository: SpotVisitedJpaRepository,
    private val txAdvice: TxAdvice,
) : SpotVisitedRepository {
    override fun create(spotVisited: SpotVisited.Create): SpotVisited.Info =
        txAdvice.write {
            spotVisitedJpaRepository
                .save(
                    SpotVisitedEntity(spotVisited),
                ).toSpotVisitedInfo()
        }

    override fun findByUserId(userId: Long): List<SpotVisited.Info> =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .findAllByUserId(userId)
                .map { it.toSpotVisitedInfo() }
        }

    override fun findLastVisited(
        userId: Long,
        spotId: Long,
    ): SpotVisited.Info? =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .findFirstByUserIdAndSpotIdOrderByCreatedAtDesc(userId, spotId)
                ?.toSpotVisitedInfo()
        }

    override fun countBySpotId(spotId: Long): Long =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .countBySpotId(spotId)
        }

    override fun findTodayAllByUserId(
        userId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info> =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .findAllByUserIdAndDate(userId, today)
                .map { it.toSpotVisitedInfo() }
        }

    override fun findTodayAllByUserIdAndSpotId(
        userId: Long,
        spotId: Long,
        today: LocalDate,
    ): List<SpotVisited.Info> =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .findAllByUserIdAndSpotIdAndDate(userId, spotId, today)
                .map { it.toSpotVisitedInfo() }
        }
}

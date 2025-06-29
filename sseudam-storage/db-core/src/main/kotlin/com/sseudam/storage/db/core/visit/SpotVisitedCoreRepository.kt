package com.sseudam.storage.db.core.visit

import com.sseudam.support.tx.TxAdvice
import com.sseudam.visit.SpotVisited
import com.sseudam.visit.SpotVisitedRepository
import org.springframework.stereotype.Repository

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

    override fun readByUserId(userId: Long): List<SpotVisited.Info> =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .findAllByUserId(userId)
                .map { it.toSpotVisitedInfo() }
        }

    override fun countBySpotId(spotId: Long): Long =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .countBySpotId(spotId)
        }
}

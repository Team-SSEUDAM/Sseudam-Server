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
    override fun create(spotVisited: SpotVisited.Create): Unit =
        txAdvice.write {
            spotVisitedJpaRepository
                .save(
                    SpotVisitedEntity(spotVisited),
                )
        }

    override fun readByUserId(userId: Long): List<SpotVisited.Info> =
        txAdvice.readOnly {
            spotVisitedJpaRepository
                .findAllByUserId(userId)
                .map { it.toSpotVisitedInfo() }
        }
}

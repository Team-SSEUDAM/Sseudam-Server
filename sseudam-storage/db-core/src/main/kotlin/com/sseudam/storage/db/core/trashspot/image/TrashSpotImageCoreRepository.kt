package com.sseudam.storage.db.core.trashspot.image

import com.sseudam.support.tx.TxAdvice
import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.image.TrashSpotImageRepository
import org.springframework.stereotype.Repository

@Repository
class TrashSpotImageCoreRepository(
    private val trashSpotImageJpaRepository: TrashSpotImageJpaRepository,
    private val txAdvice: TxAdvice,
) : TrashSpotImageRepository {
    override fun findAllByTrashSpotIds(map: List<Long>): List<TrashSpotImage> =
        txAdvice.readOnly {
            trashSpotImageJpaRepository
                .findAllByTrashSpotIdIn(map)
                .map { it.toTrashSpotImage() }
        }

    override fun findBySpotId(spotId: Long): List<TrashSpotImage> =
        txAdvice.readOnly {
            trashSpotImageJpaRepository
                .findAllByTrashSpotId(spotId)
                .map { it.toTrashSpotImage() }
        }
}

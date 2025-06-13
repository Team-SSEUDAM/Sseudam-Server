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
    override fun save(createImage: TrashSpotImage.Create): TrashSpotImage.Info =
        txAdvice.write {
            trashSpotImageJpaRepository
                .save(
                    TrashSpotImageEntity(createImage),
                ).toTrashSpotImage()
        }

    override fun findAllByTrashSpotIds(map: List<Long>): List<TrashSpotImage.Info> =
        txAdvice.readOnly {
            trashSpotImageJpaRepository
                .findAllByTrashSpotIdIn(map)
                .map { it.toTrashSpotImage() }
        }

    override fun findBySpotId(spotId: Long): List<TrashSpotImage.Info> =
        txAdvice.readOnly {
            trashSpotImageJpaRepository
                .findAllByTrashSpotId(spotId)
                .map { it.toTrashSpotImage() }
        }
}

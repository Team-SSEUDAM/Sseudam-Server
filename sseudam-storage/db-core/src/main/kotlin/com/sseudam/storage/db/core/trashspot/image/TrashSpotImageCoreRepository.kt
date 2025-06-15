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

    override fun findAllByTrashSpotIds(spotIds: List<Long>): List<TrashSpotImage.Info> =
        txAdvice.readOnly {
            trashSpotImageJpaRepository
                .findAllByTrashSpotIdIn(spotIds)
                .map { it.toTrashSpotImage() }
        }

    override fun findAllBySpotId(spotId: Long): List<TrashSpotImage.Info> =
        txAdvice.readOnly {
            trashSpotImageJpaRepository
                .findAllByTrashSpotId(spotId)
                .filter { it.deletedAt == null }
                .map { it.toTrashSpotImage() }
        }

    // TODO: 다중 이미지 처리
    override fun updateImage(
        spotId: Long,
        imageUrl: String,
    ) = txAdvice.write {
        val trashSpotImages =
            trashSpotImageJpaRepository
                .findAllByTrashSpotId(spotId)
                .filter { it.deletedAt == null }
        trashSpotImages.forEach { it.softDelete() }

        trashSpotImageJpaRepository
            .save(
                TrashSpotImageEntity(spotId, imageUrl),
            ).toTrashSpotImage()
    }
}

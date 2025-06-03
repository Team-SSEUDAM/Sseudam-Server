package com.sseudam.storage.db.core.trashspot.image

import com.sseudam.trashspot.image.TrashSpotImage
import com.sseudam.trashspot.image.TrashSpotImageRepository
import org.springframework.stereotype.Repository

@Repository
class TrashSpotImageCoreRepository(
    private val trashSpotImageJpaRepository: TrashSpotImageJpaRepository,
) : TrashSpotImageRepository {
    override fun findAllByTrashSpotIds(map: List<Long>): List<TrashSpotImage> =
        trashSpotImageJpaRepository
            .findAllByTrashSpotIdIn(map)
            .map { it.toTrashSpotImage() }
}

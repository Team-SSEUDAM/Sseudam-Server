package com.sseudam.trashspot.repository

import com.sseudam.trashspot.image.TrashSpotImage

interface TrashSpotImageRepository {
    fun save(createImage: TrashSpotImage.Create): TrashSpotImage.Info

    fun findAllByTrashSpotIds(spotIds: List<Long>): List<TrashSpotImage.Info>

    fun findAllBySpotId(spotId: Long): List<TrashSpotImage.Info>

    fun updateImage(
        spotId: Long,
        imageUrl: String,
    ): TrashSpotImage.Info
}

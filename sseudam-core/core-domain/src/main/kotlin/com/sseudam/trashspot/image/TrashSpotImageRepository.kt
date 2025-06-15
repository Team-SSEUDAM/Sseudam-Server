package com.sseudam.trashspot.image

interface TrashSpotImageRepository {
    fun save(createImage: TrashSpotImage.Create): TrashSpotImage.Info

    fun findAllByTrashSpotIds(spotIds: List<Long>): List<TrashSpotImage.Info>

    fun findAllBySpotId(spotId: Long): List<TrashSpotImage.Info>

    fun updateImage(
        spotId: Long,
        imageUrl: String,
    ): TrashSpotImage.Info
}

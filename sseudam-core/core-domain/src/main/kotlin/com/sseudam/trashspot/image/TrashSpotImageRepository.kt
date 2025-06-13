package com.sseudam.trashspot.image

interface TrashSpotImageRepository {
    fun save(createImage: TrashSpotImage.Create): TrashSpotImage.Info

    fun findAllByTrashSpotIds(map: List<Long>): List<TrashSpotImage.Info>

    fun findBySpotId(spotId: Long): List<TrashSpotImage.Info>
}

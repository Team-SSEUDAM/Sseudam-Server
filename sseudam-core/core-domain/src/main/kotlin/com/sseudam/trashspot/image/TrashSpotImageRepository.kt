package com.sseudam.trashspot.image

interface TrashSpotImageRepository {
    fun findAllByTrashSpotIds(map: List<Long>): List<TrashSpotImage>

    fun findBySpotId(spotId: Long): List<TrashSpotImage>
}

package com.sseudam.trashspot.image

import com.sseudam.trashspot.TrashSpot
import org.springframework.stereotype.Service

@Service
class TrashSpotImageService(
    private val trashSpotImageReader: TrashSpotImageReader,
    private val trashSpotImageAppender: TrashSpotImageAppender,
) {
    fun append(createImage: TrashSpotImage.Create): TrashSpotImage.Info = trashSpotImageAppender.append(createImage)

    fun findAll(trashSpots: List<TrashSpot.Info>): List<TrashSpotImage.Info> =
        trashSpotImageReader.readAllByTrashSpotIds(
            trashSpots.map {
                it.id
            },
        )

    fun findBySpotId(spotId: Long): List<TrashSpotImage.Info> = trashSpotImageReader.readBySpotId(spotId)
}

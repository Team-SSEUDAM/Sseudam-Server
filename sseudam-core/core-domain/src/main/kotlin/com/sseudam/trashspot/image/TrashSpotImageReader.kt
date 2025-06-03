package com.sseudam.trashspot.image

import org.springframework.stereotype.Component

@Component
class TrashSpotImageReader(
    private val trashSpotImageRepository: TrashSpotImageRepository,
) {
    fun findAllByTrashSpotIds(map: List<Long>): List<TrashSpotImage> = trashSpotImageRepository.findAllByTrashSpotIds(map)
}

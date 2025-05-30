package com.sseudam.storage.db.core.trashspot.image

import com.sseudam.trashspot.image.TrashSpotImageRepository
import org.springframework.stereotype.Repository

@Repository
class TrashSpotImageCoreRepository(
    private val trashSpotImageJpaRepository: TrashSpotImageJpaRepository,
) : TrashSpotImageRepository

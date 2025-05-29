package com.sseudam.storage.db.core.trashspot

import com.sseudam.trashspot.TrashSpotRepository
import org.springframework.stereotype.Repository

@Repository
class TrashSpotCoreRepository(
    private val trashSpotJpaRepository: TrashSpotJpaRepository,
) : TrashSpotRepository

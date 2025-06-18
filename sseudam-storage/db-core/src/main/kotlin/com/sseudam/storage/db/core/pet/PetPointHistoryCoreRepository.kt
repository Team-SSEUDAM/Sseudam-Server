package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class PetPointHistoryCoreRepository(
    private val petPointHistoryJpaRepository: PetPointHistoryJpaRepository,
) : PetPointHistoryRepository

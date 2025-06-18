package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetRepository
import org.springframework.stereotype.Repository

@Repository
class PetCoreRepository(
    private val petJpaRepository: PetJpaRepository,
) : PetRepository

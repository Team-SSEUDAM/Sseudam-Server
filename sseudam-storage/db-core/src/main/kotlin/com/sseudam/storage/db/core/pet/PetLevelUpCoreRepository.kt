package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetLevelUpRepository
import org.springframework.stereotype.Repository

@Repository
class PetLevelUpCoreRepository(
    private val petLevelUpJpaRepository: PetLevelUpJpaRepository,
) : PetLevelUpRepository

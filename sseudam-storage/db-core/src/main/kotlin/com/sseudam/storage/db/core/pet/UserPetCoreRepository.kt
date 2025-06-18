package com.sseudam.storage.db.core.pet

import com.sseudam.pet.UserPetRepository
import org.springframework.stereotype.Repository

@Repository
class UserPetCoreRepository(
    private val userPetJpaRepository: UserPetJpaRepository,
) : UserPetRepository

package com.sseudam.pet.event

import com.sseudam.pet.PetPointAction

data class UserPetContextEvent(
    val userId: Long,
    val petPointAction: PetPointAction,
)

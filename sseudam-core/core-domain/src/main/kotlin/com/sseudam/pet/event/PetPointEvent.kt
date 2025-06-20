package com.sseudam.pet.event

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.UserPet

data class PetPointEvent(
    val userPet: UserPet.Info,
    val petPointAction: PetPointAction,
)

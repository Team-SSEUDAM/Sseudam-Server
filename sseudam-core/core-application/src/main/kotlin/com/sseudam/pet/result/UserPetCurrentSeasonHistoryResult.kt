package com.sseudam.pet.result

import com.sseudam.pet.UserPet

data class UserPetCurrentSeasonHistoryResult(
    val userPetInfo: UserPet.Info,
    val seasonHistories: List<UserPetLevelUpCurrentSeasonHistoryInfo>,
)

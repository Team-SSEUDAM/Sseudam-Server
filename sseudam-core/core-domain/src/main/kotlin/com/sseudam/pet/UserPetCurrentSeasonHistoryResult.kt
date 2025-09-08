package com.sseudam.pet

data class UserPetCurrentSeasonHistoryResult(
    val userPetInfo: UserPet.Info,
    val seasonHistory: List<UserPetLevelUpCurrentSeasonHistoryInfo>,
)

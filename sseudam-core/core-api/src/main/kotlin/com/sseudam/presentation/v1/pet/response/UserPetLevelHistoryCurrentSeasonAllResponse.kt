package com.sseudam.presentation.v1.pet.response

import com.sseudam.pet.UserPet
import com.sseudam.pet.UserPetLevelUpCurrentSeasonHistoryInfo

data class UserPetLevelHistoryCurrentSeasonAllResponse(
    val userPetInfo: UserPet.Info,
    val list: List<UserPetLevelHistoryCurrentResponse>,
) {
    companion object {
        fun of(
            userPetInfo: UserPet.Info,
            histories: List<UserPetLevelUpCurrentSeasonHistoryInfo>,
        ): UserPetLevelHistoryCurrentSeasonAllResponse =
            UserPetLevelHistoryCurrentSeasonAllResponse(
                userPetInfo = userPetInfo,
                list = histories.map { UserPetLevelHistoryCurrentResponse.of(it) },
            )
    }
}

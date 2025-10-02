package com.sseudam.presentation.v1.pet.response

import com.sseudam.pet.result.UserPetLevelUpCurrentSeasonHistoryInfo

data class UserPetLevelHistoryCurrentSeasonAllResponse(
    val userPetInfo: UserPetInfoResponse,
    val list: List<UserPetLevelHistoryCurrentResponse>,
) {
    companion object {
        fun of(
            userPetInfo: UserPetInfoResponse,
            histories: List<UserPetLevelUpCurrentSeasonHistoryInfo>,
        ): UserPetLevelHistoryCurrentSeasonAllResponse =
            UserPetLevelHistoryCurrentSeasonAllResponse(
                userPetInfo = userPetInfo,
                list = histories.map { UserPetLevelHistoryCurrentResponse.of(it) },
            )
    }
}

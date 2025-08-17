package com.sseudam.presentation.v1.pet.response

import com.sseudam.pet.UserPetLevelUpHistoryInfo

data class UserPetLevelHistoryAllResponse(
    val list: List<UserPetLevelHistoryResponse>,
) {
    companion object {
        fun of(histories: List<UserPetLevelUpHistoryInfo>): UserPetLevelHistoryAllResponse =
            UserPetLevelHistoryAllResponse(
                list = histories.map { UserPetLevelHistoryResponse.of(it) },
            )
    }
}

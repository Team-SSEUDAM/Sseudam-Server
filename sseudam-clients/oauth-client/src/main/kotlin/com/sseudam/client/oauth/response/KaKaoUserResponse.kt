package com.sseudam.client.oauth.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.sseudam.client.oauth.KaKaoClientResult

@JsonIgnoreProperties(ignoreUnknown = true)
data class KaKaoUserResponse(
    val id: String,
    @field:JsonProperty("kakao_account")
    val kaKaoAccount: KaKaoAccount,
) {
    fun toResult(): KaKaoClientResult =
        KaKaoClientResult(
            id = id,
            email = kaKaoAccount.email ?: "",
            name = kaKaoAccount.name ?: "쓰담",
            nickname = kaKaoAccount.kaKaoProfile.nickname ?: "쓰담",
        )
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class KaKaoAccount(
    val email: String?,
    val name: String?,
    @field:JsonProperty("profile")
    val kaKaoProfile: KaKaoProfile,
    @field:JsonProperty("phone_number")
    val phoneNumber: String?,
    @field:JsonProperty("birthyear")
    val birthYear: String?,
    @field:JsonProperty("birthday")
    val birthDay: String?,
    val gender: String?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class KaKaoProfile(
    val nickname: String?,
)

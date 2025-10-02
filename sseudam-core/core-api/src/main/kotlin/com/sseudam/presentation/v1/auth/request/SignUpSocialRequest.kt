package com.sseudam.presentation.v1.auth.request

import com.sseudam.auth.AuthorityType
import com.sseudam.auth.GrantedAuthority
import com.sseudam.auth.command.AuthenticationSocialCommand
import com.sseudam.common.Address
import com.sseudam.user.SocialType
import com.sseudam.user.command.UserCommand
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "소셜 회원가입 요청 Json")
data class SignUpSocialRequest(
    @Schema(description = "소셜 이메일", example = "sseudam@privateasjdjan.com")
    val email: String,
    @Schema(description = "사용자 닉네임", example = "쓰담")
    val name: String,
    @Schema(description = "관심 지역", example = "서울시 영등포구 여의도동 123-45")
    val address: String,
) {
    fun toNewUser(
        socialId: String,
        socialType: SocialType,
    ): UserCommand =
        UserCommand(
            email = email,
            name = name,
            socialId = socialId,
            socialType = socialType,
            address = null,
        )

    fun toNewUser(
        socialId: String,
        socialType: SocialType,
        site: String,
    ): UserCommand =
        UserCommand(
            email = email,
            name = name,
            socialId = socialId,
            socialType = socialType,
            address =
                Address(
                    city = site.split(" ").getOrNull(1) ?: "",
                    site = site,
                ),
        )

    fun toNewAuthenticationSocial(
        socialId: String,
        socialType: SocialType,
    ): AuthenticationSocialCommand =
        AuthenticationSocialCommand(
            loginId = email,
            socialId = socialId,
            socialType = socialType,
            grantedAuthority = GrantedAuthority(AuthorityType.USER),
        )
}

package com.sseudam.presentation.v1.auth.request

import com.sseudam.auth.AuthorityType
import com.sseudam.auth.GrantedAuthority
import com.sseudam.auth.NewAuthenticationSocial
import com.sseudam.user.NewUser
import com.sseudam.user.SocialType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "소셜 회원가입 요청 Json")
data class SignUpSocialRequest(
    val email: String,
    val name: String,
) {
    fun toNewUser(
        socialId: String,
        socialType: SocialType,
    ): NewUser =
        NewUser(
            email = email,
            name = name,
            socialId = socialId,
            socialType = socialType,
        )

    fun toNewAuthenticationSocial(
        socialId: String,
        socialType: SocialType,
    ): NewAuthenticationSocial =
        NewAuthenticationSocial(
            loginId = email,
            socialId = socialId,
            socialType = socialType,
            grantedAuthority = GrantedAuthority(AuthorityType.USER),
        )
}

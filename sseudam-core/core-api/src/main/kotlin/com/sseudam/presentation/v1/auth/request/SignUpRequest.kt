package com.sseudam.presentation.v1.auth.request

import com.sseudam.auth.AuthorityType
import com.sseudam.auth.GrantedAuthority
import com.sseudam.auth.NewAuthenticationSseudam
import com.sseudam.user.NewUser
import com.sseudam.user.SocialType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원가입 요청 Json")
data class SignUpRequest(
    @Schema(description = "이메일", example = "test@test.com")
    val email: String,
    @Schema(description = "비밀번호", example = "password")
    val password: String,
    @Schema(description = "이름", example = "윤범차")
    val name: String?,
    @Schema(description = "이름", example = "닉네임이야")
    val nickname: String?,
) {
    fun toNewUser(password: String): NewUser =
        NewUser(
            email = email,
            name = name,
            nickname = nickname,
            password = password,
            socialId = "",
            socialType = SocialType.SSEUDAM,
            address = null,
        )

    fun toNewAuthenticationSseudam(): NewAuthenticationSseudam =
        NewAuthenticationSseudam(
            loginId = email,
            password = password,
            grantedAuthority = GrantedAuthority(AuthorityType.USER),
        )
}

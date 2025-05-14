package com.sseudam.user

/**
 * NewUser
 *
 * @property email 이메일
 * @property name 이름
 * @property nickname 닉네임
 * @property password 비밀번호
 * @property socialId 소셜 아이디
 * @property socialType 소셜 타입
 */
data class NewUser(
    val email: String,
    val name: String?,
    val nickname: String? = null,
    val password: String? = null,
    val socialId: String,
    val socialType: SocialType,
)

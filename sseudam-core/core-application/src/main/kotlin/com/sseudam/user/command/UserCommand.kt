package com.sseudam.user.command

import com.sseudam.common.Address
import com.sseudam.user.SocialType

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
data class UserCommand(
    val name: String,
    val nickname: String? = null,
    val address: Address?,
    val email: String,
    val password: String? = null,
    val socialId: String,
    val socialType: SocialType,
)

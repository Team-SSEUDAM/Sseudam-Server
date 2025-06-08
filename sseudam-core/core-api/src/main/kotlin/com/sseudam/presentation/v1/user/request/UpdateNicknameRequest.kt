package com.sseudam.presentation.v1.user.request

import com.sseudam.user.UpdateNickname
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "닉네임 수정 요청")
data class UpdateNicknameRequest(
    @Schema(description = "닉네임", example = "피다짱")
    val nickname: String,
) {
    fun toUpdateNickname(): UpdateNickname =
        UpdateNickname(
            nickname = nickname,
        )
}

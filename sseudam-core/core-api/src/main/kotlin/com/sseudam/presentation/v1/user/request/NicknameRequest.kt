package com.sseudam.presentation.v1.user.request

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.user.UpdateNickname
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "닉네임 요청 Json")
data class NicknameRequest(
    @Schema(description = "닉네임", example = "피다짱")
    val nickname: String,
) {
    fun toUpdateNickname(): UpdateNickname =
        UpdateNickname(
            nickname = nickname,
        )

    fun toValidateNickname(): String {
        // 닉네임이 비어있거나 null인 경우 예외 처리
        if (nickname.isBlank()) {
            throw ErrorException(ErrorType.NICKNAME_IS_BLANK)
        }

        // 영어 및 숫자, 한글로만 구성된 닉네임인지 확인
        require(nickname.matches(Regex("^[a-zA-Z0-9가-힣]+$"))) {
            throw ErrorException(ErrorType.INVALID_NICKNAME_FORMAT)
        }
        return nickname
    }
}

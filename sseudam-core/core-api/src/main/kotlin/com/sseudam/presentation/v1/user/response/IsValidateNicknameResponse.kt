package com.sseudam.presentation.v1.user.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "닉네임 유효성 검사 응답 Json")
data class IsValidateNicknameResponse(
    @Schema(
        description = "닉네임 유효성 검사 결과",
        example = "true",
    )
    val isValid: Boolean,
    @Schema(
        description = "닉네임 중복 여부 메세지",
        example = "사용 가능한 닉네임입니다.",
    )
    val message: String? = null,
) {
    companion object {
        fun of(
            isValid: Boolean,
            message: String? = null,
        ): IsValidateNicknameResponse =
            IsValidateNicknameResponse(
                isValid = isValid,
                message = message,
            )
    }
}

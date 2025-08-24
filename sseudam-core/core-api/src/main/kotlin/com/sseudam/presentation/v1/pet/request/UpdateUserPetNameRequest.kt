package com.sseudam.presentation.v1.pet.request

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 펫 이름 변경 요청 Json")
data class UpdateUserPetNameRequest(
    @Schema(description = "변경할 사용자 펫 이름")
    val nickname: String,
) {
    init {
        require(nickname.isNotBlank()) { throw ErrorException(ErrorType.NOT_BLANK_NICKNAME) }
        require(nickname.length in 2..12) { throw ErrorException(ErrorType.INVALID_PET_NICKNAME) }
    }
}

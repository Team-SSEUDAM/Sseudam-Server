package com.sseudam.presentation.v1.pet.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "사용자 펫 이름 변경 요청 Json")
data class UpdateUserPetNameRequest(
    @Schema(description = "변경할 사용자 펫 이름")
    val nickname: String,
)

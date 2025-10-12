package com.sseudam.presentation.v1.suggestion.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "제보 메시지 응답")
data class SuggestionMessageResponse(
    @Schema(description = "제보 관련 응답 메시지", example = "제보가 취소되었습니다.")
    val message: String,
)

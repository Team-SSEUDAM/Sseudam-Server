package com.sseudam.presentation.v1.suggestion.request

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "제보 취소 요청")
data class SuggestionCancelRequest(
    @Schema(description = "제보 ID", example = "1")
    val suggestionId: Long,
)

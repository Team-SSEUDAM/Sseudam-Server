package com.sseudam.presentation.v1.visit.response

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "방문하기 응답 Json")
data class SpotVisitedCreateResponse(
    @Schema(description = "방문하기 응답", example = "방문 완료")
    val message: String,
)

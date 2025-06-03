package com.sseudam.presentation.v1.trashspot.response

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "쓰레기통 장소 목록 응답")
data class TrashSpotAllResponse(
    @field:ArraySchema(
        schema = Schema(implementation = TrashSpotResponse::class),
        arraySchema = Schema(description = "쓰레기통 장소 목록"),
    )
    val list: List<TrashSpotResponse>,
) {
    companion object {
        fun of(list: List<TrashSpotResponse>) = TrashSpotAllResponse(list)
    }
}

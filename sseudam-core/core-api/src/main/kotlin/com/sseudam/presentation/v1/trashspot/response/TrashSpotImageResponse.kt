package com.sseudam.presentation.v1.trashspot.response

import com.sseudam.trashspot.image.TrashSpotImage
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "쓰레기통 이미지 응답")
data class TrashSpotImageResponse(
    @Schema(description = "이미지 ID")
    val imageId: Long,
    @Schema(description = "쓰레기통 ID")
    val trashSpotId: Long,
    @Schema(description = "이미지 URL")
    val imageUrl: String,
) {
    companion object {
        fun of(trashSpotImage: TrashSpotImage) =
            TrashSpotImageResponse(
                imageId = trashSpotImage.id,
                trashSpotId = trashSpotImage.trashSpotId,
                imageUrl = trashSpotImage.imageUrl,
            )
    }
}

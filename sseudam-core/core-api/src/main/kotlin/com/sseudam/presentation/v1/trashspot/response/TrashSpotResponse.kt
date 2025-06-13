package com.sseudam.presentation.v1.trashspot.response

import com.sseudam.common.Address
import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashSpot
import com.sseudam.trashspot.TrashType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "쓰레기통 장소")
data class TrashSpotResponse(
    @Schema(description = "쓰레기통 장소 ID")
    val id: Long,
    @Schema(description = "쓰레기통 장소 명")
    val name: String,
    @Schema(description = "쓰레기통 장소 지역")
    val region: Region,
    @Schema(description = "쓰레기통 주소")
    val address: Address,
    @Schema(
        description = "좌표 정보 (GeoJson)",
        example = """
        {
          "type": "Point",
          "coordinates": [127.10317, 37.48881]
        }
    """,
    )
    val point: GeoJson,
    @Schema(description = "쓰레기통 타입")
    val trashType: TrashType,
    @Schema(description = "쓰레기통 수정 시간")
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun of(trashSpot: TrashSpot.Info) =
            TrashSpotResponse(
                id = trashSpot.id,
                name = trashSpot.name,
                region = trashSpot.region,
                address = trashSpot.address,
                point = trashSpot.point,
                trashType = trashSpot.trashType,
                updatedAt = trashSpot.updatedAt,
            )
    }
}

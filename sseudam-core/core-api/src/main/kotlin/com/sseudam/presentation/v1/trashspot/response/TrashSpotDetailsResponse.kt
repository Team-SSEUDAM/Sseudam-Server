package com.sseudam.presentation.v1.trashspot.response

import com.sseudam.support.geo.GeoJson
import com.sseudam.support.geo.Region
import com.sseudam.trashspot.TrashSpotDetail
import com.sseudam.trashspot.TrashType
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "쓰레기통 장소 상세 조회 응답 Json")
data class TrashSpotDetailsResponse(
    @Schema(description = "쓰레기통 장소 ID")
    val id: Long,
    @Schema(description = "쓰레기통 장소 제보자")
    val suggestionerId: Long?,
    @Schema(description = "쓰레기통 장소 제보자 이름")
    val suggestionerName: String?,
    @Schema(description = "쓰레기통 장소 명")
    val name: String,
    @Schema(description = "쓰레기통 장소 지역")
    val region: Region,
    @Schema(description = "도시")
    val city: String,
    @Schema(description = "주소")
    val site: String,
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
    @Schema(description = "쓰레기통 이미지 URL")
    val imageUrl: String?,
    @Schema(description = "쓰레기통 수정 시간")
    val updatedAt: LocalDateTime?,
) {
    companion object {
        fun of(trashSpotDetail: TrashSpotDetail) =
            TrashSpotDetailsResponse(
                id = trashSpotDetail.trashSpot.id,
                suggestionerId = trashSpotDetail.user?.id,
                suggestionerName = trashSpotDetail.user?.nickname,
                name = trashSpotDetail.trashSpot.name,
                region = trashSpotDetail.trashSpot.region,
                city = trashSpotDetail.trashSpot.address.city,
                site = trashSpotDetail.trashSpot.address.site,
                point = trashSpotDetail.trashSpot.point,
                trashType = trashSpotDetail.trashSpot.trashType,
                imageUrl = trashSpotDetail.image?.imageUrl,
                updatedAt = trashSpotDetail.trashSpot.updatedAt,
            )
    }
}

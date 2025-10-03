package com.sseudam.presentation.v1.trashspot

import com.sseudam.common.Region
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.trashspot.response.TrashSpotAllResponse
import com.sseudam.presentation.v1.trashspot.response.TrashSpotDetailsResponse
import com.sseudam.presentation.v1.trashspot.response.TrashSpotResponse
import com.sseudam.trashspot.TrashSpotFacade
import com.sseudam.trashspot.TrashType
import com.sseudam.trashspot.dto.TrashSpotLocation
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "🗑️ Trash Spot API", description = "쓰레기통 장소 관련 API입니다.")
@ApiV1Controller
class TrashSpotController(
    private val trashSpotFacade: TrashSpotFacade,
) {
    @Operation(summary = "쓰레기통 장소 조회", description = "쓰레기통 장소를 조회합니다.")
    @GetMapping("/trash-spots")
    fun trashSpotFindAll(
        @RequestParam @Parameter(name = "region", description = "지역") region: Region?,
        @RequestParam @Parameter(name = "type", description = "쓰레기통 유형") type: TrashType?,
        @RequestParam @Parameter(name = "swLat", description = "남서쪽 위도") swLat: Double?,
        @RequestParam @Parameter(name = "swLng", description = "남서쪽 경도") swLng: Double?,
        @RequestParam @Parameter(name = "neLat", description = "북동쪽 위도") neLat: Double?,
        @RequestParam @Parameter(name = "neLng", description = "북동쪽 경도") neLng: Double?,
    ): TrashSpotAllResponse {
        val trashSpots =
            trashSpotFacade.findAll(
                region = region,
                trashType = type,
                location =
                    TrashSpotLocation(
                        swLat,
                        swLng,
                        neLat,
                        neLng,
                    ),
            )
        return TrashSpotAllResponse.of(
            trashSpots.map {
                TrashSpotResponse.of(it)
            },
        )
    }

    @Operation(summary = "쓰레기통 장소 상세 조회", description = "쓰레기통 장소를 상세 조회합니다.")
    @GetMapping("/trash-spots/{spotId}")
    fun trashSpotFindDetails(
        @PathVariable spotId: Long,
    ): TrashSpotDetailsResponse {
        val details = trashSpotFacade.findDetails(spotId)
        return TrashSpotDetailsResponse.of(details)
    }
}

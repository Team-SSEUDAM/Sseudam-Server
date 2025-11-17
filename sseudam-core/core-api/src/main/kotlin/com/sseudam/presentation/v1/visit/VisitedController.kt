package com.sseudam.presentation.v1.visit

import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.visit.response.SpotLatestVisitedResponse
import com.sseudam.presentation.v1.visit.response.SpotVisitedAllResponse
import com.sseudam.presentation.v1.visit.response.SpotVisitedCountResponse
import com.sseudam.presentation.v1.visit.response.SpotVisitedDetailResponse
import com.sseudam.user.User
import com.sseudam.visit.SpotVisitedFacade
import com.sseudam.visit.SpotVisitedService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Tag(name = "🙋 Visited API", description = "방문 관련 API입니다.")
@ApiV1Controller
class VisitedController(
    private val spotVisitedService: SpotVisitedService,
    private val spotVisitedFacade: SpotVisitedFacade,
) {
    @Operation(summary = "방문하기", description = "장소에 방문합니다.")
    @PostMapping("/visited/{spotId}")
    fun visitedSpotCreate(
        user: User,
        @PathVariable spotId: Long,
    ): SpotVisitedDetailResponse {
        val result = spotVisitedFacade.visitSpot(user.id, spotId)
        return SpotVisitedDetailResponse.of(
            isToday = result.isToday,
            visited = result.visited,
        )
    }

    @Operation(summary = "방문 내역 조회", description = "방문한 기록들을 조회합니다.")
    @GetMapping("/visited")
    fun visitedSpotFindAll(user: User): SpotVisitedAllResponse {
        val visitedSpots = spotVisitedFacade.findSpotVisitedByUserId(user.id)
        return SpotVisitedAllResponse.from(visitedSpots)
    }

    @Operation(summary = "장소 최근 방문 여부 조회", description = "해당 장소에 최근 방문했는지 여부를 조회합니다.")
    @GetMapping("/visited/{spotId}")
    fun visitedSpotFindBySpot(
        user: User,
        @PathVariable spotId: Long,
    ): SpotLatestVisitedResponse {
        val visited = spotVisitedService.findLatestByUserAndSpot(user.id, spotId)
        return SpotLatestVisitedResponse.of(
            spotId = spotId,
            userId = user.id,
            visited = visited,
        )
    }

    @Operation(summary = "장소 방문자 수 조회", description = "장소의 방문자 수를 조회합니다.")
    @GetMapping("/visited/count/{spotId}")
    fun visitedSpotCountBySpot(
        @PathVariable spotId: Long,
    ): SpotVisitedCountResponse {
        val count = spotVisitedService.countBySpotId(spotId)

        return SpotVisitedCountResponse.of(
            visitedCount = count,
            spotId = spotId,
        )
    }
}

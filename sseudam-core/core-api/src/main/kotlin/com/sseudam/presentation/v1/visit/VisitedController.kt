package com.sseudam.presentation.v1.visit

import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.visit.response.SpotVisitedAllResponse
import com.sseudam.presentation.v1.visit.response.SpotVisitedCreateResponse
import com.sseudam.user.User
import com.sseudam.visit.SpotVisited
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
    ): SpotVisitedCreateResponse {
        spotVisitedService.append(SpotVisited.Create(user.id, spotId))
        return SpotVisitedCreateResponse("방문 인증이 완료되었어요")
    }

    @Operation(summary = "방문 내역 조회", description = "방문한 기록들을 조회합니다.")
    @GetMapping("/visited")
    fun visitedSpotFindAll(user: User): SpotVisitedAllResponse {
        val visitedSpots = spotVisitedFacade.findSpotVisitedByUserId(user.id)
        return SpotVisitedAllResponse.of(visitedSpots)
    }
}

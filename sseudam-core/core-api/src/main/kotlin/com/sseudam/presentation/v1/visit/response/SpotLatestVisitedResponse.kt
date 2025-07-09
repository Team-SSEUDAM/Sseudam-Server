package com.sseudam.presentation.v1.visit.response

import com.sseudam.visit.SpotVisited
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "최근 방문한 장소에 대한 응답 Json")
data class SpotLatestVisitedResponse(
    @Schema(description = "방문한 장소의 ID", example = "1")
    val spotId: Long,
    @Schema(description = "방문을 시도한 유저의 ID", example = "1")
    val userId: Long,
    @Schema(description = "최근 방문했던 시간", example = "2025-07-01T12:00:00")
    val lastVisitedAt: LocalDateTime?,
) {
    companion object {
        fun of(
            spotId: Long,
            userId: Long,
            visited: SpotVisited.Info?,
        ): SpotLatestVisitedResponse =
            SpotLatestVisitedResponse(
                spotId = spotId,
                userId = userId,
                lastVisitedAt = visited?.visitedAt,
            )
    }
}

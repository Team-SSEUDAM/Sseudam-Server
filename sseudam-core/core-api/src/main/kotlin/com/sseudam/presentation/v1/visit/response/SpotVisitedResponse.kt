package com.sseudam.presentation.v1.visit.response

import com.sseudam.visit.SpotVisited
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@Schema(description = "방문하기 응답 Json")
data class SpotVisitedResponse(
    @Schema(description = "방문 ID", example = "1")
    val id: Long,
    @Schema(description = "방문한 쓰레기통 장소 ID", example = "1")
    val spotId: Long,
    @Schema(description = "방문한 사용자 ID", example = "1")
    val userId: Long,
    @Schema(description = "방문한 쓰레기통 주소", example = "서울특별시 강남구 테헤란로 123")
    val site: String,
    @Schema(description = "방문 시간", example = "2023-10-01T12:00:00")
    val visitedAt: LocalDateTime,
) {
    companion object {
        fun of(visited: SpotVisited.Info): SpotVisitedResponse =
            SpotVisitedResponse(
                id = visited.id,
                spotId = visited.spotId,
                userId = visited.userId,
                site = visited.site,
                visitedAt = visited.visitedAt,
            )
    }
}

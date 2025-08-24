package com.sseudam.presentation.v1.visit.response

data class SpotVisitedCountResponse(
    val spotId: Long,
    val visitedCount: Long,
) {
    companion object {
        fun of(
            spotId: Long,
            visitedCount: Long,
        ): SpotVisitedCountResponse =
            SpotVisitedCountResponse(
                spotId = spotId,
                visitedCount = visitedCount,
            )
    }
}

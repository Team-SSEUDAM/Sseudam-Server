package com.sseudam.presentation.v1.visit.response

import com.sseudam.visit.SpotVisited

data class SpotVisitedAllResponse(
    val list: List<SpotVisitedResponse>,
) {
    companion object {
        fun from(infos: List<SpotVisited.Info>): SpotVisitedAllResponse =
            SpotVisitedAllResponse(
                list = infos.map { SpotVisitedResponse.of(it) },
            )
    }
}

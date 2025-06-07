package com.sseudam.presentation.v1.visit.response

import com.sseudam.visit.SpotVisitedAll

data class SpotVisitedAllResponse(
    val list: SpotVisitedAll,
) {
    companion object {
        fun of(list: SpotVisitedAll): SpotVisitedAllResponse = SpotVisitedAllResponse(list)
    }
}

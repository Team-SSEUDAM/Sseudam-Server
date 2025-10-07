package com.sseudam.visit.result

import com.sseudam.visit.SpotVisited

data class SpotVisitedResult(
    val isToday: Boolean,
    val visited: SpotVisited.Info,
)

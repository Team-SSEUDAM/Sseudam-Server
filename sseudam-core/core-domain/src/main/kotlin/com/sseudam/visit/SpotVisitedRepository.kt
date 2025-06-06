package com.sseudam.visit

interface SpotVisitedRepository {
    fun create(spotVisited: SpotVisited.Create): SpotVisited.Info
}

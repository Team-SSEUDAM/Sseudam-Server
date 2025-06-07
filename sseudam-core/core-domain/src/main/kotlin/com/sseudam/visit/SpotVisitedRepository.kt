package com.sseudam.visit

interface SpotVisitedRepository {
    fun create(spotVisited: SpotVisited.Create): SpotVisited.Info

    fun readByUserId(userId: Long): List<SpotVisited.Info>
}

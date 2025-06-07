package com.sseudam.visit

interface SpotVisitedRepository {
    fun create(spotVisited: SpotVisited.Create)

    fun readByUserId(userId: Long): List<SpotVisited.Info>
}

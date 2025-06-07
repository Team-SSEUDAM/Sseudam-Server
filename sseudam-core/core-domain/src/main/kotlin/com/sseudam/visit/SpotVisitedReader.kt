package com.sseudam.visit

import org.springframework.stereotype.Component

@Component
class SpotVisitedReader(
    private val spotVisitedRepository: SpotVisitedRepository,
) {
    fun readByUserId(userId: Long): List<SpotVisited.Info> = spotVisitedRepository.readByUserId(userId)
}

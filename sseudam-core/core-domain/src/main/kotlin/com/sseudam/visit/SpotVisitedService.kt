package com.sseudam.visit

import com.sseudam.pet.PetPointAction
import com.sseudam.pet.event.PetEventPublisher
import org.springframework.stereotype.Service

@Service
class SpotVisitedService(
    private val spotVisitedAppender: SpotVisitedAppender,
    private val spotVisitedReader: SpotVisitedReader,
    private val petEventPublisher: PetEventPublisher,
) {
    fun append(spotVisited: SpotVisited.Create) {
        val visited = spotVisitedAppender.append(spotVisited)
        petEventPublisher.publish(visited.userId, PetPointAction.SPOT_VISITED)
    }

    fun findAllByUser(userId: Long): List<SpotVisited.Info> = spotVisitedReader.readByUserId(userId)

    fun countBySpotId(spotId: Long): Long = spotVisitedReader.countBySpotId(spotId)
}

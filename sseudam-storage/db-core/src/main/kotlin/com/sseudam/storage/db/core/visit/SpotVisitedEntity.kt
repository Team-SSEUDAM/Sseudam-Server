package com.sseudam.storage.db.core.visit

import com.sseudam.storage.db.core.support.BaseEntity
import com.sseudam.visit.SpotVisited
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDate

@Entity
@Table(name = "t_spot_visited")
class SpotVisitedEntity(
    val userId: Long,
    val spotId: Long,
    val date: LocalDate,
) : BaseEntity() {
    constructor(spotVisitedCreate: SpotVisited.Create) : this(
        userId = spotVisitedCreate.userId,
        spotId = spotVisitedCreate.spotId,
        date = spotVisitedCreate.date,
    )

    fun toSpotVisitedInfo(): SpotVisited.Info =
        SpotVisited.Info(
            id = id!!,
            userId = userId,
            spotId = spotId,
            date = date,
            site = "",
            visitedAt = createdAt,
        )
}

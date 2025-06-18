package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointHistory
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "t_pet_point_history")
class PetPointHistoryEntity(
    val userId: Long,
    val petId: Long,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    val event: PetPointHistory.PointEvent,
    val point: Long,
) : BaseEntity() {
    constructor(create: PetPointHistory.Create) : this(
        userId = create.userId,
        petId = create.petId,
        event = create.event,
        point = create.point,
    )

    fun toPetPointHistoryInfo() =
        PetPointHistory.Info(
            id = id!!,
            userId = userId,
            petId = petId,
            event = event,
            point = point,
            createdAt = createdAt,
        )
}

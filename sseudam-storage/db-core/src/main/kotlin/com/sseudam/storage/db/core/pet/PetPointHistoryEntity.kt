package com.sseudam.storage.db.core.pet

import com.sseudam.pet.PetPointAction
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
    val userPetId: Long,
    val previousPoint: Long,
    val additionalPoint: Long,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(50)")
    val action: PetPointAction,
) : BaseEntity() {
    constructor(create: PetPointHistory.Create) : this(
        userPetId = create.userPetId,
        previousPoint = create.previousPoint,
        additionalPoint = create.additionalPoint,
        action = create.action,
    )

    fun toPetPointHistoryInfo() =
        PetPointHistory.Info(
            id = id!!,
            userPetId = userPetId,
            action = action,
            previousPoint = previousPoint,
            additionalPoint = additionalPoint,
            createdAt = createdAt,
        )
}

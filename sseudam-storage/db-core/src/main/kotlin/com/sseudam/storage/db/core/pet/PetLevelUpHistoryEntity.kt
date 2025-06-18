package com.sseudam.storage.db.core.pet

import com.sseudam.pet.Pet
import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Entity
@Table(name = "t_pet_level_up_history")
class PetLevelUpHistoryEntity(
    val petId: Long,
    val userId: Long,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val levelType: Pet.LevelType,
) : BaseEntity() {
    constructor(create: PetLevelUpHistory.Create) : this(
        petId = create.petId,
        userId = create.userId,
        levelType = create.levelType,
    )

    fun toPetLevelUpHistoryInfo() =
        PetLevelUpHistory.Info(
            id = id!!,
            petId = petId,
            userId = userId,
            levelType = levelType,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}

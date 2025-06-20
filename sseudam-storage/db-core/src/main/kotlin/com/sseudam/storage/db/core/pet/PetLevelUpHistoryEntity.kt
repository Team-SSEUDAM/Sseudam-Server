package com.sseudam.storage.db.core.pet

import com.sseudam.pet.Pet
import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.Month

@Entity
@Table(name = "t_pet_level_up_history")
class PetLevelUpHistoryEntity(
    val userPetId: Long,
    val pointHistoryId: Long,
    val nickname: String,
    val year: Int,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10)")
    val monthly: Month,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val levelType: Pet.LevelType,
) : BaseEntity() {
    constructor(create: PetLevelUpHistory.Create) : this(
        userPetId = create.userPetId,
        pointHistoryId = create.pointHistoryId,
        nickname = create.nickname,
        year = create.year,
        monthly = create.monthly,
        levelType = create.levelType,
    )

    fun toPetLevelUpHistoryInfo() =
        PetLevelUpHistory.Info(
            id = id!!,
            userPetId = userPetId,
            pointHistoryId = pointHistoryId,
            nickname = nickname,
            year = year,
            monthly = monthly,
            levelType = levelType,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}

package com.sseudam.storage.db.core.pet

import com.sseudam.pet.Pet
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.Month

@Entity
@Table(name = "t_pet")
class PetEntity(
    val name: String,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val levelType: Pet.LevelType,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10)")
    val monthly: Month,
) : BaseEntity() {
    constructor(petCreate: Pet.Create) : this(
        name = petCreate.name,
        levelType = petCreate.levelType,
        monthly = petCreate.monthly,
    )

    fun toPetInfo() =
        Pet.Info(
            id = id!!,
            name = name,
            levelType = levelType,
            monthly = monthly,
            createdAt = createdAt,
        )
}

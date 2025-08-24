package com.sseudam.storage.db.core.pet

import com.sseudam.pet.Pet
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.Month
import java.time.Year

@Entity
@Table(name = "t_pet")
class PetEntity(
    val name: String,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(15)")
    val levelType: Pet.LevelType,
    val year: Int,
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(10)")
    val monthly: Month,
) : BaseEntity() {
    constructor(petCreate: Pet.Create) : this(
        name = petCreate.name,
        levelType = petCreate.levelType,
        year = Year.now().value,
        monthly = petCreate.monthly,
    )

    fun toPetInfo() =
        Pet.Info(
            id = id!!,
            name = name,
            levelType = levelType,
            year = year,
            monthly = monthly,
            createdAt = createdAt,
        )
}

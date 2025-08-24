package com.sseudam.storage.db.core.pet

import com.sseudam.pet.UserPet
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "t_user_pet")
class UserPetEntity(
    val userId: Long,
    var petId: Long,
    var nickname: String,
    var point: Long,
) : BaseEntity() {
    constructor(userPetCreate: UserPet.Create) : this(
        userId = userPetCreate.userId,
        petId = userPetCreate.petId,
        nickname = userPetCreate.nickname,
        point = userPetCreate.point,
    )

    fun toUserPetInfo() =
        UserPet.Info(
            id = id!!,
            userId = userId,
            petId = petId,
            nickname = nickname,
            point = point,
            createdAt = createdAt,
        )

    fun updatePoint(point: Long): UserPetEntity {
        this.point = point
        return this
    }

    fun updateNickname(nickname: String): UserPetEntity {
        this.nickname = nickname
        return this
    }

    fun updatePetId(petId: Long): UserPetEntity {
        this.petId = petId
        return this
    }
}

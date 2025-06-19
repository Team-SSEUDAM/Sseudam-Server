package com.sseudam.storage.db.core.pet

import com.sseudam.pet.UserPet
import com.sseudam.storage.db.core.support.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "t_user_pet")
class UserPetEntity(
    val userId: Long,
    val petId: Long,
    var nickname: String,
    val point: Long,
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

    fun updateNickname(nickname: String): UserPetEntity {
        this.nickname = nickname
        return this
    }
}

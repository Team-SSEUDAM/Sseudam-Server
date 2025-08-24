package com.sseudam.pet

import org.springframework.stereotype.Service
import java.time.Month

@Service
class PetLevelUpHistoryService(
    private val petLevelUpHistoryAppender: PetLevelUpHistoryAppender,
    private val petLevelUpHistoryReader: PetLevelUpHistoryReader,
) {
    fun append(
        userPet: UserPet.Info,
        petInfo: Pet.Info,
    ) {
        petLevelUpHistoryAppender.append(
            PetLevelUpHistory.Create(
                userId = userPet.userId,
                userPetId = userPet.id,
                nickname = userPet.nickname,
                year = petInfo.year,
                monthly = petInfo.monthly,
                levelType = petInfo.levelType,
            ),
        )
    }

    fun findAllBy(
        currentYear: Int,
        currentMonth: Month,
        userPetId: Long,
    ): List<PetLevelUpHistory.Info> =
        petLevelUpHistoryReader.readBy(
            currentYear = currentYear,
            currentMonth = currentMonth,
            userPetId = userPetId,
        )

    fun findAllBy(userPetId: Long): List<PetLevelUpHistory.Info> =
        petLevelUpHistoryReader.readBy(
            userPetId = userPetId,
        )

    fun findAllByUser(userId: Long): List<PetLevelUpHistory.Info> = petLevelUpHistoryReader.readByUser(userId)
}

package com.sseudam.pet

import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserPetFacade(
    private val userPetService: UserPetService,
    private val petService: PetService,
    private val petLevelUpHistoryService: PetLevelUpHistoryService,
    private val petPointHistoryService: PetPointHistoryService,
) {
    fun findPetInfo(userId: Long): UserPet.Info {
        val currentYear = LocalDateTime.now().year
        val currentMonth = LocalDateTime.now().month

        val pets = petService.findAllLatestSeasonPets(currentYear, currentMonth)

        val userPetInfo =
            userPetService.findByUser(userId)
                ?: userPetService.append(userId, pets.first())

        return userPetInfo
    }

    fun findCurrentSeasonPetHistory(userId: Long): List<UserPetLevelUpHistoryInfo> {
        val currentYear = LocalDateTime.now().year
        val currentMonth = LocalDateTime.now().month
        val userPetInfo = userPetService.findByUser(userId)

        val currentUserPetHistories =
            petLevelUpHistoryService.findAllBy(currentYear, currentMonth, userPetInfo!!.id)

        val pointHistories = petPointHistoryService.findAllByUserPet(userPetInfo.id)

        val pointHistoriesMap = pointHistories.associateBy { it.id }
        return currentUserPetHistories.map { history ->
            UserPetLevelUpHistoryInfo(
                userId = userId,
                nickname = history.nickname,
                levelType = history.levelType,
                point = (
                    (pointHistoriesMap[history.pointHistoryId]?.previousPoint ?: 0L) +
                        (pointHistoriesMap[history.pointHistoryId]?.additionalPoint ?: 0L)
                ),
                createdAt = history.createdAt,
            )
        }
    }
}

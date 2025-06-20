package com.sseudam.pet

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserPetFacade(
    private val userPetService: UserPetService,
    private val petService: PetService,
    private val petLevelUpHistoryService: PetLevelUpHistoryService,
    private val userPetPolicy: UserPetPolicy,
) {
    fun findPetInfo(userId: Long): UserPet.Info {
        val (currentYear, currentMonth) = LocalDateTime.now().let { it.year to it.month }
        val pets = petService.findAllLatestSeasonPets(currentYear, currentMonth)
        return userPetService.findByUser(userId) ?: userPetService.append(userId, pets.first())
    }

    fun findCurrentSeasonPetHistory(userId: Long): List<UserPetLevelUpHistoryInfo> {
        val (currentYear, currentMonth) = LocalDateTime.now().let { it.year to it.month }
        val userPetInfo = userPetService.findByUser(userId) ?: return emptyList()
        return petLevelUpHistoryService
            .findAllBy(currentYear, currentMonth, userPetInfo.id)
            .map { history ->
                UserPetLevelUpHistoryInfo(
                    userId = userId,
                    nickname = history.nickname,
                    levelType = history.levelType,
                    point = userPetPolicy.getMinLevelStandard(history.levelType),
                    createdAt = history.createdAt,
                )
            }
    }

    fun findAllPetHistory(userId: Long): List<UserPetLevelUpHistoryInfo> {
        val (currentYear, currentMonth) = LocalDateTime.now().let { it.year to it.month }
        val userPetHistories =
            petLevelUpHistoryService
                .findAllByUser(userId)
                .filterNot { it.createdAt.year == currentYear && it.createdAt.month == currentMonth }

        if (userPetHistories.isEmpty()) return emptyList()

        return userPetHistories
            .groupBy { it.userPetId }
            .mapValues { (_, histories) -> histories.maxByOrNull { it.levelType.level } ?: throw ErrorException(ErrorType.NOT_FOUND_DATA) }
            .values
            .map { history ->
                UserPetLevelUpHistoryInfo(
                    userId = userId,
                    nickname = history.nickname,
                    levelType = history.levelType,
                    point = userPetPolicy.getMinLevelStandard(history.levelType),
                    createdAt = history.createdAt,
                )
            }
    }
}

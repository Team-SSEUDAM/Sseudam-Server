package com.sseudam.pet

import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class UserPetFacade(
    private val userPetService: UserPetService,
    private val petService: PetService,
    private val petLevelUpHistoryService: PetLevelUpHistoryService,
    private val userPetPolicy: UserPetPolicy,
) {
    fun findPetInfo(userId: Long): UserPet.Info {
        val (currentYear, currentMonth) = LocalDate.now().let { it.year to it.month }
        val pets = petService.findAllLatestSeasonPets(currentYear, currentMonth)
        return userPetService.findByUser(userId)
            ?: run {
                val level1Pet =
                    pets.find { it.levelType == Pet.LevelType.LEVEL_1 }
                        ?: throw ErrorException(ErrorType.INVALID_PET_LEVEL_TYPE)
                userPetService.append(userId, level1Pet)
            }
    }

    fun findCurrentSeasonPetHistory(userId: Long): Pair<UserPet.Info, List<UserPetLevelUpCurrentSeasonHistoryInfo>> {
        val (currentYear, currentMonth) = LocalDate.now().let { it.year to it.month }
        val pets = petService.findAllLatestSeasonPets(currentYear, currentMonth)
        val userPetInfo =
            userPetService.findByUser(userId) ?: pets
                .find { it.levelType == Pet.LevelType.LEVEL_1 }
                ?.let { userPetService.append(userId, it) }
                ?: throw ErrorException(ErrorType.INVALID_PET_LEVEL_TYPE)

        val histories = petLevelUpHistoryService.findAllBy(currentYear, currentMonth, userPetInfo.id)
        val levelTypeToHistory =
            histories
                .groupBy { it.levelType }
                .mapValues { (_, list) -> list.maxByOrNull { it.createdAt } }

        val petHistoryInfo =
            pets.map { petInfo ->
                val pointStandard = userPetPolicy.getMinLevelStandard(petInfo.levelType)
                val history = levelTypeToHistory[petInfo.levelType]
                UserPetLevelUpCurrentSeasonHistoryInfo(
                    userId = userId,
                    nickname = petInfo.levelType.adjective + userPetInfo.nickname,
                    levelType = petInfo.levelType,
                    point = pointStandard,
                    isLocked = userPetInfo.point <= pointStandard,
                    year = currentYear,
                    month = currentMonth,
                    createdAt =
                        if (petInfo.levelType == Pet.LevelType.LEVEL_1) {
                            LocalDateTime.of(currentYear, currentMonth, 1, 0, 0)
                        } else {
                            history?.createdAt ?: LocalDateTime.of(currentYear, currentMonth, 1, 0, 0)
                        },
                )
            }

        return Pair(userPetInfo, petHistoryInfo)
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
                    nickname = history.levelType.adjective + history.nickname,
                    levelType = history.levelType,
                    point = userPetPolicy.getMinLevelStandard(history.levelType),
                    year = history.year,
                    month = history.monthly,
                    createdAt = history.createdAt,
                )
            }
    }
}

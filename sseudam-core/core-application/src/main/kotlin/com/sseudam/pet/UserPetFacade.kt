package com.sseudam.pet

import com.sseudam.pet.component.UserPetPolicy
import com.sseudam.pet.result.UserPetCurrentSeasonHistoryResult
import com.sseudam.pet.result.UserPetLevelUpCurrentSeasonHistoryInfo
import com.sseudam.pet.result.UserPetLevelUpHistoryInfo
import com.sseudam.support.Cache
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import org.springframework.stereotype.Service
import tools.jackson.core.type.TypeReference
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

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

    fun findCurrentSeasonPetHistory(userId: Long): UserPetCurrentSeasonHistoryResult {
        val (currentYear, currentMonth) = LocalDate.now().run { year to month }
        val pets = petService.findAllLatestSeasonPets(currentYear, currentMonth)
        val userPetInfo =
            userPetService.findByUser(userId)
                ?: pets
                    .find { it.levelType == Pet.LevelType.LEVEL_1 }
                    ?.let { userPetService.append(userId, it) }
                ?: throw ErrorException(ErrorType.INVALID_PET_LEVEL_TYPE)

        val histories = petLevelUpHistoryService.findAllBy(currentYear, currentMonth, userPetInfo.id)
        val levelTypeToHistory =
            histories
                .groupBy { it.levelType }
                .mapValues { it.value.maxByOrNull { history -> history.createdAt } }

        val petHistoryInfo =
            pets.map { petInfo ->
                val pointStandard = userPetPolicy.getMinLevelStandard(petInfo.levelType)
                val history = levelTypeToHistory[petInfo.levelType]
                val userPetLevelType = userPetPolicy.getLevelType(userPetInfo.point)
                val isLocked = petInfo.levelType.level > userPetLevelType.level
                val createdAt = history?.createdAt ?: LocalDateTime.of(currentYear, currentMonth, 1, 0, 0)

                UserPetLevelUpCurrentSeasonHistoryInfo(
                    userId = userId,
                    nickname = petInfo.levelType.adjective + userPetInfo.nickname,
                    levelType = petInfo.levelType,
                    point = pointStandard,
                    isLocked = isLocked,
                    season =
                        userPetPolicy.getSeasonByYearMonth(
                            history?.year ?: currentYear,
                            history?.monthly ?: currentMonth,
                        ),
                    createdAt = createdAt,
                )
            }

        return UserPetCurrentSeasonHistoryResult(userPetInfo, petHistoryInfo)
    }

    fun findAllPetHistory(userId: Long): List<UserPetLevelUpHistoryInfo> =
        Cache.cache(
            ttl = 10,
            key = "pet:history:$userId",
            typeReference = object : TypeReference<List<UserPetLevelUpHistoryInfo>>() {},
        ) {
            val userPetHistories = petLevelUpHistoryService.findAllByUser(userId)
            if (userPetHistories.isEmpty()) return@cache emptyList()

            return@cache userPetHistories
                .groupBy { userPetPolicy.getSeasonByYearMonth(it.year, it.monthly) }
                .mapNotNull { (_, histories) ->
                    histories.maxByOrNull { it.levelType.level }
                }.map { history ->
                    UserPetLevelUpHistoryInfo(
                        userId = userId,
                        nickname = history.levelType.adjective + history.nickname,
                        levelType = history.levelType,
                        point = userPetPolicy.getMinLevelStandard(history.levelType),
                        season = userPetPolicy.getSeasonByYearMonth(history.year, history.monthly),
                        createdAt = history.createdAt,
                    )
                }
        }

    fun createBatchUserPet(
        currentYear: Int,
        currentMonth: Month,
    ) {
        val createLevelOnePet = petService.createPetSeason(currentYear, currentMonth)
        val allUserPet: List<UserPet.Info> = userPetService.findAll()
        userPetService.initPointForAllUsers(allUserPet, createLevelOnePet.id)
    }
}

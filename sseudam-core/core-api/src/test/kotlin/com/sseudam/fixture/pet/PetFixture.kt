package com.sseudam.fixture.pet

import com.navercorp.fixturemonkey.kotlin.setExp
import com.sseudam.pet.Pet
import com.sseudam.pet.PetLevelUpHistory
import com.sseudam.pet.UserPet
import com.sseudam.pet.component.LevelStandard
import com.sseudam.pet.result.UserPetLevelUpCurrentSeasonHistoryInfo
import com.sseudam.pet.result.UserPetLevelUpHistoryInfo
import com.sseudam.test.helper.fixtureBuilder
import com.sseudam.test.helper.fixtureBuilders
import net.jqwik.api.Arbitraries
import java.time.Month

object PetFixture {
    private const val DEFAULT_USER_ID = 1L
    private const val DEFAULT_PET_ID = 1L
    private const val DEFAULT_NICKNAME = "냥이"
    private const val DEFAULT_YEAR = 2025
    private val DEFAULT_MONTH = Month.JUNE
    private const val DEFAULT_SEASON = "2025-06"

    val petInfo =
        fixtureBuilder<Pet.Info> {
            setExp(Pet.Info::id, DEFAULT_PET_ID)
            setExp(Pet.Info::name, DEFAULT_NICKNAME)
            setExp(Pet.Info::levelType, Pet.LevelType.LEVEL_1)
            setExp(Pet.Info::year, DEFAULT_YEAR)
            setExp(Pet.Info::monthly, DEFAULT_MONTH)
        }

    val userPetInfo =
        fixtureBuilder<UserPet.Info> {
            setExp(UserPet.Info::id, DEFAULT_USER_ID)
            setExp(UserPet.Info::userId, DEFAULT_USER_ID)
            setExp(UserPet.Info::petId, DEFAULT_PET_ID)
            setExp(UserPet.Info::nickname, DEFAULT_NICKNAME)
            setExp(UserPet.Info::point, Arbitraries.longs().between(LevelStandard.LEVEL_4_MIN.toLong(), LevelStandard.LEVEL_4_MAX.toLong()))
        }

    val userPetInfos =
        fixtureBuilders<UserPet.Info>(
            block = {
                setExp(UserPet.Info::petId, DEFAULT_PET_ID)
                setExp(UserPet.Info::nickname, DEFAULT_NICKNAME)
                setExp(
                    UserPet.Info::point,
                    Arbitraries.longs().between(LevelStandard.LEVEL_1_MIN.toLong(), LevelStandard.SPECIAL_MIN.toLong()),
                )
            },
            size = 2,
        )

    val levelUpHistoryInfo =
        fixtureBuilder<UserPetLevelUpHistoryInfo> {
            setExp(UserPetLevelUpHistoryInfo::userId, DEFAULT_USER_ID)
            setExp(UserPetLevelUpHistoryInfo::nickname, DEFAULT_NICKNAME)
            setExp(
                UserPetLevelUpHistoryInfo::point,
                Arbitraries.longs().between(LevelStandard.LEVEL_2_MIN.toLong(), LevelStandard.LEVEL_2_MAX.toLong()),
            )
            setExp(UserPetLevelUpHistoryInfo::season, DEFAULT_SEASON)
            setExp(UserPetLevelUpHistoryInfo::levelType, Pet.LevelType.LEVEL_2)
        }

    val currentSeasonHistoryInfo =
        fixtureBuilder<UserPetLevelUpCurrentSeasonHistoryInfo> {
            setExp(UserPetLevelUpCurrentSeasonHistoryInfo::userId, DEFAULT_USER_ID)
            setExp(UserPetLevelUpCurrentSeasonHistoryInfo::nickname, DEFAULT_NICKNAME)
            setExp(UserPetLevelUpCurrentSeasonHistoryInfo::levelType, Pet.LevelType.LEVEL_2)
            setExp(
                UserPetLevelUpCurrentSeasonHistoryInfo::point,
                Arbitraries.longs().between(LevelStandard.LEVEL_2_MIN.toLong(), LevelStandard.LEVEL_2_MAX.toLong()),
            )
            setExp(UserPetLevelUpCurrentSeasonHistoryInfo::isLocked, false)
            setExp(UserPetLevelUpCurrentSeasonHistoryInfo::season, DEFAULT_SEASON)
        }

    val levelUpHistories =
        fixtureBuilders<UserPetLevelUpHistoryInfo>(
            block = {
                setExp(UserPetLevelUpHistoryInfo::userId, DEFAULT_USER_ID)
                setExp(UserPetLevelUpHistoryInfo::nickname, Arbitraries.strings().ofMinLength(2).ofMaxLength(12))
                setExp(
                    UserPetLevelUpHistoryInfo::point,
                    Arbitraries.longs().between(LevelStandard.LEVEL_1_MIN.toLong(), LevelStandard.SPECIAL_MIN.toLong()),
                )
                setExp(UserPetLevelUpHistoryInfo::season, Arbitraries.strings().withChars("0123456789-").ofLength(7))
                setExp(UserPetLevelUpHistoryInfo::levelType, Arbitraries.of(Pet.LevelType.entries))
            },
            size = 3,
        )

    val currentSeasonHistories =
        fixtureBuilders<UserPetLevelUpCurrentSeasonHistoryInfo>(
            block = {
                setExp(UserPetLevelUpCurrentSeasonHistoryInfo::userId, DEFAULT_USER_ID)
                setExp(UserPetLevelUpCurrentSeasonHistoryInfo::nickname, Arbitraries.strings().ofMinLength(2).ofMaxLength(12))
                setExp(
                    UserPetLevelUpCurrentSeasonHistoryInfo::point,
                    Arbitraries.longs().between(LevelStandard.LEVEL_1_MIN.toLong(), LevelStandard.SPECIAL_MIN.toLong()),
                )
                setExp(UserPetLevelUpCurrentSeasonHistoryInfo::isLocked, Arbitraries.of(true, false))
                setExp(UserPetLevelUpCurrentSeasonHistoryInfo::season, DEFAULT_SEASON)
                setExp(UserPetLevelUpCurrentSeasonHistoryInfo::levelType, Arbitraries.of(Pet.LevelType.entries))
            },
            size = 3,
        )

    val petLevelUpHistoryInfo =
        fixtureBuilder<PetLevelUpHistory.Info> {
            setExp(PetLevelUpHistory.Info::id, 1L)
            setExp(PetLevelUpHistory.Info::userId, DEFAULT_USER_ID)
            setExp(PetLevelUpHistory.Info::userPetId, DEFAULT_USER_ID)
            setExp(PetLevelUpHistory.Info::nickname, DEFAULT_NICKNAME)
            setExp(PetLevelUpHistory.Info::year, DEFAULT_YEAR)
            setExp(PetLevelUpHistory.Info::monthly, DEFAULT_MONTH)
            setExp(PetLevelUpHistory.Info::levelType, Pet.LevelType.LEVEL_2)
            setExp(PetLevelUpHistory.Info::updatedAt, null)
        }

    val petLevelUpHistories =
        fixtureBuilders<PetLevelUpHistory.Info>(
            block = {
                setExp(PetLevelUpHistory.Info::userId, DEFAULT_USER_ID)
                setExp(PetLevelUpHistory.Info::userPetId, DEFAULT_USER_ID)
                setExp(PetLevelUpHistory.Info::nickname, DEFAULT_NICKNAME)
                setExp(PetLevelUpHistory.Info::year, DEFAULT_YEAR)
                setExp(PetLevelUpHistory.Info::monthly, DEFAULT_MONTH)
                setExp(PetLevelUpHistory.Info::levelType, Arbitraries.of(Pet.LevelType.entries))
                setExp(PetLevelUpHistory.Info::updatedAt, null)
            },
            size = 2,
        )
}

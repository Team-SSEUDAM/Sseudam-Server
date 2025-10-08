package com.sseudam.application.pet

import com.sseudam.DevelopTest
import com.sseudam.fixture.pet.PetFixture
import com.sseudam.pet.Pet
import com.sseudam.pet.PetLevelUpHistoryService
import com.sseudam.pet.PetService
import com.sseudam.pet.UserPetFacade
import com.sseudam.pet.UserPetService
import com.sseudam.pet.component.UserPetPolicy
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.time.Month

@DevelopTest
class UserPetFacadeTest :
    DescribeSpec({
        val userPetService: UserPetService = mockk()
        val petService: PetService = mockk()
        val petLevelUpHistoryService: PetLevelUpHistoryService = mockk()
        val userPetPolicy: UserPetPolicy = mockk()
        val userPetFacade = UserPetFacade(userPetService, petService, petLevelUpHistoryService, userPetPolicy)

        describe("펫 정보 조회") {
            context("사용자의 펫이 존재하는 경우") {
                it("기존 펫 정보를 반환한다") {
                    val userId = 1L
                    val userPetInfo = PetFixture.userPetInfo

                    every { userPetService.findByUser(userId) } returns userPetInfo
                    every { petService.findAllLatestSeasonPets(any(), any()) } returns emptyList()

                    val result = userPetFacade.findPetInfo(userId)

                    result shouldBe userPetInfo
                    verify { userPetService.findByUser(userId) }
                }
            }

            context("사용자의 펫이 없는 경우") {
                it("새로운 LEVEL_1 펫을 생성하여 반환한다") {
                    val userId = 1L
                    val level1Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1)
                    val newUserPet = PetFixture.userPetInfo
                    val pets = listOf(level1Pet)

                    every { userPetService.findByUser(userId) } returns null
                    every { petService.findAllLatestSeasonPets(any(), any()) } returns pets
                    every { userPetService.append(userId, level1Pet) } returns newUserPet

                    val result = userPetFacade.findPetInfo(userId)

                    result shouldBe newUserPet
                    verify { userPetService.append(userId, level1Pet) }
                }
            }

            context("사용자의 펫이 없고 LEVEL_1 펫도 없는 경우") {
                it("예외가 발생한다") {
                    val userId = 1L
                    val pets = listOf(PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_2))

                    every { userPetService.findByUser(userId) } returns null
                    every { petService.findAllLatestSeasonPets(any(), any()) } returns pets

                    val exception =
                        shouldThrow<ErrorException> {
                            userPetFacade.findPetInfo(userId)
                        }

                    exception.errorType shouldBe ErrorType.INVALID_PET_LEVEL_TYPE
                }
            }
        }

        describe("현 시즌 펫 성장 기록 조회") {
            context("사용자의 펫과 히스토리가 있는 경우") {
                it("현 시즌 성장 기록을 반환한다") {
                    val userId = 1L
                    val userPetInfo = PetFixture.userPetInfo.copy(point = 250L)
                    val pets =
                        listOf(
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1),
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_2),
                        )
                    val histories = listOf(PetFixture.petLevelUpHistoryInfo.copy(userPetId = userPetInfo.id))

                    every { userPetService.findByUser(userId) } returns userPetInfo
                    every { petService.findAllLatestSeasonPets(any(), any()) } returns pets
                    every { petLevelUpHistoryService.findAllBy(any(), any(), userPetInfo.id) } returns histories
                    every { userPetPolicy.getMinLevelStandard(any()) } returns 20L
                    every { userPetPolicy.getLevelType(userPetInfo.point) } returns Pet.LevelType.LEVEL_4
                    every { userPetPolicy.getSeasonByYearMonth(any(), any()) } returns "2025-06"

                    val result = userPetFacade.findCurrentSeasonPetHistory(userId)

                    result.userPetInfo shouldBe userPetInfo
                    result.seasonHistories shouldHaveSize 2
                }
            }

            context("사용자의 펫이 없는 경우") {
                it("새로운 펫을 생성하고 기록을 반환한다") {
                    val userId = 1L
                    val level1Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1)
                    val newUserPet = PetFixture.userPetInfo
                    val pets = listOf(level1Pet)

                    every { userPetService.findByUser(userId) } returns null
                    every { petService.findAllLatestSeasonPets(any(), any()) } returns pets
                    every { userPetService.append(userId, level1Pet) } returns newUserPet
                    every { petLevelUpHistoryService.findAllBy(any(), any(), newUserPet.id) } returns emptyList()
                    every { userPetPolicy.getMinLevelStandard(any()) } returns 0L
                    every { userPetPolicy.getLevelType(newUserPet.point) } returns Pet.LevelType.LEVEL_4
                    every { userPetPolicy.getSeasonByYearMonth(any(), any()) } returns "2025-06"

                    val result = userPetFacade.findCurrentSeasonPetHistory(userId)

                    result.userPetInfo shouldBe newUserPet
                    verify { userPetService.append(userId, level1Pet) }
                }
            }

            context("잠긴 레벨이 있는 경우") {
                it("잠긴 레벨을 isLocked=true로 표시한다") {
                    val userId = 1L
                    val userPetInfo = PetFixture.userPetInfo.copy(point = 50L)
                    val pets =
                        listOf(
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1),
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_2),
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_3),
                        )

                    every { userPetService.findByUser(userId) } returns userPetInfo
                    every { petService.findAllLatestSeasonPets(any(), any()) } returns pets
                    every { petLevelUpHistoryService.findAllBy(any(), any(), userPetInfo.id) } returns emptyList()
                    every { userPetPolicy.getMinLevelStandard(any()) } returns 0L
                    every { userPetPolicy.getLevelType(userPetInfo.point) } returns Pet.LevelType.LEVEL_2
                    every { userPetPolicy.getSeasonByYearMonth(any(), any()) } returns "2025-06"

                    val result = userPetFacade.findCurrentSeasonPetHistory(userId)

                    val lockedHistory = result.seasonHistories.find { it.levelType == Pet.LevelType.LEVEL_3 }
                    lockedHistory shouldNotBe null
                    lockedHistory?.isLocked shouldBe true
                }
            }
        }

        describe("전체 펫 성장 기록 조회") {
            context("사용자의 히스토리가 있는 경우") {
                it("시즌별 최고 레벨 기록을 반환한다") {
                    val userId = 1L
                    val histories = PetFixture.petLevelUpHistories

                    every { petLevelUpHistoryService.findAllByUser(userId) } returns histories
                    every { userPetPolicy.getSeasonByYearMonth(any(), any()) } returns "2025-06"
                    every { userPetPolicy.getMinLevelStandard(any()) } returns 110L

                    val result = userPetFacade.findAllPetHistory(userId)

                    result shouldHaveSize 1
                    result.first().levelType shouldBe Pet.LevelType.LEVEL_3
                }
            }

            context("사용자의 히스토리가 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L

                    every { petLevelUpHistoryService.findAllByUser(userId) } returns emptyList()

                    val result = userPetFacade.findAllPetHistory(userId)

                    result.shouldBeEmpty()
                }
            }

            context("여러 시즌의 히스토리가 있는 경우") {
                it("각 시즌별 최고 레벨만 반환한다") {
                    val userId = 1L
                    val histories =
                        listOf(
                            PetFixture.petLevelUpHistoryInfo.copy(monthly = Month.MAY, levelType = Pet.LevelType.LEVEL_2),
                            PetFixture.petLevelUpHistoryInfo.copy(id = 2L, monthly = Month.JUNE, levelType = Pet.LevelType.LEVEL_3),
                        )

                    every { petLevelUpHistoryService.findAllByUser(userId) } returns histories
                    every { userPetPolicy.getSeasonByYearMonth(2025, Month.MAY) } returns "2025-05"
                    every { userPetPolicy.getSeasonByYearMonth(2025, Month.JUNE) } returns "2025-06"
                    every { userPetPolicy.getMinLevelStandard(Pet.LevelType.LEVEL_2) } returns 20L
                    every { userPetPolicy.getMinLevelStandard(Pet.LevelType.LEVEL_3) } returns 110L

                    val result = userPetFacade.findAllPetHistory(userId)

                    result shouldHaveSize 2
                }
            }
        }

        describe("배치 사용자 펫 생성") {
            context("새로운 시즌을 시작하는 경우") {
                it("모든 사용자의 펫을 초기화한다") {
                    val currentYear = 2025
                    val currentMonth = Month.JULY
                    val level1Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1)
                    val allUserPets = PetFixture.userPetInfos

                    every { petService.createPetSeason(currentYear, currentMonth) } returns level1Pet
                    every { userPetService.findAll() } returns allUserPets
                    every { userPetService.initPointForAllUsers(allUserPets, level1Pet.id) } just Runs

                    userPetFacade.createBatchUserPet(currentYear, currentMonth)

                    verify { petService.createPetSeason(currentYear, currentMonth) }
                    verify { userPetService.findAll() }
                    verify { userPetService.initPointForAllUsers(allUserPets, level1Pet.id) }
                }
            }

            context("사용자가 없는 경우") {
                it("펫만 생성하고 초기화는 스킨한다") {
                    val currentYear = 2025
                    val currentMonth = Month.JULY
                    val level1Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1)

                    every { petService.createPetSeason(currentYear, currentMonth) } returns level1Pet
                    every { userPetService.findAll() } returns emptyList()
                    every { userPetService.initPointForAllUsers(emptyList(), level1Pet.id) } just Runs

                    userPetFacade.createBatchUserPet(currentYear, currentMonth)

                    verify { userPetService.initPointForAllUsers(emptyList(), level1Pet.id) }
                }
            }
        }
    })

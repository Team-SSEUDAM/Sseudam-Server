package com.sseudam.application.pet

import com.sseudam.DevelopTest
import com.sseudam.fixture.pet.PetFixture
import com.sseudam.pet.Pet
import com.sseudam.pet.PetService
import com.sseudam.pet.component.PetAppender
import com.sseudam.pet.component.PetReader
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Month

@DevelopTest
class PetServiceTest :
    DescribeSpec({
        val petReader: PetReader = mockk()
        val petAppender: PetAppender = mockk()
        val petService = PetService(petReader, petAppender)

        describe("펫 시즌 생성") {
            context("유효한 연도와 월을 입력한 경우") {
                it("모든 레벨의 펫을 생성하고 LEVEL_1 펫을 반환한다") {
                    val currentYear = 2025
                    val currentMonth = Month.JUNE
                    val level1Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1)
                    val level2Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_2)
                    val level3Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_3)
                    val level4Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_4)
                    val specialPet = PetFixture.petInfo.copy(levelType = Pet.LevelType.SPECIAL)

                    every { petAppender.appendSeasonPet(match { it.levelType == Pet.LevelType.LEVEL_1 }) } returns level1Pet
                    every { petAppender.appendSeasonPet(match { it.levelType == Pet.LevelType.LEVEL_2 }) } returns level2Pet
                    every { petAppender.appendSeasonPet(match { it.levelType == Pet.LevelType.LEVEL_3 }) } returns level3Pet
                    every { petAppender.appendSeasonPet(match { it.levelType == Pet.LevelType.LEVEL_4 }) } returns level4Pet
                    every { petAppender.appendSeasonPet(match { it.levelType == Pet.LevelType.SPECIAL }) } returns specialPet

                    val result = petService.createPetSeason(currentYear, currentMonth)

                    result shouldBe level1Pet
                    result.levelType shouldBe Pet.LevelType.LEVEL_1
                    result.year shouldBe currentYear
                    result.monthly shouldBe currentMonth
                    verify(exactly = 5) { petAppender.appendSeasonPet(any()) }
                }
            }

            context("펫 생성 중 LEVEL_1이 없는 경우") {
                it("예외가 발생한다") {
                    val currentYear = 2025
                    val currentMonth = Month.JUNE
                    val level2Pet = PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_2)

                    every { petAppender.appendSeasonPet(any()) } returns level2Pet

                    val exception =
                        shouldThrow<ErrorException> {
                            petService.createPetSeason(currentYear, currentMonth)
                        }

                    exception.errorType shouldBe ErrorType.FAILED_PET_CREATION
                }
            }

            context("다양한 월로 펫 시즌을 생성하는 경우") {
                it("각 월에 맞는 펫을 생성한다") {
                    val currentYear = 2025
                    val months = listOf(Month.JANUARY, Month.JUNE, Month.DECEMBER)

                    months.forEach { month ->
                        val level1Pet =
                            PetFixture.petInfo.copy(
                                levelType = Pet.LevelType.LEVEL_1,
                                year = currentYear,
                                monthly = month,
                            )

                        every { petAppender.appendSeasonPet(any()) } returns level1Pet

                        val result = petService.createPetSeason(currentYear, month)

                        result.monthly shouldBe month
                    }
                }
            }
        }

        describe("펫 조회") {
            context("존재하는 펫 ID로 조회하는 경우") {
                it("펫 정보를 반환한다") {
                    val petId = 1L
                    val petInfo = PetFixture.petInfo

                    every { petReader.readBy(petId) } returns petInfo

                    val result = petService.findBy(petId)

                    result shouldBe petInfo
                    verify { petReader.readBy(petId) }
                }
            }

            context("존재하지 않는 펫 ID로 조회하는 경우") {
                it("예외가 발생한다") {
                    val petId = 999L

                    every { petReader.readBy(petId) } throws ErrorException(ErrorType.NOT_FOUND_DATA)

                    shouldThrow<ErrorException> {
                        petService.findBy(petId)
                    }

                    verify { petReader.readBy(petId) }
                }
            }
        }

        describe("최신 시즌 펫 목록 조회") {
            context("해당 시즌에 펫이 있는 경우") {
                it("펫 목록을 반환한다") {
                    val currentYear = 2025
                    val currentMonth = Month.JUNE
                    val pets =
                        listOf(
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_1),
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_2),
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_3),
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.LEVEL_4),
                            PetFixture.petInfo.copy(levelType = Pet.LevelType.SPECIAL),
                        )

                    every { petReader.readAllLatestSeasonPets(currentYear, currentMonth) } returns pets

                    val result = petService.findAllLatestSeasonPets(currentYear, currentMonth)

                    result shouldBe pets
                    result.size shouldBe 5
                    verify { petReader.readAllLatestSeasonPets(currentYear, currentMonth) }
                }
            }

            context("해당 시즌에 펫이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val currentYear = 2025
                    val currentMonth = Month.DECEMBER

                    every { petReader.readAllLatestSeasonPets(currentYear, currentMonth) } returns emptyList()

                    val result = petService.findAllLatestSeasonPets(currentYear, currentMonth)

                    result shouldBe emptyList()
                    verify { petReader.readAllLatestSeasonPets(currentYear, currentMonth) }
                }
            }

            context("다른 연도의 동일한 월로 조회하는 경우") {
                it("해당 연도의 펫만 반환한다") {
                    val year2024 = 2024
                    val year2025 = 2025
                    val month = Month.JUNE
                    val pets2024 = listOf(PetFixture.petInfo.copy(year = year2024))
                    val pets2025 = listOf(PetFixture.petInfo.copy(year = year2025))

                    every { petReader.readAllLatestSeasonPets(year2024, month) } returns pets2024
                    every { petReader.readAllLatestSeasonPets(year2025, month) } returns pets2025

                    val result2024 = petService.findAllLatestSeasonPets(year2024, month)
                    val result2025 = petService.findAllLatestSeasonPets(year2025, month)

                    result2024 shouldNotBe result2025
                    result2024.first().year shouldBe year2024
                    result2025.first().year shouldBe year2025
                }
            }
        }
    })

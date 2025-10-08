package com.sseudam.application.pet

import com.sseudam.DevelopTest
import com.sseudam.fixture.pet.PetFixture
import com.sseudam.pet.Pet
import com.sseudam.pet.component.LevelStandard
import com.sseudam.pet.component.UserPetPolicy
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.Month

@DevelopTest
class UserPetPolicyTest :
    DescribeSpec({
        val userPetPolicy = UserPetPolicy()

        describe("레벨 타입 조회") {
            context("LEVEL_1 범위의 포인트인 경우") {
                it("LEVEL_1을 반환한다") {
                    val points = listOf(0L, 10L, 19L)

                    points.forEach { point ->
                        val result = userPetPolicy.getLevelType(point)
                        result shouldBe Pet.LevelType.LEVEL_1
                    }
                }
            }

            context("LEVEL_2 범위의 포인트인 경우") {
                it("LEVEL_2를 반환한다") {
                    val points = listOf(20L, 50L, 109L)

                    points.forEach { point ->
                        val result = userPetPolicy.getLevelType(point)
                        result shouldBe Pet.LevelType.LEVEL_2
                    }
                }
            }

            context("LEVEL_3 범위의 포인트인 경우") {
                it("LEVEL_3을 반환한다") {
                    val points = listOf(110L, 150L, 219L)

                    points.forEach { point ->
                        val result = userPetPolicy.getLevelType(point)
                        result shouldBe Pet.LevelType.LEVEL_3
                    }
                }
            }

            context("LEVEL_4 범위의 포인트인 경우") {
                it("LEVEL_4를 반환한다") {
                    val points = listOf(220L, 250L, 299L)

                    points.forEach { point ->
                        val result = userPetPolicy.getLevelType(point)
                        result shouldBe Pet.LevelType.LEVEL_4
                    }
                }
            }

            context("SPECIAL 범위의 포인트인 경우") {
                it("SPECIAL을 반환한다") {
                    val points = listOf(300L, 500L, 1000L)

                    points.forEach { point ->
                        val result = userPetPolicy.getLevelType(point)
                        result shouldBe Pet.LevelType.SPECIAL
                    }
                }
            }

            context("음수 포인트인 경우") {
                it("예외가 발생한다") {
                    val exception =
                        shouldThrow<ErrorException> {
                            userPetPolicy.getLevelType(-1L)
                        }

                    exception.errorType shouldBe ErrorType.INVALID_CUMULATIVE_POINT
                }
            }

            context("경계값 포인트인 경우") {
                it("정확한 레벨을 반환한다") {
                    userPetPolicy.getLevelType(0L) shouldBe Pet.LevelType.LEVEL_1
                    userPetPolicy.getLevelType(19L) shouldBe Pet.LevelType.LEVEL_1
                    userPetPolicy.getLevelType(20L) shouldBe Pet.LevelType.LEVEL_2
                    userPetPolicy.getLevelType(109L) shouldBe Pet.LevelType.LEVEL_2
                    userPetPolicy.getLevelType(110L) shouldBe Pet.LevelType.LEVEL_3
                    userPetPolicy.getLevelType(219L) shouldBe Pet.LevelType.LEVEL_3
                    userPetPolicy.getLevelType(220L) shouldBe Pet.LevelType.LEVEL_4
                    userPetPolicy.getLevelType(299L) shouldBe Pet.LevelType.LEVEL_4
                    userPetPolicy.getLevelType(300L) shouldBe Pet.LevelType.SPECIAL
                }
            }
        }

        describe("최대 레벨 기준 조회") {
            context("LEVEL_1인 경우") {
                it("LEVEL_2 최소값을 반환한다") {
                    val result = userPetPolicy.getMaxLevelStandard(Pet.LevelType.LEVEL_1)
                    result shouldBe LevelStandard.LEVEL_2_MIN.toLong()
                }
            }

            context("LEVEL_2인 경우") {
                it("LEVEL_3 최소값을 반환한다") {
                    val result = userPetPolicy.getMaxLevelStandard(Pet.LevelType.LEVEL_2)
                    result shouldBe LevelStandard.LEVEL_3_MIN.toLong()
                }
            }

            context("LEVEL_3인 경우") {
                it("LEVEL_4 최소값을 반환한다") {
                    val result = userPetPolicy.getMaxLevelStandard(Pet.LevelType.LEVEL_3)
                    result shouldBe LevelStandard.LEVEL_4_MIN.toLong()
                }
            }

            context("LEVEL_4인 경우") {
                it("SPECIAL 최소값을 반환한다") {
                    val result = userPetPolicy.getMaxLevelStandard(Pet.LevelType.LEVEL_4)
                    result shouldBe LevelStandard.SPECIAL_MIN.toLong()
                }
            }

            context("SPECIAL인 경우") {
                it("0을 반환한다") {
                    val result = userPetPolicy.getMaxLevelStandard(Pet.LevelType.SPECIAL)
                    result shouldBe 0L
                }
            }
        }

        describe("최소 레벨 기준 조회") {
            context("LEVEL_1인 경우") {
                it("LEVEL_1 최소값을 반환한다") {
                    val result = userPetPolicy.getMinLevelStandard(Pet.LevelType.LEVEL_1)
                    result shouldBe LevelStandard.LEVEL_1_MIN.toLong()
                }
            }

            context("LEVEL_2인 경우") {
                it("LEVEL_2 최소값을 반환한다") {
                    val result = userPetPolicy.getMinLevelStandard(Pet.LevelType.LEVEL_2)
                    result shouldBe LevelStandard.LEVEL_2_MIN.toLong()
                }
            }

            context("LEVEL_3인 경우") {
                it("LEVEL_3 최소값을 반환한다") {
                    val result = userPetPolicy.getMinLevelStandard(Pet.LevelType.LEVEL_3)
                    result shouldBe LevelStandard.LEVEL_3_MIN.toLong()
                }
            }

            context("LEVEL_4인 경우") {
                it("LEVEL_4 최소값을 반환한다") {
                    val result = userPetPolicy.getMinLevelStandard(Pet.LevelType.LEVEL_4)
                    result shouldBe LevelStandard.LEVEL_4_MIN.toLong()
                }
            }

            context("SPECIAL인 경우") {
                it("SPECIAL 최소값을 반환한다") {
                    val result = userPetPolicy.getMinLevelStandard(Pet.LevelType.SPECIAL)
                    result shouldBe LevelStandard.SPECIAL_MIN.toLong()
                }
            }
        }

        describe("펫 정보로 시즌 조회") {
            context("유효한 펫 정보인 경우") {
                it("시즌 문자열을 반환한다") {
                    val petInfo = PetFixture.petInfo.copy(year = 2025, monthly = Month.JUNE)

                    val result = userPetPolicy.getSeasonByPetInfo(petInfo)

                    result shouldBe "2025-06"
                }
            }

            context("다양한 월의 펫 정보인 경우") {
                it("각 월에 맞는 시즌 문자열을 반환한다") {
                    val testCases =
                        listOf(
                            Month.JANUARY to "2025-01",
                            Month.JUNE to "2025-06",
                            Month.DECEMBER to "2025-12",
                        )

                    testCases.forEach { (month, expected) ->
                        val petInfo = PetFixture.petInfo.copy(year = 2025, monthly = month)
                        val result = userPetPolicy.getSeasonByPetInfo(petInfo)
                        result shouldBe expected
                    }
                }
            }
        }

        describe("연도와 월로 시즌 조회") {
            context("유효한 연도와 월인 경우") {
                it("시즌 문자열을 반환한다") {
                    val result = userPetPolicy.getSeasonByYearMonth(2025, Month.JUNE)
                    result shouldBe "2025-06"
                }
            }

            context("다양한 연도와 월의 조합인 경우") {
                it("각 조합에 맞는 시즌 문자열을 반환한다") {
                    val testCases =
                        listOf(
                            Triple(2024, Month.JANUARY, "2024-01"),
                            Triple(2025, Month.JUNE, "2025-06"),
                            Triple(2026, Month.DECEMBER, "2026-12"),
                        )

                    testCases.forEach { (year, month, expected) ->
                        val result = userPetPolicy.getSeasonByYearMonth(year, month)
                        result shouldBe expected
                    }
                }
            }

            context("한 자리 월인 경우") {
                it("제로 패딩된 시즌 문자열을 반환한다") {
                    val result = userPetPolicy.getSeasonByYearMonth(2025, Month.JANUARY)
                    result shouldBe "2025-01"
                }
            }

            context("두 자리 월인 경우") {
                it("시즌 문자열을 반환한다") {
                    val result = userPetPolicy.getSeasonByYearMonth(2025, Month.DECEMBER)
                    result shouldBe "2025-12"
                }
            }
        }
    })

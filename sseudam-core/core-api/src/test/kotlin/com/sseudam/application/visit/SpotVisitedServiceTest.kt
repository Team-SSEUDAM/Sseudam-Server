package com.sseudam.application.visit

import com.sseudam.DevelopTest
import com.sseudam.fixture.visit.VisitedFixture
import com.sseudam.visit.SpotVisitedService
import com.sseudam.visit.component.SpotVisitedAppender
import com.sseudam.visit.component.SpotVisitedReader
import com.sseudam.visit.component.SpotVisitedValidator
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDate

@DevelopTest
class SpotVisitedServiceTest :
    DescribeSpec({
        val spotVisitedAppender: SpotVisitedAppender = mockk()
        val spotVisitedReader: SpotVisitedReader = mockk()
        val spotVisitedValidator: SpotVisitedValidator = mockk()
        val spotVisitedService = SpotVisitedService(spotVisitedAppender, spotVisitedReader, spotVisitedValidator)

        describe("방문 추가") {
            context("유효한 방문 정보인 경우") {
                it("방문을 생성하고 결과를 반환한다") {
                    val create = VisitedFixture.spotVisitedCreate
                    val visitedInfo = VisitedFixture.spotVisitedInfo

                    every { spotVisitedAppender.append(create) } returns visitedInfo

                    val result = spotVisitedService.append(create)

                    result shouldBe visitedInfo
                    verify { spotVisitedAppender.append(create) }
                }
            }
        }

        describe("사용자별 방문 내역 조회") {
            context("방문 내역이 있는 경우") {
                it("방문 목록을 반환한다") {
                    val userId = 1L
                    val visitedList = listOf(VisitedFixture.spotVisitedInfo)

                    every { spotVisitedReader.readByUserId(userId) } returns visitedList

                    val result = spotVisitedService.findAllByUser(userId)

                    result shouldBe visitedList
                    verify { spotVisitedReader.readByUserId(userId) }
                }
            }

            context("방문 내역이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L

                    every { spotVisitedReader.readByUserId(userId) } returns emptyList()

                    val result = spotVisitedService.findAllByUser(userId)

                    result shouldBe emptyList()
                    verify { spotVisitedReader.readByUserId(userId) }
                }
            }
        }

        describe("오늘 사용자별 방문 내역 조회") {
            context("오늘 방문 내역이 있는 경우") {
                it("오늘 방문 목록을 반환한다") {
                    val userId = 1L
                    val today = LocalDate.now()
                    val visitedList = listOf(VisitedFixture.spotVisitedInfo)

                    every { spotVisitedReader.readTodayAllBy(userId, today) } returns visitedList

                    val result = spotVisitedService.findTodaySpotVisitedByUser(userId)

                    result shouldBe visitedList
                    verify { spotVisitedReader.readTodayAllBy(userId, today) }
                }
            }

            context("오늘 방문 내역이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L
                    val today = LocalDate.now()

                    every { spotVisitedReader.readTodayAllBy(userId, today) } returns emptyList()

                    val result = spotVisitedService.findTodaySpotVisitedByUser(userId)

                    result shouldBe emptyList()
                    verify { spotVisitedReader.readTodayAllBy(userId, today) }
                }
            }
        }

        describe("오늘 사용자와 장소별 방문 내역 조회") {
            context("오늘 해당 장소에 방문한 경우") {
                it("오늘 방문 목록을 반환한다") {
                    val userId = 1L
                    val spotId = 1L
                    val today = LocalDate.now()
                    val visitedList = listOf(VisitedFixture.spotVisitedInfo)

                    every { spotVisitedReader.readTodayAllBy(userId, spotId, today) } returns visitedList

                    val result = spotVisitedService.findTodaySpotVisitedByUserAndSpot(userId, spotId)

                    result shouldBe visitedList
                    verify { spotVisitedReader.readTodayAllBy(userId, spotId, today) }
                }
            }

            context("오늘 해당 장소에 방문하지 않은 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 1L
                    val spotId = 999L
                    val today = LocalDate.now()

                    every { spotVisitedReader.readTodayAllBy(userId, spotId, today) } returns emptyList()

                    val result = spotVisitedService.findTodaySpotVisitedByUserAndSpot(userId, spotId)

                    result shouldBe emptyList()
                    verify { spotVisitedReader.readTodayAllBy(userId, spotId, today) }
                }
            }
        }

        describe("최근 방문 조회") {
            context("최근 방문 기록이 있는 경우") {
                it("최근 방문 정보를 반환한다") {
                    val userId = 1L
                    val spotId = 1L
                    val visitedInfo = VisitedFixture.spotVisitedInfo

                    every { spotVisitedReader.readLastVisited(userId, spotId) } returns visitedInfo

                    val result = spotVisitedService.findLatestByUserAndSpot(userId, spotId)

                    result shouldBe visitedInfo
                    verify { spotVisitedReader.readLastVisited(userId, spotId) }
                }
            }

            context("최근 방문 기록이 없는 경우") {
                it("null을 반환한다") {
                    val userId = 1L
                    val spotId = 999L

                    every { spotVisitedReader.readLastVisited(userId, spotId) } returns null

                    val result = spotVisitedService.findLatestByUserAndSpot(userId, spotId)

                    result shouldBe null
                    verify { spotVisitedReader.readLastVisited(userId, spotId) }
                }
            }
        }

        describe("장소별 방문자 수 조회") {
            context("방문자가 있는 경우") {
                it("방문자 수를 반환한다") {
                    val spotId = 1L
                    val count = 10L

                    every { spotVisitedReader.countBySpotId(spotId) } returns count

                    val result = spotVisitedService.countBySpotId(spotId)

                    result shouldBe count
                    verify { spotVisitedReader.countBySpotId(spotId) }
                }
            }

            context("방문자가 없는 경우") {
                it("0을 반환한다") {
                    val spotId = 999L

                    every { spotVisitedReader.countBySpotId(spotId) } returns 0L

                    val result = spotVisitedService.countBySpotId(spotId)

                    result shouldBe 0L
                    verify { spotVisitedReader.countBySpotId(spotId) }
                }
            }
        }

        describe("방문 검증") {
            context("유효한 방문인 경우") {
                it("예외를 던지지 않는다") {
                    val todayVisited = listOf(VisitedFixture.spotVisitedInfo)
                    val todayVisitedSpot = VisitedFixture.spotVisitedInfo

                    every { spotVisitedValidator.verifyVisited(todayVisited, todayVisitedSpot) } just Runs

                    spotVisitedService.verifyVisited(todayVisited, todayVisitedSpot)

                    verify { spotVisitedValidator.verifyVisited(todayVisited, todayVisitedSpot) }
                }
            }
        }
    })

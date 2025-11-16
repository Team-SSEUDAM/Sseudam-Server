package com.sseudam.application.visit

import com.sseudam.DevelopTest
import com.sseudam.contract.trashspot.TrashSpotQueryContract
import com.sseudam.fixture.visit.VisitedFixture
import com.sseudam.pet.PetPointAction
import com.sseudam.support.error.ErrorException
import com.sseudam.support.error.ErrorType
import com.sseudam.visit.SpotVisitedFacade
import com.sseudam.visit.SpotVisitedService
import com.sseudam.visit.event.SpotVisitedEvent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher

@DevelopTest
class SpotVisitedFacadeTest :
    DescribeSpec({
        val spotVisitedService: SpotVisitedService = mockk()
        val trashSpotService: TrashSpotQueryContract = mockk()
        val applicationEventPublisher: ApplicationEventPublisher = mockk()
        val spotVisitedFacade =
            SpotVisitedFacade(
                spotVisitedService = spotVisitedService,
                trashSpotService = trashSpotService,
                applicationEventPublisher = applicationEventPublisher,
            )

        describe("장소 방문") {
            context("오늘 첫 방문인 경우") {
                it("방문을 생성하고 TODAY_FIRST_SPOT_VISITED 이벤트를 발행한다") {
                    val userId = 1L
                    val spotId = 1L
                    val visitedInfo = VisitedFixture.spotVisitedInfo

                    every { spotVisitedService.findTodaySpotVisitedByUser(userId) } returns emptyList()
                    every { spotVisitedService.verifyVisited(emptyList(), null) } just Runs
                    every { spotVisitedService.append(any()) } returns visitedInfo
                    every { applicationEventPublisher.publishEvent(any<SpotVisitedEvent>()) } just Runs

                    val result = spotVisitedFacade.visitSpot(userId, spotId, null)

                    result.isToday shouldBe true
                    result.visited shouldBe visitedInfo
                    verify { spotVisitedService.findTodaySpotVisitedByUser(userId) }
                    verify { spotVisitedService.verifyVisited(emptyList(), null) }
                    verify { spotVisitedService.append(any()) }
                    verify {
                        applicationEventPublisher.publishEvent(
                            match<SpotVisitedEvent> {
                                it.userId == userId && it.petPointAction == PetPointAction.TODAY_FIRST_SPOT_VISITED
                            },
                        )
                    }
                }
            }

            context("오늘 이미 다른 장소를 방문한 경우") {
                it("방문을 생성하고 SPOT_VISITED 이벤트를 발행한다") {
                    val userId = 1L
                    val spotId = 2L
                    val visitedInfo = VisitedFixture.spotVisitedInfo
                    val todayVisits = listOf(VisitedFixture.spotVisitedInfo.copy(spotId = 1L))

                    every { spotVisitedService.findTodaySpotVisitedByUser(userId) } returns todayVisits
                    every { spotVisitedService.verifyVisited(todayVisits, null) } just Runs
                    every { spotVisitedService.append(any()) } returns visitedInfo
                    every { applicationEventPublisher.publishEvent(any<SpotVisitedEvent>()) } just Runs

                    val result = spotVisitedFacade.visitSpot(userId, spotId, null)

                    result.isToday shouldBe false
                    result.visited shouldBe visitedInfo
                    verify { spotVisitedService.verifyVisited(todayVisits, null) }
                    verify {
                        applicationEventPublisher.publishEvent(
                            match<SpotVisitedEvent> {
                                it.userId == userId && it.petPointAction == PetPointAction.SPOT_VISITED
                            },
                        )
                    }
                }
            }

            context("방문 검증에서 예외가 발생하는 경우") {
                it("예외를 전파한다") {
                    val userId = 1L
                    val spotId = 1L
                    val todayVisits = listOf(VisitedFixture.spotVisitedInfo)

                    every { spotVisitedService.findTodaySpotVisitedByUser(userId) } returns todayVisits
                    every { spotVisitedService.verifyVisited(todayVisits, todayVisits[0]) } throws ErrorException(ErrorType.SPOT_VISITED_ALREADY)

                    val exception =
                        shouldThrow<ErrorException> {
                            spotVisitedFacade.visitSpot(userId, spotId, null)
                        }

                    exception.errorType shouldBe ErrorType.SPOT_VISITED_ALREADY
                    verify { spotVisitedService.findTodaySpotVisitedByUser(userId) }
                    verify { spotVisitedService.verifyVisited(todayVisits, todayVisits[0]) }
                }
            }
        }

        describe("사용자별 방문 내역 조회") {
            context("방문 내역이 있는 경우") {
                it("장소 정보와 함께 방문 목록을 반환한다") {
                    val userId = 1L
                    val visitedList = listOf(VisitedFixture.spotVisitedInfo)
                    val spotList = listOf(VisitedFixture.trashSpotInfo)

                    every { spotVisitedService.findAllByUser(userId) } returns visitedList
                    every { trashSpotService.findAllByIds(any()) } returns spotList

                    val result = spotVisitedFacade.findSpotVisitedByUserId(userId)

                    result.size shouldBe 1
                    verify { spotVisitedService.findAllByUser(userId) }
                    verify { trashSpotService.findAllByIds(any()) }
                }
            }

            context("방문 내역이 없는 경우") {
                it("빈 목록을 반환한다") {
                    val userId = 999L

                    every { spotVisitedService.findAllByUser(userId) } returns emptyList()

                    val result = spotVisitedFacade.findSpotVisitedByUserId(userId)

                    result shouldBe emptyList()
                    verify { spotVisitedService.findAllByUser(userId) }
                }
            }

            context("장소 정보가 없는 방문 내역이 있는 경우") {
                it("해당 방문 내역을 제외하고 반환한다") {
                    val userId = 1L
                    val visitedList =
                        listOf(
                            VisitedFixture.spotVisitedInfo.copy(spotId = 1L),
                            VisitedFixture.spotVisitedInfo.copy(spotId = 999L),
                        )
                    val spotList = listOf(VisitedFixture.trashSpotInfo.copy(id = 1L))

                    every { spotVisitedService.findAllByUser(userId) } returns visitedList
                    every { trashSpotService.findAllByIds(any()) } returns spotList

                    val result = spotVisitedFacade.findSpotVisitedByUserId(userId)

                    result.size shouldBe 1
                    result[0].spotId shouldBe 1L
                }
            }
        }
    })

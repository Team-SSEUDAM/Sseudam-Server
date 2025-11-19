package com.sseudam.application.trashspot

import com.sseudam.DevelopTest
import com.sseudam.contract.visit.SpotVisitedQueryContract
import com.sseudam.fixture.trashspot.TrashSpotFixture
import com.sseudam.trashspot.TrashSpotFacade
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.dto.TrashSpotLocation
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

@DevelopTest
class TrashSpotFacadeTest :
    DescribeSpec({
        val service: TrashSpotService = mockk()
        val imageService: TrashSpotImageService = mockk()
        val userService: UserService = mockk()
        val visitedService: SpotVisitedQueryContract = mockk()

        val trashSpotFacade = TrashSpotFacade(service, imageService, userService, visitedService)

        describe("장소 전체 조회") {
            it("FindAll 요청 결과를 반환하여 검증한다.") {
                val expected = TrashSpotFixture.infos
                every { service.findAll(any(), any(), any()) } returns expected

                val actual = trashSpotFacade.findAll(region = null, trashType = null, location = TrashSpotLocation.notSet())

                actual shouldBe expected
            }
        }

        describe("장소 상세 조회") {
            it("FindDetails 요청 결과를 반환하여 검증한다.") {
                val spot = TrashSpotFixture.info
                val image = TrashSpotFixture.image

                every { service.findBy(1L) } returns spot
                every { imageService.findBySpotId(1L) } returns listOf(image)
                every { userService.getProfile(any()) } returns null
                every { visitedService.countBySpotId(1L) } returns 5L

                val actual = trashSpotFacade.findDetails(1L)

                actual.trashSpot shouldBe spot
                actual.image shouldBe image
                actual.user shouldBe null
                actual.visitedCount shouldBe 5L
            }
        }
    })

package com.sseudam.domain.trashspot

import com.sseudam.DevelopTest
import com.sseudam.common.GeoConverter
import com.sseudam.common.GeoJson
import com.sseudam.fixture.trashspot.TrashSpotFixture
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.trashspot.TrashSpotFacade
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.dto.TrashSpotLocation
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserProfile
import com.sseudam.user.UserService
import com.sseudam.visit.SpotVisitedService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

@DevelopTest
class TrashSpotFacadeTest :
    DescribeSpec({
        val service: TrashSpotService = mockk()
        val imageService: TrashSpotImageService = mockk()
        val suggestionService: SuggestionService = mockk()
        val userService: UserService = mockk()
        val visitedService: SpotVisitedService = mockk()
        val geoConverter: GeoConverter = mockk()

        val trashSpotFacade = TrashSpotFacade(service, imageService, suggestionService, userService, visitedService, geoConverter)

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
                val suggestion =
                    SpotSuggestion.Info(
                        id = 1L,
                        userId = 123L,
                        spotName = "n",
                        point = spot.point,
                        region = spot.region,
                        address = spot.address,
                        trashType = spot.trashType,
                        imageUrl = "",
                        status = SuggestionStatus.WAITING,
                        createdAt = LocalDateTime.now(),
                    )
                val userProfile =
                    UserProfile(
                        id = 123L,
                        key = "k",
                        email = "e@example.com",
                        name = "u",
                        nickname = "nick",
                        address = spot.address,
                        createdAt = LocalDateTime.now(),
                    )

                every { service.findBy(1L) } returns spot
                every { imageService.findBySpotId(1L) } returns listOf(image)
                every { suggestionService.findSpotSuggestionByPoint(geoConverter.geoJsonPointToJtsPoint(spot.point as GeoJson.Point)) } returns suggestion
                every { userService.getProfile(123L) } returns userProfile
                every { visitedService.countBySpotId(1L) } returns 5L

                val actual = trashSpotFacade.findDetails(1L)

                actual.trashSpot shouldBe spot
                actual.image shouldBe image
                actual.user shouldBe userProfile
                actual.visitedCount shouldBe 5L
            }
        }
    })

package com.sseudam.domain.trashspot

import com.sseudam.fixture.trashspot.TrashSpotFixture
import com.sseudam.suggestion.SpotSuggestion
import com.sseudam.suggestion.SuggestionService
import com.sseudam.trashspot.TrashSpotFacade
import com.sseudam.trashspot.TrashSpotLocation
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.trashspot.image.TrashSpotImageService
import com.sseudam.user.UserProfile
import com.sseudam.user.UserService
import com.sseudam.visit.SpotVisitedService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime

class TrashSpotFacadeTest :
    DescribeSpec({
        val service: TrashSpotService = mockk()
        val imageService: TrashSpotImageService = mockk()
        val suggestionService: SuggestionService = mockk()
        val userService: UserService = mockk()
        val visitedService: SpotVisitedService = mockk()

        val facade = TrashSpotFacade(service, imageService, suggestionService, userService, visitedService)

        describe("findAll") {
            it("returns what the service returns") {
                val expected = TrashSpotFixture.infos
                every { service.findAll(any(), any(), any()) } returns expected

                val actual = facade.findAll(region = null, trashType = null, location = TrashSpotLocation.notSet())

                actual shouldBe expected
            }
        }

        describe("findDetails") {
            it("composes detail from services") {
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
                        createdAt = LocalDateTime.now(),
                    )
                val userProfile = UserProfile(id = 123L, key = "k", email = "e@example.com", name = "u", nickname = "nick", createdAt = LocalDateTime.now())

                every { service.findBy(1L) } returns spot
                every { imageService.findBySpotId(1L) } returns listOf(image)
                every { suggestionService.findSpotSuggestionBySite(spot.address.site) } returns suggestion
                every { userService.getProfile(123L) } returns userProfile
                every { visitedService.countBySpotId(1L) } returns 5L

                val actual = facade.findDetails(1L)

                actual.trashSpot shouldBe spot
                actual.image shouldBe image
                actual.user shouldBe userProfile
                actual.visitedCount shouldBe 5L
            }
        }
    })

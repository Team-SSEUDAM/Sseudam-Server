package com.sseudam.presentation.v1.visit

import com.sseudam.RestDocsTest
import com.sseudam.config.UserArgumentResolver
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.ARRAY
import com.sseudam.docs.support.Authorization
import com.sseudam.docs.support.BOOLEAN
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.NUMBER
import com.sseudam.docs.support.RestDocsUtils.headers
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.headerType
import com.sseudam.docs.support.type
import com.sseudam.fixture.visit.VisitedFixture
import com.sseudam.trashspot.TrashSpotService
import com.sseudam.user.User
import com.sseudam.visit.SpotVisitedFacade
import com.sseudam.visit.SpotVisitedService
import com.sseudam.visit.result.SpotVisitedResult
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.UUID

@RestDocsTest
class VisitedControllerTest : RestDocsTestSuite() {
    private lateinit var visitedController: VisitedController
    private lateinit var spotVisitedService: SpotVisitedService
    private lateinit var spotVisitedFacade: SpotVisitedFacade
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest() {
        userArgumentResolver = mockk()
        spotVisitedService = mockk()
        spotVisitedFacade = mockk()
        val trashSpotService = mockk<TrashSpotService>()
        visitedController = VisitedController(spotVisitedService, spotVisitedFacade, trashSpotService)
        mockMvcSpec = mockController(visitedController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(
                id = 1L,
                key = UUID.randomUUID().toString(),
            )
        every { trashSpotService.findById(any()) } returns mockk(relaxed = true)
    }

    @DisplayName("방문하기 - 200")
    @Test
    fun t1() {
        val visitedInfo = VisitedFixture.spotVisitedInfo
        val isToday = true

        every { spotVisitedFacade.visitSpot(any(), any()) } returns SpotVisitedResult(isToday, visitedInfo)

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .post("/api/v1/visited/{spotId}", 1L)
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "방문하기",
            DocsTag.VISITED,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotVisitedDetailResponse",
            responseBody(
                "id" type NUMBER means "방문 ID" example "1",
                "spotId" type NUMBER means "방문한 쓰레기통 장소 ID" example "1",
                "userId" type NUMBER means "방문한 사용자 ID" example "1",
                "site" type STRING means "방문한 쓰레기통 주소" example "서울특별시 강남구 테헤란로 123",
                "visitedAt" type STRING means "방문 시간" example "2023-10-01T12:00:00",
                "isToday" type BOOLEAN means "오늘 첫 방문 여부" example "true",
            ),
        )
    }

    @DisplayName("방문 내역 조회 - 200")
    @Test
    fun t2() {
        val visitedSpots = listOf(VisitedFixture.spotVisitedInfo)

        every { spotVisitedFacade.findSpotVisitedByUserId(any()) } returns visitedSpots

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .get("/api/v1/visited")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "방문 내역 조회",
            DocsTag.VISITED,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotVisitedAllResponse",
            responseBody(
                "list" type ARRAY means "방문 목록",
                "list[].id" type NUMBER means "방문 ID" example "1",
                "list[].spotId" type NUMBER means "방문한 쓰레기통 장소 ID" example "1",
                "list[].userId" type NUMBER means "방문한 사용자 ID" example "1",
                "list[].site" type STRING means "방문한 쓰레기통 주소" example "서울특별시 강남구 테헤란로 123",
                "list[].visitedAt" type STRING means "방문 시간" example "2023-10-01T12:00:00",
            ),
        )
    }

    @DisplayName("장소 최근 방문 여부 조회 - 200")
    @Test
    fun t3() {
        val visitedInfo = VisitedFixture.spotVisitedInfo

        every { spotVisitedService.findLatestByUserAndSpot(any(), any()) } returns visitedInfo

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .get("/api/v1/visited/{spotId}", 1L)
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "장소 최근 방문 여부 조회",
            DocsTag.VISITED,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotLatestVisitedResponse",
            responseBody(
                "spotId" type NUMBER means "방문한 장소의 ID" example "1",
                "userId" type NUMBER means "방문을 시도한 유저의 ID" example "1",
                "lastVisitedAt" type STRING means "최근 방문했던 시간" example "2025-07-01T12:00:00" isOptional true,
            ),
        )
    }

    @DisplayName("장소 방문자 수 조회 - 200")
    @Test
    fun t4() {
        val count = 10L

        every { spotVisitedService.countBySpotId(any()) } returns count

        val response =
            given()
                .contentType(ContentType.JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .get("/api/v1/visited/count/{spotId}", 1L)
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "장소 방문자 수 조회",
            DocsTag.VISITED,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotVisitedCountResponse",
            responseBody(
                "spotId" type NUMBER means "장소 ID" example "1",
                "visitedCount" type NUMBER means "방문자 수" example "10",
            ),
        )
    }
}

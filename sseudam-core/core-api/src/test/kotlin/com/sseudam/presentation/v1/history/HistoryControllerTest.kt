package com.sseudam.presentation.v1.history

import com.sseudam.RestDocsTest
import com.sseudam.config.UserArgumentResolver
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.ARRAY
import com.sseudam.docs.support.Authorization
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.ENUM
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.NUMBER
import com.sseudam.docs.support.OBJECT
import com.sseudam.docs.support.RestDocsUtils.headers
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.headerType
import com.sseudam.docs.support.type
import com.sseudam.fixture.history.HistoryFixture
import com.sseudam.history.HistoryFacade
import com.sseudam.history.dto.HistoryStatus
import com.sseudam.history.dto.SpotActionType
import com.sseudam.trashspot.TrashType
import com.sseudam.user.User
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider
import java.util.UUID

@RestDocsTest
class HistoryControllerTest : RestDocsTestSuite() {
    private lateinit var historyController: HistoryController
    private lateinit var historyFacade: HistoryFacade
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest(restDocumentation: RestDocumentationContextProvider) {
        historyFacade = mockk()
        userArgumentResolver = mockk()
        historyController = HistoryController(historyFacade)
        mockMvcSpec = mockController(historyController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(id = 1L, key = UUID.randomUUID().toString())
    }

    @DisplayName("내역 조회 - 200")
    @Test
    fun t1() {
        val histories = HistoryFixture.spotHistoryInfos

        every { historyFacade.findHistories(any()) } returns histories

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .get("/api/v1/histories")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "내역 조회",
            DocsTag.HISTORY,
            headers(
                "Authorization" headerType Authorization,
            ),
            "HistoryAllResponse",
            responseBody(
                "list" type ARRAY means "내역 목록",
                "list[].id" type NUMBER means "내역 ID" example "1",
                "list[].spotId" type NUMBER means "장소 ID" example "1" isOptional true,
                "list[].userId" type NUMBER means "사용자 ID" example "1",
                "list[].point" type OBJECT means "위치 정보",
                "list[].point.type" type STRING means "좌표 타입" example "Point",
                "list[].point.coordinates" type ARRAY means "좌표 배열" example "[126.977969, 37.566535]",
                "list[].spotName" type STRING means "장소 이름" example "테스트 쓰레기통",
                "list[].address" type OBJECT means "주소 정보",
                "list[].address.city" type STRING means "도시" example "강남구",
                "list[].address.site" type STRING means "상세 주소" example "서울시 강남구 강남동 1-4",
                "list[].trashType" type ENUM(TrashType::class) means "쓰레기통 타입" example "GENERAL",
                "list[].imageUrl" type STRING means "이미지 URL" example "https://example.com/image.jpg",
                "list[].status" type ENUM(HistoryStatus::class) means "내역 상태" example "WAITING",
                "list[].actionType" type ENUM(SpotActionType::class) means "액션 타입" example "REPORT",
                "list[].createdAt" type STRING means "생성 일자" example "2024-01-01T00:00:00",
            ),
        )
    }
}

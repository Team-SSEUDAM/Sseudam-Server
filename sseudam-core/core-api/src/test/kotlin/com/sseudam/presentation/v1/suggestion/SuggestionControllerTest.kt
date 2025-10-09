package com.sseudam.presentation.v1.suggestion

import com.sseudam.RestDocsTest
import com.sseudam.common.Region
import com.sseudam.config.UserArgumentResolver
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.ARRAY
import com.sseudam.docs.support.Authorization
import com.sseudam.docs.support.BOOLEAN
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.ENUM
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.NUMBER
import com.sseudam.docs.support.OBJECT
import com.sseudam.docs.support.RestDocsUtils.headers
import com.sseudam.docs.support.RestDocsUtils.requestBody
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.headerType
import com.sseudam.docs.support.type
import com.sseudam.fixture.suggestion.SuggestionFixture
import com.sseudam.suggestion.SuggestionFacade
import com.sseudam.suggestion.SuggestionService
import com.sseudam.suggestion.SuggestionStatus
import com.sseudam.suggestion.result.CreateSpotSuggestionResult
import com.sseudam.trashspot.TrashType
import com.sseudam.user.User
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.UUID

@RestDocsTest
class SuggestionControllerTest : RestDocsTestSuite() {
    private lateinit var suggestionController: SuggestionController
    private lateinit var suggestionService: SuggestionService
    private lateinit var suggestionFacade: SuggestionFacade
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest() {
        userArgumentResolver = mockk()
        suggestionService = mockk()
        suggestionFacade = mockk()
        suggestionController = SuggestionController(suggestionService, suggestionFacade)
        mockMvcSpec = mockController(suggestionController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(
                id = 1L,
                key = UUID.randomUUID().toString(),
            )
    }

    @DisplayName("제보하기 - 200")
    @Test
    fun t1() {
        val request = SuggestionFixture.spotSuggestionCreateRequest
        val suggestionInfo = SuggestionFixture.spotSuggestionInfo
        val s3ImageUrl = SuggestionFixture.s3ImageUrl

        every { suggestionFacade.createSpotSuggestion(any()) } returns CreateSpotSuggestionResult(suggestionInfo, s3ImageUrl)

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/suggestions")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "제보하기",
            DocsTag.SUGGESTION,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotSuggestionCreateRequest",
            "SuggestionImageUrlResponse",
            requestBody(
                "spotName" type STRING means "쓰레기통 이름" example "우리집 앞",
                "latitude" type NUMBER means "위도" example "37.566535",
                "longitude" type NUMBER means "경도" example "126.977969",
                "region" type ENUM(Region::class) means "지역" example "SEOUL",
                "city" type STRING means "구/군/시" example "강남구/거창군/동두천시",
                "site" type STRING means "주소" example "서울시 강남구 강남동 1-4",
                "trashType" type ENUM(TrashType::class) means "쓰레기통 유형" example "GENERAL",
            ),
            responseBody(
                "suggestionId" type NUMBER means "제보 ID",
                "presignedUrl" type STRING means "PresignedUrl",
            ),
        )
    }

    @DisplayName("사용자 제보 내역 조회 - 200")
    @Test
    fun t2() {
        val suggestions = listOf(SuggestionFixture.spotSuggestionInfo)

        every { suggestionService.findAllSpotSuggestionByUser(any()) } returns suggestions

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .get("/api/v1/suggestions")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "사용자 제보 내역 조회",
            DocsTag.SUGGESTION,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotSuggestionAllResponse",
            responseBody(
                "list" type ARRAY means "제보 목록",
                "list[].id" type NUMBER means "제보 ID",
                "list[].point" type OBJECT means "제보 위치",
                "list[].point.type" type STRING means "좌표 타입",
                "list[].point.coordinates" type ARRAY means "좌표 배열",
                "list[].region" type ENUM(Region::class) means "제보 지역",
                "list[].address" type OBJECT means "제보 주소",
                "list[].address.city" type STRING means "도시",
                "list[].address.site" type STRING means "상세 주소",
                "list[].trashType" type ENUM(TrashType::class) means "제보 쓰레기통 타입",
                "list[].imageUrl" type STRING means "제보된 쓰레기통 이미지",
                "list[].status" type ENUM(SuggestionStatus::class) means "제보 상태",
                "list[].createdAt" type STRING means "제보 시간",
            ),
        )
    }

    @DisplayName("제보 시 쓰레기통 검증 - 200")
    @Test
    fun t3() {
        val request = SuggestionFixture.suggestionValidationRequest

        every { suggestionFacade.validateSpotSuggestion(any()) } returns true

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/suggestions/validate")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "제보 시 쓰레기통 검증",
            DocsTag.SUGGESTION,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SuggestionValidationRequest",
            "SuggestionValidationResponse",
            requestBody(
                "name" type STRING means "검증할 쓰레기통 장소명",
            ),
            responseBody(
                "isValid" type BOOLEAN means "검증 결과" example "true",
            ),
        )
    }

    @DisplayName("제보 취소 - 200")
    @Test
    fun t4() {
        val request = SuggestionFixture.suggestionCancelRequest

        every { suggestionService.cancel(any()) } just Runs

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/suggestions/cancel")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "제보 취소",
            DocsTag.SUGGESTION,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SuggestionCancelRequest",
            "SuggestionMessageResponse",
            requestBody(
                "suggestionId" type NUMBER means "취소할 제보 ID" example "1",
            ),
            responseBody(
                "message" type STRING means "취소 완료 메시지" example "제보가 취소되었습니다.",
            ),
        )
    }
}

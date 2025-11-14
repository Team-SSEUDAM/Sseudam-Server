package com.sseudam.presentation.v1.trashspot

import com.sseudam.RestDocsTest
import com.sseudam.common.Region
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.ARRAY
import com.sseudam.docs.support.DOUBLE
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.ENUM
import com.sseudam.docs.support.LONG
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.NUMBER
import com.sseudam.docs.support.OBJECT
import com.sseudam.docs.support.ParamEnum
import com.sseudam.docs.support.RestDocsUtils.pathVariables
import com.sseudam.docs.support.RestDocsUtils.requestParameters
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.parameterType
import com.sseudam.docs.support.type
import com.sseudam.fixture.trashspot.TrashSpotFixture
import com.sseudam.trashspot.TrashSpotFacade
import com.sseudam.common.TrashType
import com.sseudam.trashspot.result.TrashSpotDetail
import io.mockk.every
import io.mockk.mockk
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

@RestDocsTest
class TrashSpotControllerTest : RestDocsTestSuite() {
    private lateinit var trashSpotController: TrashSpotController
    private lateinit var trashSpotFacade: TrashSpotFacade

    @BeforeEach
    fun setUpTest() {
        trashSpotFacade = mockk()
        trashSpotController = TrashSpotController(trashSpotFacade)
        mockMvcSpec = mockController(trashSpotController)
    }

    @DisplayName("쓰레기통 장소 목록 조회 - 200")
    @Test
    fun t1() {
        every {
            trashSpotFacade.findAll(
                region = any(),
                trashType = any(),
                location = any(),
            )
        } returns TrashSpotFixture.infos

        val response =
            given()
                .param("region", Region.SEOUL)
                .param("type", TrashType.GENERAL)
                .param("swLat", 37.0)
                .param("swLng", 126.0)
                .param("neLat", 38.0)
                .param("neLng", 128.0)
                .get("/api/v1/trash-spots")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)

        response.makeDocument(
            "쓰레기통 장소 목록 조회",
            DocsTag.TRASH_SPOT,
            requestParameters =
                requestParameters(
                    "region" parameterType ParamEnum(Region::class) means "지역" example "SEOUL" isOptional true,
                    "type" parameterType ParamEnum(TrashType::class) means "쓰레기통 유형" example "GENERAL" isOptional true,
                    "swLat" parameterType DOUBLE means "남서쪽 위도" example "37.0" isOptional true,
                    "swLng" parameterType DOUBLE means "남서쪽 경도" example "126.0" isOptional true,
                    "neLat" parameterType DOUBLE means "북동쪽 위도" example "38.0" isOptional true,
                    "neLng" parameterType DOUBLE means "북동쪽 경도" example "128.0" isOptional true,
                ),
            responseSchema = "TrashSpotAllResponse",
            responseBody =
                responseBody(
                    "list" type ARRAY means "쓰레기통 장소 목록",
                    "list[].id" type NUMBER means "쓰레기통 장소 ID",
                    "list[].name" type STRING means "쓰레기통 장소명",
                    "list[].region" type ENUM(Region::class) means "지역",
                    "list[].trashType" type ENUM(TrashType::class) means "쓰레기통 유형",
                    "list[].address" type OBJECT means "주소 정보",
                    "list[].address.city" type STRING means "도시",
                    "list[].address.site" type STRING means "상세 주소",
                    "list[].point" type OBJECT means "좌표 정보",
                    "list[].point.type" type STRING means "좌표 타입",
                    "list[].point.coordinates" type ARRAY means "좌표 배열",
                    "list[].updatedAt" type STRING means "수정 시간" isOptional true,
                ),
        )
    }

    @Test
    @DisplayName("쓰레기통 장소 상세 조회 - 200")
    fun t2() {
        val trashSpotDetail =
            TrashSpotDetail(
                trashSpot = TrashSpotFixture.info,
                image = TrashSpotFixture.image,
                user = null,
                visitedCount = 0L,
            )

        every { trashSpotFacade.findDetails(1L) } returns trashSpotDetail

        val response =
            given()
                .pathParams("spotId", 1L)
                .get("/api/v1/trash-spots/{spotId}")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "쓰레기통 장소 상세 조회",
            DocsTag.TRASH_SPOT,
            pathVariable =
                pathVariables(
                    "spotId" parameterType LONG means "쓰레기통 장소 ID" example "1",
                ),
            responseSchema = "TrashSpotDetailsResponse",
            responseBody =
                responseBody(
                    "id" type NUMBER means "쓰레기통 장소 ID",
                    "suggestionerId" type NUMBER means "제보자 ID" isOptional true,
                    "suggestionerName" type STRING means "제보자 이름" isOptional true,
                    "name" type STRING means "쓰레기통 장소명",
                    "region" type ENUM(Region::class) means "지역",
                    "address" type OBJECT means "주소 정보",
                    "address.city" type STRING means "도시",
                    "address.site" type STRING means "상세 주소",
                    "point" type OBJECT means "좌표 정보",
                    "point.type" type STRING means "좌표 타입",
                    "point.coordinates" type ARRAY means "좌표 배열",
                    "trashType" type ENUM(TrashType::class) means "쓰레기통 유형",
                    "visitedCount" type NUMBER means "방문 횟수",
                    "imageUrl" type STRING means "이미지 URL" isOptional true,
                    "lastVisitedAt" type STRING means "최근 방문 시간" isOptional true,
                    "updatedAt" type STRING means "수정 시간" isOptional true,
                ),
        )
    }
}

package com.sseudam.presentation.v1.report

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
import com.sseudam.fixture.report.ReportFixture
import com.sseudam.report.ReportFacade
import com.sseudam.report.ReportService
import com.sseudam.report.ReportStatus
import com.sseudam.report.ReportType
import com.sseudam.report.result.CreateSpotReportResult
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
class ReportControllerTest : RestDocsTestSuite() {
    private lateinit var reportController: ReportController
    private lateinit var reportService: ReportService
    private lateinit var reportFacade: ReportFacade
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest() {
        userArgumentResolver = mockk()
        reportService = mockk()
        reportFacade = mockk()
        reportController = ReportController(reportService, reportFacade)
        mockMvcSpec = mockController(reportController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(
                id = 1L,
                key = UUID.randomUUID().toString(),
            )
    }

    @DisplayName("신고하기 - 200")
    @Test
    fun t1() {
        val request = ReportFixture.spotReportCreateRequest
        val reportInfo = ReportFixture.spotReportInfo
        val s3ImageUrl = ReportFixture.s3ImageUrl

        every { reportFacade.createSpotReport(any()) } returns CreateSpotReportResult(reportInfo, s3ImageUrl.presignedUrl)

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/reports/{spotId}", 1L)
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "신고하기",
            DocsTag.REPORT,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotReportCreateRequest",
            "ReportImageUrlResponse",
            requestBody(
                "reportType" type ENUM(ReportType::class) means "신고 타입" example "POINT",
                "latitude" type NUMBER means "위도" example "37.566535",
                "longitude" type NUMBER means "경도" example "126.977969",
                "spotName" type STRING means "쓰레기통 이름" example "우리집 앞",
                "region" type ENUM(Region::class) means "지역" example "SEOUL",
                "city" type STRING means "구/군/시" example "강남구",
                "site" type STRING means "주소" example "서울시 강남구 강남동 1-4",
                "trashType" type ENUM(TrashType::class) means "쓰레기통 유형" example "GENERAL",
                "reason" type STRING means "기타 신고 사유" example "잘못된 위치에 있어요" isOptional true,
            ),
            responseBody(
                "reportId" type NUMBER means "신고 ID" example "1",
                "presignedUrl" type STRING means "PresignedUrl" example "https://example.com/presigned-url",
            ),
        )
    }

    @DisplayName("신고 상세 내역 - 200")
    @Test
    fun t2() {
        val reportDetail = ReportFixture.spotReportDetail

        every { reportFacade.findReportDetails(any()) } returns reportDetail

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .get("/api/v1/reports/{reportId}", 1L)
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "신고 상세 내역",
            DocsTag.REPORT,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotReportResponse",
            responseBody(
                "id" type NUMBER means "신고 ID" example "1",
                "spotId" type NUMBER means "장소 ID" example "1",
                "spotName" type STRING means "장소 이름" example "테스트 쓰레기통",
                "region" type ENUM(Region::class) means "지역" example "SEOUL",
                "userId" type NUMBER means "신고자 ID" example "1",
                "reportType" type ENUM(ReportType::class) means "신고 타입" example "POINT",
                "point" type OBJECT means "신고 위치",
                "point.type" type STRING means "좌표 타입" example "Point",
                "point.coordinates" type ARRAY means "좌표 배열" example "[126.977969, 37.566535]",
                "address" type OBJECT means "신고 주소",
                "address.city" type STRING means "도시" example "강남구",
                "address.site" type STRING means "상세 주소" example "서울시 강남구 강남동 1-4",
                "trashType" type ENUM(TrashType::class) means "쓰레기통 타입" example "GENERAL",
                "imageUrl" type STRING means "신고된 이미지 url" example "https://example.com/image.jpg",
                "status" type ENUM(ReportStatus::class) means "신고 상태" example "WAITING",
                "rejectReason" type STRING means "반려 사유" isOptional true,
                "createdAt" type STRING means "신고 시간" example "2024-01-01T00:00:00",
            ),
        )
    }

    @DisplayName("사용자 신고 내역 조회 - 200")
    @Test
    fun t3() {
        val reports = listOf(ReportFixture.spotReportDetail)

        every { reportService.findAllDetailsByUserId(any()) } returns reports

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .get("/api/v1/reports")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "사용자 신고 내역 조회",
            DocsTag.REPORT,
            headers(
                "Authorization" headerType Authorization,
            ),
            "SpotReportAllResponse",
            responseBody(
                "list" type ARRAY means "신고 목록",
                "list[].id" type NUMBER means "신고 ID" example "1",
                "list[].spotId" type NUMBER means "장소 ID" example "1",
                "list[].spotName" type STRING means "장소 이름" example "테스트 쓰레기통",
                "list[].region" type ENUM(Region::class) means "지역" example "SEOUL",
                "list[].userId" type NUMBER means "신고자 ID" example "1",
                "list[].reportType" type ENUM(ReportType::class) means "신고 타입" example "POINT",
                "list[].point" type OBJECT means "신고 위치",
                "list[].point.type" type STRING means "좌표 타입" example "Point",
                "list[].point.coordinates" type ARRAY means "좌표 배열" example "[126.977969, 37.566535]",
                "list[].address" type OBJECT means "신고 주소",
                "list[].address.city" type STRING means "도시" example "강남구",
                "list[].address.site" type STRING means "상세 주소" example "서울시 강남구 강남동 1-4",
                "list[].trashType" type ENUM(TrashType::class) means "쓰레기통 타입" example "GENERAL",
                "list[].imageUrl" type STRING means "신고된 이미지 url" example "https://example.com/image.jpg",
                "list[].status" type ENUM(ReportStatus::class) means "신고 상태" example "WAITING",
                "list[].rejectReason" type STRING means "반려 사유" isOptional true,
                "list[].createdAt" type STRING means "신고 시간" example "2024-01-01T00:00:00",
            ),
        )
    }

    @DisplayName("신고 시 쓰레기통 검증 - 200")
    @Test
    fun t4() {
        val request = ReportFixture.reportValidationRequest

        every { reportFacade.validateSpotReport(any()) } returns true

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/reports/validate")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "신고 시 쓰레기통 검증",
            DocsTag.REPORT,
            headers(
                "Authorization" headerType Authorization,
            ),
            "ReportValidationRequest",
            "ReportValidationResponse",
            requestBody(
                "name" type STRING means "검증할 쓰레기통 장소명" example "테스트 쓰레기통",
            ),
            responseBody(
                "isValid" type BOOLEAN means "검증 결과" example "true",
            ),
        )
    }

    @DisplayName("신고 취소 - 200")
    @Test
    fun t5() {
        val request = ReportFixture.reportCancelRequest

        every { reportService.cancel(any()) } just Runs

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .body(request)
                .post("/api/v1/reports/cancel")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "신고 취소",
            DocsTag.REPORT,
            headers(
                "Authorization" headerType Authorization,
            ),
            "ReportCancelRequest",
            "ReportMessageResponse",
            requestBody(
                "reportId" type NUMBER means "취소할 신고 ID" example "1",
            ),
            responseBody(
                "message" type STRING means "취소 완료 메시지" example "신고가 취소되었습니다.",
            ),
        )
    }
}

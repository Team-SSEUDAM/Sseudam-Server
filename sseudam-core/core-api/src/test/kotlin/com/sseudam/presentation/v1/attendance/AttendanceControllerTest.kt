package com.sseudam.presentation.v1.attendance

import com.sseudam.RestDocsTest
import com.sseudam.attendance.AttendanceFacade
import com.sseudam.config.UserArgumentResolver
import com.sseudam.docs.RestDocsTestSuite
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
import com.sseudam.fixture.attendance.AttendanceFixture
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
class AttendanceControllerTest : RestDocsTestSuite() {
    private lateinit var attendanceController: AttendanceController
    private lateinit var attendanceFacade: AttendanceFacade
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest(restDocumentation: RestDocumentationContextProvider) {
        attendanceFacade = mockk()
        userArgumentResolver = mockk()
        attendanceController = AttendanceController(attendanceFacade)
        mockMvcSpec = mockController(attendanceController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(
                id = 1L,
                key = UUID.randomUUID().toString(),
            )
    }

    @DisplayName("출석 체크 - 200")
    @Test
    fun t1() {
        val attendanceResult = AttendanceFixture.attendanceResult

        every { attendanceFacade.todayAttendance(any()) } returns attendanceResult

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(ContentType.JSON)
                .post("/api/v1/attendance")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "출석 체크",
            DocsTag.HISTORY,
            headers(
                "Authorization" headerType Authorization,
            ),
            "AttendanceResponse",
            responseBody(
                "userId" type NUMBER means "사용자 ID" example "1",
                "date" type STRING means "출석 날짜" example "2025-06-30",
                "isToday" type BOOLEAN means "오늘 출석 여부" example "true",
                "continuity" type NUMBER means "출석 연속 횟수" example "3",
                "isContinuity" type BOOLEAN means "연속 출석 여부" example "true",
                "createdAt" type STRING means "출석 시간" example "2025-06-30T12:00:00",
            ),
        )
    }
}

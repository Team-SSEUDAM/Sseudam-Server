package com.sseudam.presentation.v1.notification

import com.sseudam.RestDocsTest
import com.sseudam.config.UserArgumentResolver
import com.sseudam.docs.RestDocsTestSuite
import com.sseudam.docs.support.ARRAY
import com.sseudam.docs.support.Authorization
import com.sseudam.docs.support.DocsTag
import com.sseudam.docs.support.ENUM
import com.sseudam.docs.support.LONG
import com.sseudam.docs.support.MockMvcExtensions.makeDocument
import com.sseudam.docs.support.NUMBER
import com.sseudam.docs.support.RestDocsUtils.headers
import com.sseudam.docs.support.RestDocsUtils.requestParameters
import com.sseudam.docs.support.RestDocsUtils.responseBody
import com.sseudam.docs.support.STRING
import com.sseudam.docs.support.headerType
import com.sseudam.docs.support.parameterType
import com.sseudam.docs.support.type
import com.sseudam.fixture.notification.NotificationFixture
import com.sseudam.notification.NotificationService
import com.sseudam.notification.NotificationType
import com.sseudam.notification.ReadStatus
import com.sseudam.user.User
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import java.util.UUID

@RestDocsTest
class NotificationControllerTest : RestDocsTestSuite() {
    private lateinit var notificationController: NotificationController
    private lateinit var notificationService: NotificationService
    private lateinit var userArgumentResolver: UserArgumentResolver

    @BeforeEach
    fun setUpTest() {
        userArgumentResolver = mockk()
        notificationService = mockk()
        notificationController = NotificationController(notificationService)
        mockMvcSpec = mockController(notificationController, userArgumentResolver)
        every { userArgumentResolver.supportsParameter(any()) } returns true
        every { userArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns
            User(
                id = 1L,
                key = UUID.randomUUID().toString(),
            )
    }

    @DisplayName("알림 조회 - 200")
    @Test
    fun t1() {
        val cursor = NotificationFixture.notificationCursor

        every { notificationService.findAllNotifications(any(), any()) } returns cursor

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .queryParam("size", 10)
                .queryParam("lastId", 1)
                .get("/api/v1/notifications")
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            identifier = "알림 조회",
            docsTag = DocsTag.NOTIFICATION,
            requestHeader =
                headers(
                    "Authorization" headerType Authorization,
                ),
            responseSchema = "NotificationAllCursorResponse",
            requestParameters =
                requestParameters(
                    "size" parameterType LONG means "조회할 알림 개수" example "10",
                    "lastId" parameterType LONG means "마지막 알림 ID" example "1" isOptional true,
                ),
            responseBody =
                responseBody(
                    "list" type ARRAY means "알림 목록",
                    "list[].id" type NUMBER means "알림 ID" example "1",
                    "list[].userId" type NUMBER means "사용자 ID" example "1",
                    "list[].type" type ENUM(NotificationType::class) means "알림 타입" example "APPROVE_SUGGESTION",
                    "list[].parameterValue" type NUMBER means "알림 파라미터 값" example "1",
                    "list[].topic" type STRING means "알림 토픽" example "쓰담",
                    "list[].contents" type STRING means "알림 내용" example "누군가 쓰담님이 제보한 쓰레기통에 쓰레기를 버렸어요!",
                    "list[].readStatus" type ENUM(ReadStatus::class) means "읽음 상태" example "UNREAD",
                    "list[].createdAt" type STRING means "생성일" example "2025-10-08T17:15:28.001012",
                    "nextCursor" type NUMBER means "다음 커서 ID" example "2" isOptional true,
                ),
        )
    }

    @DisplayName("알림 읽음 처리 - 200")
    @Test
    fun t2() {
        every { notificationService.markAsRead(any(), any()) } just Runs

        val response =
            given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .put("/api/v1/notifications/{notificationId}/read", 1L)
                .then()
                .status(HttpStatus.OK)

        response.makeDocument(
            "알림 읽음 처리",
            DocsTag.NOTIFICATION,
            headers(
                "Authorization" headerType Authorization,
            ),
            "NotificationMessageResponse",
            responseBody(
                "message" type STRING means "읽음 처리 완료 메시지" example "알림이 읽음 처리되었습니다.",
            ),
        )
    }
}

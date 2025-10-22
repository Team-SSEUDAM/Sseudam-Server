package com.sseudam.application.notification

import com.sseudam.DevelopTest
import com.sseudam.fixture.notification.NotificationFixture
import com.sseudam.notification.NotificationService
import com.sseudam.notification.component.NotificationStoredAppender
import com.sseudam.notification.component.NotificationStoredKeyGenerator
import com.sseudam.notification.component.NotificationStoredReader
import com.sseudam.notification.component.NotificationStoredUpdater
import com.sseudam.support.cursor.CursorRequest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class NotificationServiceTest :
    DescribeSpec({
        val notificationStoredAppender: NotificationStoredAppender = mockk()
        val notificationStoredReader: NotificationStoredReader = mockk()
        val notificationStoredUpdater: NotificationStoredUpdater = mockk()
        val notificationStoredKeyGenerator: NotificationStoredKeyGenerator = mockk()

        val notificationService =
            NotificationService(
                notificationStoredAppender,
                notificationStoredReader,
                notificationStoredUpdater,
                notificationStoredKeyGenerator,
            )

        describe("알림 추가") {
            context("유효한 알림 정보인 경우") {
                it("알림을 생성하고 결과를 반환한다") {
                    val create = NotificationFixture.notificationStoredCreate
                    val notificationInfo = NotificationFixture.notificationStoredInfo

                    every { notificationStoredKeyGenerator.generate() } returns "notification-key-1"
                    every { notificationStoredAppender.append(any()) } returns notificationInfo

                    val result = notificationService.append(create)

                    result shouldBe notificationInfo
                    verify { notificationStoredKeyGenerator.generate() }
                    verify { notificationStoredAppender.append(any()) }
                }
            }
        }

        describe("알림 일괄 추가") {
            context("유효한 알림 목록인 경우") {
                it("알림 목록을 생성하고 결과를 반환한다") {
                    val commands = listOf(NotificationFixture.createNotificationStoredCommand)
                    val notificationInfos = listOf(NotificationFixture.notificationStoredInfo)

                    every { notificationStoredKeyGenerator.generate() } returns "notification-key-1"
                    every { notificationStoredAppender.appendAll(any()) } returns notificationInfos

                    val result = notificationService.appendAll(commands)

                    result shouldBe notificationInfos
                    verify { notificationStoredKeyGenerator.generate() }
                    verify { notificationStoredAppender.appendAll(any()) }
                }
            }

            context("빈 목록인 경우") {
                it("빈 목록을 반환한다") {
                    val commands = emptyList<com.sseudam.notification.command.CreateNotificationStoredCommand>()

                    every { notificationStoredAppender.appendAll(any()) } returns emptyList()

                    val result = notificationService.appendAll(commands)

                    result shouldBe emptyList()
                    verify { notificationStoredAppender.appendAll(any()) }
                }
            }
        }

        describe("알림 목록 조회") {
            context("알림이 있는 경우") {
                it("커서 페이지네이션된 알림 목록을 반환한다") {
                    val userId = 1L
                    val cursorRequest = CursorRequest(size = 10, lastId = null)
                    val cursor = NotificationFixture.notificationCursor

                    every { notificationStoredReader.findAllBy(userId, cursorRequest) } returns cursor

                    val result = notificationService.findAllNotifications(userId, cursorRequest)

                    result shouldBe cursor
                    verify { notificationStoredReader.findAllBy(userId, cursorRequest) }
                }
            }

            context("알림이 없는 경우") {
                it("빈 커서를 반환한다") {
                    val userId = 999L
                    val cursorRequest = CursorRequest(size = 10, lastId = null)
                    val emptyCursor =
                        com.sseudam.support.cursor
                            .Cursor(emptyList<com.sseudam.notification.NotificationStored.Info>(), null, 10)

                    every { notificationStoredReader.findAllBy(userId, cursorRequest) } returns emptyCursor

                    val result = notificationService.findAllNotifications(userId, cursorRequest)

                    result.content shouldBe emptyList()
                    result.nextCursor shouldBe null
                }
            }
        }

        describe("알림 읽음 처리") {
            context("유효한 알림 ID인 경우") {
                it("알림을 읽음 상태로 변경한다") {
                    val userId = 1L
                    val notificationId = 1L

                    every { notificationStoredUpdater.markAsRead(userId, notificationId) } just Runs

                    notificationService.markAsRead(userId, notificationId)

                    verify { notificationStoredUpdater.markAsRead(userId, notificationId) }
                }
            }
        }
    })

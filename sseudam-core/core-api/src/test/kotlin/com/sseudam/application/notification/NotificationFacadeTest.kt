package com.sseudam.application.notification

import com.sseudam.DevelopTest
import com.sseudam.fixture.notification.NotificationFixture
import com.sseudam.fixture.user.UserFixture
import com.sseudam.notification.NotificationFacade
import com.sseudam.notification.NotificationService
import com.sseudam.notification.component.NotificationStoredKeyGenerator
import com.sseudam.notification.fcm.FcmSender
import com.sseudam.user.UserDeviceService
import com.sseudam.user.UserService
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class NotificationFacadeTest :
    DescribeSpec({
        val userDeviceService: UserDeviceService = mockk()
        val userService: UserService = mockk()
        val notificationStoredKeyGenerator: NotificationStoredKeyGenerator = mockk()
        val notificationService: NotificationService = mockk()
        val fcmSender: FcmSender = mockk()

        val notificationFacade =
            NotificationFacade(
                userDeviceService,
                userService,
                notificationStoredKeyGenerator,
                notificationService,
                fcmSender,
            )

        describe("주간 알림 메시지 생성") {
            context("FCM 토큰이 있는 사용자가 있는 경우") {
                it("알림 메시지를 생성하고 저장한다") {
                    val userDevice = NotificationFixture.userDeviceInfo
                    val userProfile = UserFixture.userProfile

                    every { userDeviceService.findAll() } returns listOf(userDevice)
                    every { userService.findAllBy(any()) } returns listOf(userProfile)
                    every { notificationStoredKeyGenerator.generate() } returns "notification-key-1"
                    every { notificationService.appendAll(any()) } returns emptyList()

                    val result = notificationFacade.createWeeklyNotificationMessages()

                    result.size shouldBe 1
                    result[0].fcmToken shouldBe "fcm-token-1"
                    verify { userDeviceService.findAll() }
                    verify { userService.findAllBy(any()) }
                    verify { notificationService.appendAll(any()) }
                }
            }

            context("FCM 토큰이 없는 경우") {
                it("빈 목록을 반환한다") {
                    every { userDeviceService.findAll() } returns emptyList()

                    val result = notificationFacade.createWeeklyNotificationMessages()

                    result.shouldBeEmpty()
                    verify { userDeviceService.findAll() }
                }
            }

            context("FCM 토큰이 빈 문자열인 경우") {
                it("빈 목록을 반환한다") {
                    val userDevice = NotificationFixture.userDeviceInfo.copy(fcmToken = "")

                    every { userDeviceService.findAll() } returns listOf(userDevice)

                    val result = notificationFacade.createWeeklyNotificationMessages()

                    result.shouldBeEmpty()
                    verify { userDeviceService.findAll() }
                }
            }
        }

        describe("새 펫 알림 전송") {
            context("FCM 토큰이 있는 사용자가 있는 경우") {
                it("알림을 전송하고 저장한다") {
                    val userDevice = NotificationFixture.userDeviceInfo
                    val userProfile = UserFixture.userProfile

                    every { userDeviceService.findAll() } returns listOf(userDevice)
                    every { userService.findAllBy(any()) } returns listOf(userProfile)
                    every { fcmSender.sendAll(any()) } returns emptyList()
                    every { notificationStoredKeyGenerator.generate() } returns "notification-key-1"
                    every { notificationService.appendAll(any()) } returns emptyList()

                    notificationFacade.sendNewPetNotifications()

                    verify { userDeviceService.findAll() }
                    verify { userService.findAllBy(any()) }
                    verify { fcmSender.sendAll(any()) }
                    verify { notificationService.appendAll(any()) }
                }
            }

            context("FCM 토큰이 없는 경우") {
                it("아무 작업도 수행하지 않는다") {
                    every { userDeviceService.findAll() } returns emptyList()

                    notificationFacade.sendNewPetNotifications()

                    verify { userDeviceService.findAll() }
                }
            }
        }
    })

package com.sseudam.application.version.app

import com.sseudam.DevelopTest
import com.sseudam.common.DeviceType
import com.sseudam.fixture.version.app.AppVersionFixture
import com.sseudam.version.app.AppVersionService
import com.sseudam.version.app.component.AppVersionReader
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

@DevelopTest
class AppVersionServiceTest :
    DescribeSpec({
        lateinit var appVersionReader: AppVersionReader
        lateinit var appVersionService: AppVersionService

        beforeEach {
            appVersionReader = mockk()
            appVersionService = AppVersionService(appVersionReader)
        }

        describe("앱 버전 조회") {
            context("IOS 디바이스 타입으로 조회하는 경우") {
                it("IOS 앱 버전 정보를 반환한다") {
                    every { appVersionReader.readByDeviceType(DeviceType.IOS) } returns AppVersionFixture.iosVersion

                    val result = appVersionService.getAppVersion(DeviceType.IOS)

                    result.deviceType shouldBe DeviceType.IOS
                    result.currentVersion shouldBe "1.0.1"
                    result.criticalVersion shouldBe "1.0.0"
                    verify { appVersionReader.readByDeviceType(DeviceType.IOS) }
                }
            }
        }
    })

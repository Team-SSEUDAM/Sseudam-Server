package com.sseudam.fixture.version.app

import com.sseudam.common.DeviceType
import com.sseudam.version.app.AppVersion

object AppVersionFixture {
    val iosVersion =
        AppVersion(
            deviceType = DeviceType.IOS,
            currentVersion = "1.0.1",
            criticalVersion = "1.0.0",
        )

    val androidVersion =
        AppVersion(
            deviceType = DeviceType.ANDROID,
            currentVersion = "1.0.1",
            criticalVersion = "1.0.0",
        )
}

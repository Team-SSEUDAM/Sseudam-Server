package com.sseudam.presentation.v1.version.app.response

import com.sseudam.common.DeviceType
import com.sseudam.version.app.AppVersion

data class AppVersionResponse(
    val deviceType: DeviceType,
    val version: String,
) {
    companion object {
        fun from(appVersion: AppVersion): AppVersionResponse =
            with(appVersion) {
                AppVersionResponse(
                    deviceType = deviceType,
                    version = version,
                )
            }
    }
}

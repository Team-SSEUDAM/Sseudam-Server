package com.sseudam.presentation.v1.version.app.response

import com.sseudam.common.DeviceType
import com.sseudam.version.app.AppVersion
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "앱 버전 조회 응답 Json")
data class AppVersionResponse(
    @Schema(description = "앱 OS", example = "IOS")
    val deviceType: DeviceType,
    @Schema(description = "앱 최신 버전", example = "1.0.1")
    val currentVersion: String,
    @Schema(description = "강제 업데이트 대상 버전", example = "1.0.0")
    val criticalVersion: String,
) {
    companion object {
        fun from(appVersion: AppVersion): AppVersionResponse =
            with(appVersion) {
                AppVersionResponse(
                    deviceType = deviceType,
                    currentVersion = currentVersion,
                    criticalVersion = criticalVersion,
                )
            }
    }
}

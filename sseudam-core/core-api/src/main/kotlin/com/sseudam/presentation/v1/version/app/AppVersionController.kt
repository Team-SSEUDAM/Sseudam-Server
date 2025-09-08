package com.sseudam.presentation.v1.version.app

import com.sseudam.common.DeviceType
import com.sseudam.presentation.v1.annotation.ApiV1Controller
import com.sseudam.presentation.v1.version.app.response.AppVersionResponse
import com.sseudam.version.app.AppVersionService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "📱 Version API", description = "버전 관련 API입니다.")
@ApiV1Controller
class AppVersionController(
    private val appVersionService: AppVersionService,
) {
    @Operation(summary = "앱 버전 조회", description = "앱 버전에 대한 조회를 합니다.")
    @GetMapping("/versions/app")
    fun appVersionFind(
        @RequestParam deviceType: DeviceType,
    ): AppVersionResponse = AppVersionResponse.from(appVersionService.getAppVersion(deviceType))
}

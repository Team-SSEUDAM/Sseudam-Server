package com.sseudam.version.app

import com.sseudam.common.DeviceType
import com.sseudam.version.app.component.AppVersionReader
import org.springframework.stereotype.Service

@Service
class AppVersionService(
    private val appVersionReader: AppVersionReader,
) {
    fun getAppVersion(deviceType: DeviceType): AppVersion = appVersionReader.readByDeviceType(deviceType)
}
